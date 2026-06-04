package order_service.order.Service;
import jakarta.validation.Valid;
import order_service.order.Client.ProductClient;
import order_service.order.Entity.CartItemEntity;
import order_service.order.Entity.MainOrderEntity;
import order_service.order.Entity.OrderEntity;
import order_service.order.Entity.OrderItemEntity;
import order_service.order.Enum.OrderEventType;
import order_service.order.Enum.OrderStatus;
import order_service.order.Enum.PaymentStatus;
import order_service.order.Exception.*;
import order_service.order.Mapper.EntityToOrder;
import order_service.order.Mapper.EntityToOrderDetail;
import order_service.order.Mapper.EntityToOrderItem;
import order_service.order.MapperToEntity.OrderItemEntityMapper;
import order_service.order.RabbitmMq.RabbitProducer;
import order_service.order.Repository.CartItemRepo;
import order_service.order.Repository.MainOrderRepo;
import order_service.order.Repository.OrderRepo;
import order_service.order.Request.*;
import order_service.order.Response.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepo orderRepo;
    private final CartItemRepo cartItemRepo;
    private final ProductClient productClient;
    private final RabbitProducer rabbitProducer;
    private final MainOrderRepo mainOrderRepo;

    @Value("${internal.secret-token}")
    private String internalToken;

    public OrderService(OrderRepo orderRepo, CartItemRepo cartItemRepo, ProductClient productClient, RabbitProducer rabbitProducer, MainOrderRepo mainOrderRepo) {
        this.orderRepo = orderRepo;
        this.cartItemRepo = cartItemRepo;
        this.productClient = productClient;
        this.rabbitProducer = rabbitProducer;
        this.mainOrderRepo = mainOrderRepo;
    }

    @Transactional
    public List<OrderDetailResponse> createOrder(@Valid CreateOrderRequest request, Long userId) {
        List<CartItemEntity> cartItems = cartItemRepo.findAllById(request.getCartItemIds());

        if(cartItems.isEmpty()){
            throw new EmptyCartException("There are no products in the cart");
        }


        boolean allItemsBelongToUser = cartItems.stream()
                .allMatch(item -> item.getUserId().equals(userId));

        if (!allItemsBelongToUser) {
            throw new CartItemOwnerException("Some cart items don't belong to current user");
        }

        CheckAvailabilityRequest availabilityRequest = buildAvailabilityRequest(cartItems);
        CheckAvailabilityResponse availability = productClient.checkProductAvailability(availabilityRequest,internalToken);

        if (!availability.getAvailable()) {
            throw new ProudctsNotAvailable(
                    "Some products are not available: " + availability.getMessage()
            );
        }

        MainOrderEntity mainOrder = new MainOrderEntity();
        mainOrder.setUserId(userId);
        mainOrder.setBuyerEmail(request.getBuyerEmail());
        mainOrder.setPaymentStatus(PaymentStatus.PENDING);

        mainOrderRepo.save(mainOrder);


            Map<Long, List<CartItemEntity>> itemsBySeller = cartItems.stream()
                    .collect(Collectors.groupingBy(CartItemEntity::getSellerId));

            List<OrderEntity> orders = new ArrayList<>();

            for (Map.Entry<Long, List<CartItemEntity>> entry : itemsBySeller.entrySet()) {
                Long sellerId = entry.getKey();
                List<CartItemEntity> sellerItems = entry.getValue();

                OrderEntity order = createOrderForSeller(
                        userId,
                        sellerId,
                        sellerItems,
                        request.getBuyerEmail()
                );

                order.setMainOrder(mainOrder);
                orders.add(order);
            }


            orders = orderRepo.saveAll(orders);

            BigDecimal totalAmount = orders.stream()
                            .map(OrderEntity::getTotal)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

            mainOrder.setTotalAmount(totalAmount);


            cartItemRepo.deleteAllById(request.getCartItemIds());

            List<OrderItemResponse> orderItems = orders.stream()
                            .flatMap(order->order.getItems().stream())
                            .map(EntityToOrderItem::entityToOrderItem)
                            .toList();

            rabbitProducer.sendOrderCreated(new OrderEvent(mainOrder.getBuyerEmail()
                    ,mainOrder.getOrderNumber()
                    ,mainOrder.getTotalAmount()
                    ,mainOrder.getCreatedAt(),
                    mainOrder.getPaidAt()
                    ,orderItems
                    , OrderEventType.ORDER_CREATED
                    ));

            return orders.stream()
                    .map(EntityToOrderDetail::entityToOrder)
                    .collect(Collectors.toList());

    }

    private CheckAvailabilityRequest buildAvailabilityRequest(List<CartItemEntity> cartItemEntities){
        CheckAvailabilityRequest request = new CheckAvailabilityRequest();
        List<CheckAvailabilityItem> items = cartItemEntities.stream()
                .map(cartItem->{
                    CheckAvailabilityItem item = new CheckAvailabilityItem();
                    item.setQuantity(cartItem.getQuantity());
                    item.setProductId(cartItem.getProductId());
                    return item;
                })
        .collect(Collectors.toList());

        request.setItems(items);
        return request;
    }

    public CheckAvailabilityRequest buildAvailabilityRequestFromOrder(OrderEntity order) {
        CheckAvailabilityRequest request = new CheckAvailabilityRequest();

        List<CheckAvailabilityItem> items = order.getItems().stream()
                .map(orderItem -> {
                    CheckAvailabilityItem item =
                            new CheckAvailabilityItem();
                    item.setProductId(orderItem.getProductId());
                    item.setQuantity(orderItem.getQuantity());
                    return item;
                })
                .collect(Collectors.toList());

        request.setItems(items);
        return request;
    }

    private OrderEntity createOrderForSeller(Long userId, Long sellerId, List<CartItemEntity> sellerItems, String buyerEmail) {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setUserId(userId);
        orderEntity.setSellerId(sellerId);
        orderEntity.setStatus(OrderStatus.PENDING);
        orderEntity.setPaymentStatus(PaymentStatus.PENDING);
        orderEntity.setBuyerEmail(buyerEmail);

        List<OrderItemEntity> orderItems = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (CartItemEntity cartItem : sellerItems) {
            OrderItemEntity orderItem = OrderItemEntityMapper.toEntity(orderEntity,cartItem);

            BigDecimal itemSubtotal = cartItem.getProductPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            orderItem.setSubtotal(itemSubtotal);

            orderItems.add(orderItem);
            subtotal = subtotal.add(itemSubtotal);
        }

        orderEntity.setItems(orderItems);
        orderEntity.setSubtotal(subtotal);
        orderEntity.setTax(BigDecimal.ZERO);
        BigDecimal total = subtotal.add(BigDecimal.ZERO);
        orderEntity.setTotal(total);
        return orderEntity;
    }

    public OrderDetailResponse getOrderById(Long orderId, Long userId) {
        OrderEntity order = orderRepo.findById(orderId)
                .filter(or->or.getUserId().equals(userId))
                .orElseThrow(()->new NotFoundById("order with id: " + orderId + " not found"));
        return EntityToOrderDetail.entityToOrder(order);
    }

    public OrderDetailResponse getSellerOrderById(Long orderId, Long userId) {
        OrderEntity order = orderRepo.findById(orderId)
                .filter(or->or.getSellerId().equals(userId))
                .orElseThrow(()->new NotFoundById("order with id: " + orderId + " not found"));
        return EntityToOrderDetail.entityToOrder(order);
    }

    public Page<OrderResponse> getMyOrders(Long userId, int size, int page, OrderStatus status) {
        Pageable pageable = PageRequest.of(page,size);
        var orderStatus = status;
        if (orderStatus==null){
            orderStatus=OrderStatus.PENDING;
        }
        Page<OrderEntity> items = orderRepo.findByUserIdAndStatus(userId,orderStatus,pageable);
        var response = items.map(EntityToOrder::entityToOrder);
        return response;
    }

    public OrderResponse getOrderByNumber(Long userId, String orderNumber) {
        var order = orderRepo.findByUserIdAndOrderNumber(userId,orderNumber)
                .orElseThrow(()->new NotFoundByOrderNumber("order not found with orderNumber: " + orderNumber));
        return EntityToOrder.entityToOrder(order);
    }

    @Transactional
    public OrderDetailResponse cancelOrder(Long userId, Long orderId, UpdateOrderStatusRequest request) {

        var order = orderRepo.findById(orderId)
                .filter(x -> x.getUserId().equals(userId))
                .orElseThrow(() -> new NotFoundById("Order with id: " + orderId + " not found"));

        if (order.getPaymentStatus() == PaymentStatus.COMPLETED) {
            throw new OrderAlreadyPaidException("The order cannot be canceled");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOrderStateException("Only pending orders can be cancelled");
        }

        String reason = request != null ? request.getReason() : "Cancelled by user";

        order.setCancelledAt(Instant.now());
        order.setCancelReason(reason);
        order.setStatus(OrderStatus.CANCELLED);
        order.setPaymentStatus(PaymentStatus.FAILED);

        order = orderRepo.save(order);

        CheckAvailabilityRequest stockRequest = buildAvailabilityRequestFromOrder(order);

        try {
            productClient.returnStock(stockRequest,internalToken);
        } catch (Exception e) {
            throw new ProductServiceException("Failed to return stock to Product Service");
        }

        return EntityToOrderDetail.entityToOrder(order);
    }

    @Transactional
    public OrderDetailResponse requestRefund(Long id,
                                             UpdateOrderStatusRequest request,
                                             Long userId) {

        var order = orderRepo.findById(id)
                .filter(or -> or.getUserId().equals(userId))
                .orElseThrow(() ->
                        new NotFoundById("Order not found with id: " + id));

        if (order.getPaymentStatus() != PaymentStatus.COMPLETED) {
            throw new RefundNotAllowedException("Refund not allowed. Payment not completed");
        }

        if (order.getStatus() == OrderStatus.CANCELLED ||
                order.getStatus() == OrderStatus.REFUNDED ||
                order.getStatus() == OrderStatus.REFUND_REQUESTED) {
            throw new RefundNotAllowedException("Refund not allowed for this order status");
        }

        String reason = request != null ? request.getReason() : "Refund requested by user";

        order.setRefundReason(reason);
        order.setRefundedAt(Instant.now());

        order.setPaymentStatus(PaymentStatus.REFUND_PENDING);
        order.setStatus(OrderStatus.REFUND_REQUESTED);

        order = orderRepo.save(order);

        return EntityToOrderDetail.entityToOrder(order);
    }

    @Transactional
    public OrderDetailResponse approveRefund(Long orderId) {

        var order = orderRepo.findById(orderId)
                .orElseThrow(() -> new NotFoundById("Order not found"));

        if (order.getStatus() != OrderStatus.REFUND_REQUESTED) {
            throw new RefundNotAllowedException("Refund not requested");
        }

        CheckAvailabilityRequest stockRequest = buildAvailabilityRequestFromOrder(order);

        productClient.returnStock(stockRequest,internalToken);

        order.setRefundedAt(Instant.now());
        order.setPaymentStatus(PaymentStatus.REFUNDED);
        order.setStatus(OrderStatus.REFUNDED);

        order = orderRepo.save(order);

        return EntityToOrderDetail.entityToOrder(order);
    }

    public Page<OrderResponse> getSellersOrders(int size, int page, OrderStatus status, Long sellerId) {
        Pageable pageable = PageRequest.of(page,size);
        if (status==null){
           return orderRepo.findBySellerId(sellerId,pageable)
                    .map(EntityToOrder::entityToOrder);
        }
        return orderRepo.findBySellerIdAndStatus(sellerId,status,pageable)
                .map(EntityToOrder::entityToOrder);
    }

    public OrderDetailResponse updateOrderStatus(Long id, @Valid UpdateOrderStatusRequest request, Long sellerId) {
        var order = orderRepo.findById(id)
                .filter(or->or.getSellerId().equals(sellerId))
                .orElseThrow(() ->
                        new NotFoundById("Order not found with id: " + id));


        OrderStatus newStatus = request.getStatus();

        if (order.getStatus() == OrderStatus.CANCELLED ||
                order.getStatus() == OrderStatus.REFUNDED||
        order.getStatus() == OrderStatus.REFUND_REQUESTED) {
            throw new OrderUpdateNotAllowedException("Cannot update cancelled or refunded order");
        }

        if (newStatus == OrderStatus.COMPLETED) {
            order.setCompletedAt(Instant.now());
        }

        if (newStatus == OrderStatus.CANCELLED) {
            order.setCancelledAt(Instant.now());
            order.setCancelReason(request.getReason());
        }

        order = orderRepo.save(order);

        return EntityToOrderDetail.entityToOrder(order);
    }

    public SellerStatsResponse getSellerStats(Long sellerId) {
        if (orderRepo.existsBySellerId(sellerId)){
            return new SellerStatsResponse();
        };

        long completedOrders = orderRepo.countBySellerIdAndStatus(sellerId,OrderStatus.COMPLETED);

        BigDecimal totalRevenue = orderRepo.getTotalRevenue(sellerId);

        BigDecimal average= BigDecimal.ZERO;

        if (completedOrders!=0){
            average = totalRevenue.divide(
                    BigDecimal.valueOf(completedOrders),
                    RoundingMode.HALF_UP);
        }



        Instant fromDate = Instant.now().minus(30, ChronoUnit.DAYS);


        var stats = new SellerStatsResponse();
        stats.setCancelledOrders(orderRepo.countBySellerIdAndStatus(sellerId,OrderStatus.CANCELLED));
        stats.setCompletedOrders(orderRepo.countBySellerIdAndStatus(sellerId,OrderStatus.COMPLETED));
        stats.setPaidOrders(orderRepo.countBySellerIdAndStatus(sellerId,OrderStatus.PAID));
        stats.setRefundedOrders(orderRepo.countBySellerIdAndStatus(sellerId,OrderStatus.REFUNDED));
        stats.setPendingOrders(orderRepo.countBySellerIdAndStatus(sellerId,OrderStatus.PENDING));
        stats.setTotalOrders(orderRepo.countBySellerId(sellerId));

        stats.setPendingRevenue(orderRepo.getPendingRevenue(sellerId));
        stats.setRefundedAmount(orderRepo.getRefundedAmount(sellerId));

        stats.setTotalProductsSold(orderRepo.getTotalProductsSold(sellerId));
        stats.setUniqueCustomers(orderRepo.getUniqueCustomers(sellerId));

        stats.setFirstOrderDate(orderRepo.getFirstOrderDate(sellerId));
        stats.setLastOrderDate(orderRepo.getLastOrderDate(sellerId));

        stats.setAverageOrderValue(average);

        SellerStatsResponse.Stats statsInner = new SellerStatsResponse.Stats();

        statsInner.setOrders(orderRepo.countOrdersLast30Days(sellerId,fromDate));
        statsInner.setNewCustomers(orderRepo.getNewCustomersLast30Days(sellerId,fromDate));
        statsInner.setRevenue(orderRepo.getRevenueLast30Days(sellerId,fromDate));
        statsInner.setProductsSold(orderRepo.getProductsSoldLast30Days(sellerId,fromDate));

        stats.setLast30Days(statsInner);

        return stats;
    }

    public boolean hasActiveOrdersWithProduct(Long productId) {
        return orderRepo.findFirstByProductId(productId)
                .map(order -> order.getStatus() == OrderStatus.PENDING
                        || order.getStatus() == OrderStatus.PAID)
                .orElse(false);
    }

    public boolean hasUserPurchasedProduct(Long userId, Long productId) {
        return orderRepo.findFirstByProductIdAndUserId(productId,userId)
                .map(order->order.getPaymentStatus().equals(PaymentStatus.COMPLETED))
                .orElse(false);
    }
}
