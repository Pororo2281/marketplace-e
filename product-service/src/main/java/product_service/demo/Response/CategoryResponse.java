package product_service.demo.Response;

public class CategoryResponse {

    private Long id;

    private String name;

    private String slug;

    private String description;

    private Integer sortOrder;

    private Boolean isActive ;

    private ParentCategoryResponse parent;

    private Long childrenCount;


    public CategoryResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public ParentCategoryResponse getParent() {
        return parent;
    }

    public void setParent(ParentCategoryResponse parent) {
        this.parent = parent;
    }

    public Long getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(Long childrenCount) {
        this.childrenCount = childrenCount;
    }
}
