package product_service.demo.MapperToEntity;

import product_service.demo.Entity.ProductImageEntity;
import product_service.demo.Request.ProductImageRequest;

public class CreateImages {
    public static ProductImageEntity imageToEntity(ProductImageRequest request) {
        ProductImageEntity entity = new ProductImageEntity();
        entity.setUrl(request.getImageUrl());
        entity.setSortOrder(request.getSortOrder());
        return entity;
    }
}
