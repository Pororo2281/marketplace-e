package order_service.order.Mapper;

import order_service.order.Entity.OrderEntity;
import order_service.order.Entity.OrderItemEntity;
import order_service.order.Response.OrderResponse;

public class EntityToOrder {
    public static OrderResponse entityToOrder(OrderEntity entity){
        OrderResponse response = new OrderResponse();
        response.setOrderNumber(entity.getOrderNumber());
        response.setId(entity.getId());
        response.setCancelledAt(entity.getCancelledAt());
        response.setCompletedAt(entity.getCompletedAt());
        response.setCreatedAt(entity.getCreatedAt());
        response.setPaidAt(entity.getPaidAt());
        response.setPaymentStatus(entity.getPaymentStatus());
        response.setStatus(entity.getStatus());
        response.setTax(entity.getTax());
        response.setTotal(entity.getTotal());
        response.setSubtotal(entity.getSubtotal());
        response.setSellerId(entity.getSellerId());
        response.setItemsCount(response.getItemsCount());
        return response;
    }
}