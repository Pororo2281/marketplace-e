package order_service.order.Mapper;

import order_service.order.Entity.CartItemEntity;
import order_service.order.Response.CartItemResponse;
import order_service.order.Response.CartResponse;

import java.math.BigDecimal;
import java.util.List;

public class EntityToCart {
    public static CartResponse entityToCart(List<CartItemResponse> cartItemResponses,Long userId){
        var cart = new CartResponse();
        cart.setItems(cartItemResponses);
        cart.setUserId(userId);
        cart.setTotalItems(cartItemResponses.size());
        BigDecimal subTotal = cartItemResponses.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        cart.setSubtotal(subTotal);
        cart.setTotal(subTotal);
        cart.setTax(null);
        return cart;
    }
}
