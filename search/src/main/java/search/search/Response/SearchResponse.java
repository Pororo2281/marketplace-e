package search.search.Response;

import java.util.List;

public class SearchResponse {
    private List<CategoryCountResponse> categories;
    private List<ProductResponse> products;

    public List<CategoryCountResponse> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryCountResponse> categories) {
        this.categories = categories;
    }

    public List<ProductResponse> getProducts() {
        return products;
    }

    public void setProducts(List<ProductResponse> products) {
        this.products = products;
    }
}
