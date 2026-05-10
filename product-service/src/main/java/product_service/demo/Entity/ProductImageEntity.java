package product_service.demo.Entity;


import jakarta.persistence.*;

@Entity
@Table(name = "product_images")
public class ProductImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
    private String url;

    @Column(name = "sort_order")
    private Integer sortOrder ;

    @Column(name = "is_main")
    private Boolean isMain ;

    public ProductImageEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ProductEntity getProduct() { return product; }
    public void setProduct(ProductEntity product) { this.product = product; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public Integer getSortOrder() { return sortOrder; }
    public void setSortOrder(Integer sortOrder) { this.sortOrder = sortOrder; }

    public Boolean getIsMain() { return isMain; }
    public void setIsMain(Boolean isMain) { this.isMain = isMain; }
}
