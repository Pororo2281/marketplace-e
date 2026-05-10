package product_service.demo.Service;

import jakarta.servlet.FilterChain;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import product_service.demo.Entity.CategoryEntity;
import product_service.demo.Entity.ProductEntity;
import product_service.demo.Enum.ProductStatus;
import product_service.demo.Exception.CategoryParrent;
import product_service.demo.Exception.NotFoundById;
import product_service.demo.Exception.NotFoundBySlug;
import product_service.demo.Exception.SlugExists;
import product_service.demo.Mapper.CategoryMapper;
import product_service.demo.Mapper.ProductMapper;
import product_service.demo.Repository.CategoryRepo;
import product_service.demo.Repository.ProductRepo;
import product_service.demo.Request.CreateCategoryRequest;
import product_service.demo.Request.UpdateCategoryRequest;
import product_service.demo.Response.CategoryResponse;
import product_service.demo.Response.ProductResponse;
import product_service.demo.Specifications.ProductSpecifications;

@Service
public class CategoryService {

    private final CategoryRepo repository;
    private final ProductRepo productRepo;

    public CategoryService(CategoryRepo repository, ProductRepo productRepo) {
        this.repository = repository;
        this.productRepo = productRepo;
    }


    public Page<CategoryResponse> getAllCategories(Pageable pageable){
        return  repository.findAll(pageable)
                .map(CategoryMapper::entityToCategory);
    }

    public CategoryResponse getCategoryById(Long id) {
        var categoryEntity = repository.findById(id)
                .orElseThrow(()-> new NotFoundById("Category with id: " + id + " not found"));
        return CategoryMapper.entityToCategory(categoryEntity);
    }

    public CategoryResponse getCategoryBySlug(String slug) {
        var categoryEntity = repository.findBySlug(slug)
                .orElseThrow(()-> new NotFoundBySlug("Category with slug: " + slug + " not found"));
        return CategoryMapper.entityToCategory(categoryEntity);
    }


    public CategoryResponse createCategory(CreateCategoryRequest request) {
        var categoryEntity = toEntity(request);
        repository.save(categoryEntity);
        return CategoryMapper.entityToCategory(categoryEntity);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {

        CategoryEntity category = repository.findById(id)
                .orElseThrow(() -> new NotFoundById("Category not found with id: " + id));

        updateCategoryFields(category, request, id);

        CategoryEntity updated = repository.save(category);
        return CategoryMapper.entityToCategory(updated);
    }

    private void updateCategoryFields(CategoryEntity category, UpdateCategoryRequest request, Long categoryId) {
        if (request.getName() != null && !request.getName().trim().isEmpty()) {
            category.setName(request.getName());
        }

        if (request.getSlug() != null && !request.getSlug().trim().isEmpty()) {
            if (!request.getSlug().equals(category.getSlug()) &&
                    repository.existsBySlug(request.getSlug())) {
                throw new SlugExists("Category with slug '" + request.getSlug() + "' already exists");
            }
            category.setSlug(request.getSlug());
        }


        if (request.getDescription() != null) {
            category.setDescription(request.getDescription());
        }


        if (request.getSortOrder() != null) {
            category.setSortOrder(request.getSortOrder());
        }


        if (request.getIsActive() != null) {
            category.setActive(request.getIsActive());
        }


        if (request.getParentId() != null) {
            if (request.getParentId().equals(categoryId)) {
                throw new CategoryParrent("Category cannot be its own parent");
            }

            CategoryEntity parent = repository.findById(request.getParentId())
                    .orElseThrow(() -> new NotFoundById("Parent category not found with id: " + request.getParentId()));

            category.setParent(parent);
        }
    }



    private CategoryEntity toEntity(CreateCategoryRequest request){
        CategoryEntity category = new CategoryEntity();
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setActive(request.getIsActive());
        category.setDescription(request.getDescription());
        category.setSortOrder(request.getSortOrder());
        if (request.getParentId() != null) {
            CategoryEntity parent = repository.findById(request.getParentId())
                    .orElseThrow(() -> new NotFoundById("Parent category not found with id: " + request.getParentId()));
            category.setParent(parent);
        }
        return category;
    }

    public void deleteCategory(Long id) {
        var categoryEntity = repository.findById(id)
                .orElseThrow(()-> new NotFoundById("Category with id: " + id + " not found"));
        if (productRepo.existsByCategoryId(id)){
            throw new RuntimeException("Cannot delete category with id: " + id + " because it has associated products");
        }
        repository.delete(categoryEntity);
    }

    public CategoryResponse toggleCategoryActiveStatus(Long id) {
        var categoryEntity = repository.findById(id)
                .orElseThrow(()-> new NotFoundById("Category with id: " + id + " not found"));
        categoryEntity.setActive(!categoryEntity.isActive());
        repository.save(categoryEntity);
        return CategoryMapper.entityToCategory(categoryEntity);
    }


    public void forceDeleteProduct(Long id) {
    }
}
