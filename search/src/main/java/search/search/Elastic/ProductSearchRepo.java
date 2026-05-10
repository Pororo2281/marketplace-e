package search.search.Elastic;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductSearchRepo extends ElasticsearchRepository<ProductDocument,String> {

}
