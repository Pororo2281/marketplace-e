package product_service.demo.Mapper;

import product_service.demo.Entity.CategoryEntity;
import product_service.demo.Response.CategoryResponse;
import product_service.demo.Response.ParentCategoryResponse;

public class CategoryMapper {

    public static CategoryResponse entityToCategory(CategoryEntity entity){
        var categoryResponse = new CategoryResponse();
        categoryResponse.setId(entity.getId());
        categoryResponse.setName(entity.getName());
        categoryResponse.setSlug(entity.getSlug());
        categoryResponse.setDescription(entity.getDescription());
        categoryResponse.setSortOrder(entity.getSortOrder());
        categoryResponse.setActive(entity.isActive());
        categoryResponse.setChildrenCount(entity.getChildren().stream().count());
        if(entity.getParent()!=null){
            var parentResponse = new ParentCategoryResponse();
            parentResponse.setId(entity.getParent().getId());
            parentResponse.setName(entity.getParent().getName());
            parentResponse.setSlug(entity.getParent().getSlug());
            categoryResponse.setParent(parentResponse);
        }
        return categoryResponse;
    }
}
