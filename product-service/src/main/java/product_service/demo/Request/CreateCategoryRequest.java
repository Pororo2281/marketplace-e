package product_service.demo.Request;

import jakarta.validation.constraints.*;

public class CreateCategoryRequest {

    @NotBlank(message = "Name cannot be empty")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    private String name;

    @NotBlank(message = "Slug cannot be empty")
    @Size(max = 100, message = "Slug cannot exceed 100 characters")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Slug can contain only lowercase letters, numbers and hyphens")
    private String slug;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @Positive(message = "Parent ID must be positive")
    private Long parentId;

    @NotNull(message = "Sort order cannot be null")
    @Min(value = 0, message = "Sort order must be at least 0")
    @Max(value = 1000, message = "Sort order must not exceed 1000")
    private Integer sortOrder;

    @NotNull(message = "isActive flag cannot be null")
    private Boolean isActive;


    public CreateCategoryRequest() {}

    public CreateCategoryRequest(String name, String slug, String description,
                                 Long parentId, Integer sortOrder, Boolean isActive) {
        this.name = name;
        this.slug = slug;
        this.description = description;
        this.parentId = parentId;
        this.sortOrder = sortOrder;
        this.isActive = isActive;
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

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
}