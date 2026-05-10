package search.search.Response;

public class CategoryCountResponse {
    private String category;
    private long count;

    public CategoryCountResponse(String category, long count) {
        this.category = category;
        this.count = count;
    }

    public CategoryCountResponse() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public long getCount() {
        return count;
    }
}
