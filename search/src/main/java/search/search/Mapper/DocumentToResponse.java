package search.search.Mapper;

import search.search.Elastic.ProductDocument;
import search.search.Response.ProductResponse;

import java.util.List;

public class DocumentToResponse {
    public static List<ProductResponse> mapToResponse(List<ProductDocument> documents){
        return documents.stream().map(document -> {
            ProductResponse response = new ProductResponse();
            response.setTitle(document.getTitle());
            response.setDescription(document.getDescription());
            response.setPrice(document.getPrice());
            response.setCategory(document.getCategory());
            response.setSeller(document.getSeller());
            return response;
        }).toList();
    }
}
