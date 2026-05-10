package org.example.userservice.Entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private CategoryEntity parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CategoryEntity> subCategories = new ArrayList<>();

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductEntity> products = new ArrayList<>();

    public CategoryEntity() {
    }

    public CategoryEntity(String name, String description, CategoryEntity parent) {
        this.name = name;
        this.description = description;
        this.parent = parent;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CategoryEntity getParent() {
        return parent;
    }

    public void setParent(CategoryEntity parent) {
        this.parent = parent;
    }

    public List<CategoryEntity> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<CategoryEntity> subCategories) {
        this.subCategories = subCategories;
    }

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }
}