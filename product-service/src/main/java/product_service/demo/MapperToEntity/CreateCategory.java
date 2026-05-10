package product_service.demo.MapperToEntity;

import product_service.demo.Entity.CategoryEntity;
import product_service.demo.Exception.NotFoundById;
import product_service.demo.Repository.CategoryRepo;
import product_service.demo.Request.CreateCategoryRequest;

public class CreateCategory {

    private final CategoryRepo categoryRepo;

    public CreateCategory(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public  CategoryEntity toEntity(CreateCategoryRequest request){
        CategoryEntity category = new CategoryEntity();
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setActive(request.getIsActive());
        category.setDescription(request.getDescription());
        category.setSortOrder(request.getSortOrder());
        if (request.getParentId() != null) {
            CategoryEntity parent = categoryRepo.findById(request.getParentId())
                    .orElseThrow(() -> new NotFoundById("Parent category not found with id: " + request.getParentId()));
            category.setParent(parent);
        }
        return category;
    }
}
