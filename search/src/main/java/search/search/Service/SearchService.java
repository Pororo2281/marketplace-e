package search.search.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.FieldSuggester;
import org.springframework.stereotype.Service;
import search.search.Elastic.ProductDocument;
import search.search.Exception.SearchServiceException;
import search.search.Mapper.DocumentToResponse;
import search.search.Response.CategoryCountResponse;


import java.io.IOException;
import java.util.List;

@Service
public class SearchService {

    private final ElasticsearchClient client;

    public SearchService(ElasticsearchClient client) {
        this.client = client;
    }

    public search.search.Response.SearchResponse findByTitleOrDescriptionContaining(String text,
                                                                                    String category,
                                                                                    String seller,
                                                                                    Integer minPrice,
                                                                                    Integer maxPrice)  {

        SearchResponse<ProductDocument> response = null;
        try {
            response = client.search(s->s
                    .index("product-index")
                    .query(q->q
                            .bool(b->{
                                if (text!=null&&!text.isBlank()){
                                b.must(m->m
                                        .multiMatch(mm->mm
                                                .query(text)
                                                .fields("title^2", "description")
                                                .fuzziness("AUTO")
                                        )
                                );
                                }
                                if (category!=null){
                                    b.filter(f->f
                                            .term(t->t
                                                    .field("category")
                                                    .value(category)
                                            )
                                    );
                                }
                                if (seller!=null){
                                    b.filter(f->f
                                            .term(t->t
                                                    .field("seller")
                                                    .value(seller)
                                            )
                                    );
                                }
                                if(minPrice!=null||maxPrice!=null){
                                    b.filter(f->f
                                            .range(r->{
                                                if (minPrice!=null){
                                                    r.number(n->n
                                                            .field("price")
                                                            .gte(Double.valueOf(minPrice))
                                                    );
                                                }
                                                if (maxPrice!=null){
                                                    r.number(n->n
                                                            .field("price")
                                                            .lte(Double.valueOf(maxPrice))
                                                    );
                                                }
                                                return r;
                                            }));
                                }
                                return b;

                            }))
                            .aggregations("categories",a->a
                                    .terms(t->t
                                            .field("category"))),
                    ProductDocument.class);
        } catch (IOException e){
            throw new SearchServiceException("Elasticsearch request failed",e);
        }

        List<CategoryCountResponse> categories = response.aggregations()
                .get("categories")
                .sterms()
                .buckets()
                .array()
                .stream()
                .map(bucket->{
                    CategoryCountResponse categoryCountResponse = new CategoryCountResponse();
                    categoryCountResponse.setCategory(bucket.key().stringValue());
                    categoryCountResponse.setCount(bucket.docCount());
                    return categoryCountResponse;
                })
                .toList();

        search.search.Response.SearchResponse searchResponse = new search.search.Response.SearchResponse();
        searchResponse.setCategories(categories);
        searchResponse.setProducts(DocumentToResponse.mapToResponse(response.hits().hits().stream()
                .map(hit -> hit.source())
                .toList()));

        return searchResponse;
    }


    public List<String> suggest(String text){
        SearchResponse<ProductDocument> response = null;
        try{
            response = client.search(s->s
                    .index("product-index")
                    .suggest(suggestion->suggestion
                            .suggesters("title-suggest",st->st
                                    .prefix(text)
                                    .completion(c->c
                                            .field("suggest")
                                            .size(5)
                                            .skipDuplicates(true)
                                            .fuzzy(f->f
                                                    .fuzziness("AUTO"))
                                    )

                            )), ProductDocument.class);
        }
        catch (IOException e){
            throw new SearchServiceException("Elasticsearch request failed",e);
        }


        return response.suggest().get("title-suggest")
                .stream().flatMap(sug->sug.completion().options().stream())
                .map(opt->opt.text())
                .toList();
    }

}

