package product_service.demo.Mapper;

import product_service.demo.Entity.ProductEntity;
import product_service.demo.Enum.ProductEventType;
import product_service.demo.Event.ProductEvent;

public class ProductEventMapper {

    public static ProductEvent createProductEvent(ProductEntity productEntity, ProductEventType type){
        ProductEvent productEvent = new ProductEvent();
        productEvent.setId(productEntity.getId().toString());
        productEvent.setTitle(productEntity.getTitle());
        productEvent.setDescription(productEntity.getDescription());
        productEvent.setPrice(productEntity.getPrice());
        productEvent.setCategory(productEntity.getCategory().getName());
        productEvent.setSeller(productEntity.getSellerId().toString());
        productEvent.setType(type);
        return productEvent;
    }

}
