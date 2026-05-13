package order_service.order.Mapper;

import order_service.order.Entity.OrderEntity;
import order_service.order.Entity.OrderItemEntity;
import order_service.order.Entity.PurchasedProductEntity;
import order_service.order.Response.PurchasedProductResponse;

import java.time.Instant;

public class EntityToPurchased {
    public static PurchasedProductResponse entityToPurchased(PurchasedProductEntity entity){
        var response = new PurchasedProductResponse();
        response.setPurchasedAt(entity.getPurchasedAt());
        response.setId(entity.getId());
        response.setAccessCount(entity.getAccessCount());
        response.setDownloadUrl(entity.getDownloadUrl());
        response.setLicenseKey(entity.getLicenseKey());
        response.setOrderId(entity.getOrderId());
        response.setLastAccessedAt(entity.getLastAccessedAt());
        response.setProductId(entity.getProductId());
        response.setProductImageUrl(entity.getProductImageUrl());
        response.setProductTitle(entity.getProductTitle());
        response.setProductType(entity.getProductType());
        return  response;
    }

    public static PurchasedProductResponse entityToPurchased(OrderItemEntity entity){
        var response = new PurchasedProductResponse();
        response.setId(entity.getId());
        response.setDownloadUrl(entity.getDownloadUrl());
        response.setLicenseKey(entity.getLicenseKey());
        response.setOrderNumber(entity.getOrder().getOrderNumber());
        response.setOrderId(entity.getOrder().getId());
        response.setPurchasedAt(Instant.now());
        response.setAccessCount(entity.getDownloadCount());
        response.setLastAccessedAt(Instant.now());
        response.setProductId(entity.getProductId());
        response.setProductImageUrl(entity.getProductImageUrl());
        response.setProductTitle(entity.getProductTitle());
        response.setProductType(entity.getProductType());
        return  response;
    }

    public static PurchasedProductEntity toEntity(OrderItemEntity item, OrderEntity order){
        PurchasedProductEntity purchased = new PurchasedProductEntity();
        purchased.setUserId(order.getUserId());
        purchased.setDownloadUrl(item.getDownloadUrl());
        purchased.setLicenseKey(item.getLicenseKey());
        purchased.setOrderId(item.getOrder().getId());
        purchased.setOrderItemId(item.getId());
        purchased.setAccessCount(item.getDownloadCount());
        purchased.setProductId(item.getProductId());
        purchased.setProductImageUrl(item.getProductImageUrl());
        purchased.setProductTitle(item.getProductTitle());
        purchased.setProductType(item.getProductType());
        return purchased;
    }
}
