package order_service.order.Mapper;

import order_service.order.Entity.OrderEntity;
import order_service.order.Response.OrderDetailResponse;

public class EntityToOrderDetail {
    public static OrderDetailResponse entityToOrder(OrderEntity entity) {
        OrderDetailResponse orderDetailResponse = new OrderDetailResponse();
        orderDetailResponse.setOrderNumber(entity.getOrderNumber());
        orderDetailResponse.setBuyerEmail(entity.getBuyerEmail());
        orderDetailResponse.setId(entity.getId());
        orderDetailResponse.setMainOrderId(entity.getMainOrder().getId());
        orderDetailResponse.setCancelledAt(entity.getCancelledAt());
        orderDetailResponse.setItems(entity.getItems().stream().map(EntityToOrderItem::entityToOrderItem)
                .toList());
        orderDetailResponse.setSubtotal(entity.getSubtotal());
        orderDetailResponse.setCancelReason(entity.getCancelReason());
        orderDetailResponse.setUserId(entity.getUserId());
        orderDetailResponse.setCompletedAt(entity.getCompletedAt());
        orderDetailResponse.setCreatedAt(entity.getCreatedAt());
        orderDetailResponse.setPaidAt(entity.getPaidAt());
        orderDetailResponse.setPaymentIntentId(entity.getPaymentIntentId());
        orderDetailResponse.setPaymentStatus(entity.getPaymentStatus());
        orderDetailResponse.setRefundedAt(entity.getRefundedAt());
        orderDetailResponse.setRefundReason(entity.getRefundReason());
        orderDetailResponse.setSellerId(entity.getSellerId());
        orderDetailResponse.setStatus(entity.getStatus());
        orderDetailResponse.setTax(entity.getTax());
        orderDetailResponse.setTotal(entity.getTotal());
        orderDetailResponse.setUpdatedAt(entity.getUpdatedAt());
        return orderDetailResponse;
    }
}
