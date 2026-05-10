package product_service.demo.MapperToEntity;

import product_service.demo.Entity.ProductAttributeEntity;
import product_service.demo.Request.ProductAttributeRequest;
import product_service.demo.Request.UpdateAttributeRequest;

public class CreateAttribute {
    public static ProductAttributeEntity attributeToEntity(ProductAttributeRequest request) {
        ProductAttributeEntity entity = new ProductAttributeEntity();
        entity.setName(request.getName());
        entity.setValue(request.getValue());
        return entity;
    }

    public static ProductAttributeEntity attributeToEntity(UpdateAttributeRequest request) {
        ProductAttributeEntity entity = new ProductAttributeEntity();
        entity.setName(request.getName());
        entity.setValue(request.getValue());
        return entity;
    }
}
