package order_service.order.Mapper;

import order_service.order.Entity.OrderItemEntity;
import order_service.order.Response.OrderItemResponse;

public class EntityToOrderItem {
    public static OrderItemResponse entityToOrderItem(OrderItemEntity entity){
        OrderItemResponse orderItemResponse = new OrderItemResponse();
        orderItemResponse.setId(entity.getId());
        orderItemResponse.setDownloadCount(entity.getDownloadCount());
        orderItemResponse.setQuantity(entity.getQuantity());
        orderItemResponse.setProductId(entity.getProductId());
        orderItemResponse.setSubtotal(entity.getSubtotal());
        orderItemResponse.setAccessGrantedAt(entity.getAccessGrantedAt());
        orderItemResponse.setDownloadUrl(entity.getDownloadUrl());
        orderItemResponse.setLicenseKey(entity.getLicenseKey());
        orderItemResponse.setMaxDownloads(entity.getMaxDownloads());
        orderItemResponse.setPriceAtPurchase(entity.getPriceAtPurchase());
        orderItemResponse.setProductImageUrl(entity.getProductImageUrl());
        orderItemResponse.setProductTitle(entity.getProductTitle());
        orderItemResponse.setProductType(entity.getProductType());
        orderItemResponse.setDownloadedAt(entity.getDownloadedAt());
        return orderItemResponse;
    }
}
