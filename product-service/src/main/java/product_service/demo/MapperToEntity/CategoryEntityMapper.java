package product_service.demo.MapperToEntity;

import product_service.demo.Entity.CategoryEntity;
import product_service.demo.Request.CreateCategoryRequest;

public class CategoryEntityMapper {

    public static CategoryEntity toEntity(CreateCategoryRequest request){
        CategoryEntity category = new CategoryEntity();
        category.setName(request.getName());
        category.setSlug(request.getSlug());
        category.setActive(request.getIsActive());
        category.setDescription(request.getDescription());
        category.setSortOrder(request.getSortOrder());
        return category;
    }
}
