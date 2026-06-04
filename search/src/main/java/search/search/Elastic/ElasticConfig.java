package search.search.Elastic;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import jakarta.annotation.PostConstruct;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConfig {

    @Bean
    public ElasticsearchClient elasticsearchClient() {


        String host = System.getenv().getOrDefault("ELASTICSEARCH_HOST", "localhost");
        int port = Integer.parseInt(System.getenv().getOrDefault("ELASTICSEARCH_PORT", "9200"));

        RestClient restClient = RestClient.builder(
                new HttpHost(host, port, "http")
        ).build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
        );

        return new ElasticsearchClient(transport);
    }

}