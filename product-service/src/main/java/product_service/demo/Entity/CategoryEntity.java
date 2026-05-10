package product_service.demo.Entity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private CategoryEntity parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryEntity> children = new ArrayList<>();

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String slug;

    private String description;

    @Column(name = "sort_order")
    private Integer sortOrder ;

    @Column(name = "is_active")
    private Boolean isActive ;

    public CategoryEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CategoryEntity getParent() { return parent; }
    public void setParent(CategoryEntity parent) { this.parent = parent; }

    public List<CategoryEntity> getChildren() { return children; }
    public void setChildren(List<CategoryEntity> children) { this.children = children; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public Boolean isActive() { return isActive; }
    public void setActive(Boolean isActive) { this.isActive = isActive; }
}