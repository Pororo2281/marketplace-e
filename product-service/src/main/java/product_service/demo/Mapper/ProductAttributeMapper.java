package product_service.demo.Mapper;

import product_service.demo.Entity.ProductAttributeEntity;
import product_service.demo.Response.ProductAttributeResponse;

public class ProductAttributeMapper {
    public static ProductAttributeResponse entityToAttribute(ProductAttributeEntity attribute) {
        ProductAttributeResponse response = new ProductAttributeResponse();
        response.setId(attribute.getId());
        response.setName(attribute.getName());
        response.setValue(attribute.getValue());
        return response;
    }

}
