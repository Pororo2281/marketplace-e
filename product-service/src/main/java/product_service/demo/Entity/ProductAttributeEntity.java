package product_service.demo.Entity;
import jakarta.persistence.*;

@Entity
@Table(name = "product_attributes")
public class ProductAttributeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String value;

    public ProductAttributeEntity() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public ProductEntity getProduct() { return product; }
    public void setProduct(ProductEntity product) { this.product = product; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}
