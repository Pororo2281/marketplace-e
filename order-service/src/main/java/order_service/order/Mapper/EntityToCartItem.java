package order_service.order.Mapper;

import order_service.order.Entity.CartItemEntity;
import order_service.order.Response.CartItemResponse;
import order_service.order.Response.CartResponse;

import java.math.BigDecimal;

public class EntityToCartItem {
    public static CartItemResponse entityToCartItem(CartItemEntity cartItemEntity){
        var cartItem = new CartItemResponse();
        cartItem.setId(cartItemEntity.getId());
        cartItem.setAddedAt(cartItemEntity.getCreatedAt());
        cartItem.setProductId(cartItemEntity.getProductId());
        cartItem.setQuantity(cartItemEntity.getQuantity());
        cartItem.setProductPrice(cartItemEntity.getProductPrice());
        cartItem.setProductTitle(cartItemEntity.getProductTitle());
        cartItem.setMainImageUrl(cartItemEntity.getProductImageUrl());
        cartItem.setSellerId(cartItemEntity.getSellerId());
        cartItem.setSellerName(cartItemEntity.getSellerName());
        cartItem.setProductType(cartItemEntity.getCategoryName());
        cartItem.setSubtotal(cartItemEntity.getProductPrice().multiply(BigDecimal.valueOf(cartItemEntity.getQuantity())));

        return cartItem;
    }
}
