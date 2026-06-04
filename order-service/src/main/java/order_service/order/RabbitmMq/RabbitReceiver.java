package order_service.order.RabbitmMq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import order_service.order.Client.ProductClient;
import order_service.order.Entity.OrderEntity;
import order_service.order.Entity.OrderItemEntity;
import order_service.order.Enum.OrderEventType;
import order_service.order.Enum.OrderStatus;
import order_service.order.Enum.PaymentStatus;
import order_service.order.Exception.NotFoundById;
import order_service.order.Exception.ProductsNotEnough;
import order_service.order.Mapper.EntityToOrderItem;
import order_service.order.Repository.MainOrderRepo;
import order_service.order.Repository.OrderItemRepo;
import order_service.order.Repository.OrderRepo;
import order_service.order.Response.OrderEvent;
import order_service.order.Response.UpdatePaymentStatusResponse;
import order_service.order.Service.LibraryService;
import order_service.order.Service.OrderService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;



@Service
public class RabbitReceiver {

    private final ObjectMapper objectMapper;
    private final OrderRepo orderRepo;
    private final LibraryService libraryService;
    private final OrderItemRepo orderItemRepo;
    private final ProductClient productClient;
    private final OrderService orderService;
    private final MainOrderRepo mainOrderRepo;
    private final RabbitProducer rabbitProducer;

    @Value("${internal.secret-token}")
    private String internalToken;

    public RabbitReceiver(ObjectMapper objectMapper, OrderRepo orderRepo, LibraryService libraryService, OrderItemRepo orderItemRepo, ProductClient productClient, OrderService orderService, MainOrderRepo mainOrderRepo, RabbitProducer rabbitProducer) {
        this.objectMapper = objectMapper;
        this.orderRepo = orderRepo;
        this.libraryService = libraryService;
        this.orderItemRepo = orderItemRepo;
        this.productClient = productClient;
        this.orderService = orderService;
        this.mainOrderRepo = mainOrderRepo;
        this.rabbitProducer = rabbitProducer;
    }

    @RabbitListener(queues = {"${rabbitmq.queue.order}"})
    @Retryable(
            maxRetries = 3,
            delay = 30000
    )
    @Transactional
    public void receivePayment(String json){
        System.out.println("Received payment message: {}" + json);

        try {
            UpdatePaymentStatusResponse response = objectMapper.readValue(json, UpdatePaymentStatusResponse.class);

            if (!response.getInternalToken().equals(internalToken)){
                throw new SecurityException("Invalid internal token");
            }

            var mainOrder = mainOrderRepo.findById(response.getMainOrderId())
                    .orElseThrow(() -> new NotFoundById("Order not found with id: " + response.getMainOrderId()));

            if (mainOrder.getPaymentStatus() == PaymentStatus.REFUNDED) {
                return;
            }

            if (response.getPaymentStatus().equalsIgnoreCase("SUCCEEDED")) {
                mainOrder.setPaymentStatus(PaymentStatus.COMPLETED);
                mainOrder.setPaymentIntentId(response.getYookassaPaymentId());
                mainOrder.setPaidAt(Instant.now());
                mainOrder.getOrders().stream().forEach(ord->ord.setPaymentStatus(PaymentStatus.COMPLETED));
                mainOrder.getOrders().stream().forEach(ord->ord.setStatus(OrderStatus.PAID));
                mainOrder.getOrders().stream().forEach(ord->ord.setPaidAt(Instant.now()));
                mainOrder.getOrders().stream().forEach(ord->ord.setPaymentIntentId(response.getYookassaPaymentId()));
                for(OrderEntity order : mainOrder.getOrders()){
                    handlePaymentSuccess(order);
                    System.out.println("Calling product service for order {}" + order.getId());
                    try {
                        System.out.println(orderService.buildAvailabilityRequestFromOrder(order));
                        productClient.reverseStock(orderService.buildAvailabilityRequestFromOrder(order), internalToken);
                    } catch (FeignException.Conflict e) {
                        throw new ProductsNotEnough("Not enough stock");
                    }
                    orderRepo.save(order);

                }
                rabbitProducer.sendOrderPaid(new OrderEvent(mainOrder.getBuyerEmail(),
                        mainOrder.getOrderNumber(),
                        mainOrder.getTotalAmount(),
                        mainOrder.getCreatedAt(),
                        mainOrder.getPaidAt(),
                        mainOrder.getOrders().stream().flatMap(order->order.getItems().stream())
                                .map(EntityToOrderItem::entityToOrderItem).toList()
                        , OrderEventType.ORDER_PAID
                        ));

            } else if (response.getPaymentStatus().equalsIgnoreCase("CANCELED") || response.getPaymentStatus().equalsIgnoreCase("FAILED")) {
                mainOrder.setPaymentStatus(PaymentStatus.FAILED);
                mainOrder.setPaymentIntentId(response.getYookassaPaymentId());
                mainOrder.setPaidAt(Instant.now());

                mainOrder.getOrders().stream().forEach(ord->ord.setPaymentStatus(PaymentStatus.FAILED));
                mainOrder.getOrders().stream().forEach(ord->ord.setStatus(OrderStatus.CANCELLED));
                mainOrder.getOrders().stream().forEach(ord->ord.setPaymentIntentId(response.getYookassaPaymentId()));

            } else if (response.getPaymentStatus().equalsIgnoreCase("REFUNDED")) {
                mainOrder.setPaymentStatus(PaymentStatus.REFUNDED);
                mainOrder.setPaymentIntentId(response.getYookassaPaymentId());
                mainOrder.setPaidAt(Instant.now());

                mainOrder.getOrders().stream().forEach(ord->ord.setPaymentStatus(PaymentStatus.REFUNDED));
                mainOrder.getOrders().stream().forEach(ord->ord.setStatus(OrderStatus.REFUNDED));
                mainOrder.getOrders().stream().forEach(ord->ord.setPaymentIntentId(response.getYookassaPaymentId()));
                for (OrderEntity order : mainOrder.getOrders()){
                    try {
                        productClient.returnStock(orderService.buildAvailabilityRequestFromOrder(order), internalToken);
                    } catch (FeignException.Conflict e) {
                        throw new ProductsNotEnough("Not enough stock");
                    }
                    orderRepo.save(order);
                }
            }

            orderRepo.saveAll(mainOrder.getOrders());
            mainOrderRepo.save(mainOrder);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    public void handlePaymentSuccess(OrderEntity order) {
        libraryService.addToLibrary(order);
        for(OrderItemEntity item : order.getItems()){
            item.setAccessGrantedAt(Instant.now());
            orderItemRepo.save(item);
        }
    }

}
