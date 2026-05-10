package search.search.Rabbit;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Service;
import search.search.Elastic.ProductDocument;
import search.search.Elastic.ProductSearchRepo;
import search.search.Enum.ProductEventType;
import search.search.Event.ProductBatchEvent;
import search.search.Event.ProductEvent;

import java.util.List;

@Service
public class RabbitListener {

    private final ObjectMapper objectMapper;
    private final ProductSearchRepo repository;
    private final RabbitAdmin rabbitAdmin;
    private final Queue queue;
    private final ElasticsearchClient client;

    public RabbitListener(ObjectMapper objectMapper, ProductSearchRepo repository, RabbitAdmin rabbitAdmin, Queue queue, ElasticsearchClient client) {
        this.objectMapper = objectMapper;
        this.repository = repository;
        this.rabbitAdmin = rabbitAdmin;
        this.queue = queue;
        this.client = client;
    }

    @PostConstruct
    private void declareQueue() {
        rabbitAdmin.declareQueue(queue);
    }

    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = "${rabbitmq.queue.product}")
    public void ProductCreatedOrUpdated(String message) throws Exception {

        boolean exists = client.indices()
                .exists(e -> e.index("product-index"))
                .value();

        if (!exists) {

            client.indices().create(c -> c
                    .index("product-index")
                    .mappings(m -> m
                            .properties("title", p -> p.text(t -> t))
                            .properties("description", p -> p.text(t -> t))
                            .properties("price", p -> p.float_(f -> f))
                            .properties("category", p -> p.keyword(k -> k))
                            .properties("seller", p -> p.keyword(k -> k))
                            .properties("suggest", p -> p.completion(c1 -> c1))
                    )
            );
        }

        JsonNode node = objectMapper.readTree(message);

        if (node.has("events")) {

            ProductBatchEvent batch = objectMapper.treeToValue(node, ProductBatchEvent.class);
            handleBatch(batch.getEvents());
            return;
        }

        ProductEvent event = objectMapper.treeToValue(node, ProductEvent.class);
        handleSingle(event);
    }

    private void handleBatch(List<ProductEvent> events) {

        try {
            var bulk = new co.elastic.clients.elasticsearch.core.BulkRequest.Builder();

            for (ProductEvent event : events) {

                ProductDocument doc = map(event);

                bulk.operations(op -> op.index(i -> i
                        .index("product-index")
                        .id(event.getId())
                        .document(doc)
                ));
            }
            System.out.println(events.size()+" products indexed in batch");
            client.bulk(bulk.build());

        } catch (Exception e) {
            throw new RuntimeException("Batch indexing failed", e);
        }
    }

    private void handleSingle(ProductEvent event) {
        ProductEventType type = event.getType();

        switch (type){
            case CREATED, UPDATED -> {
                ProductDocument doc = map(event);

                System.out.println("Product created or updated with id: " + event.getId());
                repository.save(doc);
            }
            case DELETED -> {
                if (!repository.existsById(event.getId())){
                    System.out.println("Product not found in search index with id: " + event.getId());
                }
                repository.deleteById(event.getId());
                System.out.println("Product deleted with id: " + event.getId());
            }
        }

    }

    private ProductDocument map(ProductEvent event) {
        ProductDocument doc = new ProductDocument();
        doc.setId(event.getId());
        doc.setTitle(event.getTitle());
        doc.setDescription(event.getDescription());
        doc.setPrice(event.getPrice());
        doc.setCategory(event.getCategory());
        doc.setSeller(event.getSeller());
        doc.setSuggest(new String[]{event.getTitle(), event.getDescription()});
        return doc;
    }


}
