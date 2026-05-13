package order_service.order.MapperToEntity;

import order_service.order.Entity.CartItemEntity;
import order_service.order.Entity.OrderEntity;
import order_service.order.Entity.OrderItemEntity;

public class OrderItemEntityMapper {
    public static OrderItemEntity toEntity(OrderEntity orderEntity, CartItemEntity cartItem){
        OrderItemEntity orderItem = new OrderItemEntity();
        orderItem.setOrder(orderEntity);
        orderItem.setProductId(cartItem.getProductId());
        orderItem.setProductTitle(cartItem.getProductTitle());
        orderItem.setProductType(cartItem.getCategoryName());
        orderItem.setProductImageUrl(cartItem.getProductImageUrl());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPriceAtPurchase(cartItem.getProductPrice());
        return orderItem;
    }
}
