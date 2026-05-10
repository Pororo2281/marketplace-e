package search.search.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import search.search.Elastic.ProductSearchRepo;
import search.search.Response.SearchResponse;
import search.search.Service.SearchService;


import java.util.List;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final ProductSearchRepo repository;
    private final SearchService service;

    public SearchController(ProductSearchRepo repository, SearchService service) {
        this.repository = repository;
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<SearchResponse> search(@RequestParam(required = false) String query,
                                                 @RequestParam(required = false) String category,
                                                 @RequestParam(required = false) String seller,
                                                 @RequestParam(required = false) Integer minPrice,
                                                 @RequestParam(required = false) Integer maxPrice
                                        ) {
        return ResponseEntity.ok(service.findByTitleOrDescriptionContaining(query,category,seller,minPrice,maxPrice));
    }

    @GetMapping("/suggestions")
    public ResponseEntity<List<String>> suggest(@RequestParam("q") String prefix) {
        return ResponseEntity.ok(service.suggest(prefix));
    }
}
