package com.notification.notification.RabbitMq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.notification.notification.Respone.OrderItemResponse;
import com.notification.notification.Respone.OrderResponse;
import com.notification.notification.Senders.CustomMailSender;
import org.springframework.stereotype.Service;

@Service
public class RabbitListener {

    private final CustomMailSender orderMailSender;
    private final ObjectMapper objectMapper;

    public RabbitListener(CustomMailSender orderMailSender, ObjectMapper objectMapper) {
        this.orderMailSender = orderMailSender;
        this.objectMapper = objectMapper;
    }

    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = {"${rabbitmq.queue.order-created}"})
    public void receiveOrderCreated(String json){

        try {
            OrderResponse response = objectMapper.readValue(json, OrderResponse.class);

            StringBuilder itemsBuilder = new StringBuilder();
            for(OrderItemResponse item : response.getOrderItems()){
                itemsBuilder.append(item.getProductTitle() + " - " + item.getQuantity() + " шт. - " + item.getSubtotal() + " Руб.\n");
            }

            String message = """
                    Здравствуйте!

                    Вы успешно оформили заказ, но он ещё не оплачен.

                    Детали заказа:
                    Номер заказа: %s
                    Дата: %s
                    Сумма: %s Руб.

                    Товары:
                    %s

                    Обратите внимание: заказ будет отменён через 24 часа, если он не будет оплачен.

                    Если у вас есть вопросы — support@yourmarketplace.com
                    """.formatted( response.getOrderNumber(), response.getCreatedAt(), response.getTotalPrice(),itemsBuilder );
            orderMailSender.send(response.getEmail(),
                    "Заказ №" + response.getOrderNumber() + " ожидает оплаты"
                    , message);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    @org.springframework.amqp.rabbit.annotation.RabbitListener(queues = {"${rabbitmq.queue.order-paid}"})
    public void receiveOrderPaid(String json){
        try {
            OrderResponse response = objectMapper.readValue(json, OrderResponse.class);

            StringBuilder itemsBuilder = new StringBuilder();
            for(OrderItemResponse item : response.getOrderItems()){
                itemsBuilder.append(item.getProductTitle() + " - " + item.getQuantity() + " шт. - " + item.getSubtotal() + " Руб.\n");
            }
        String message = """
        Здравствуйте!

        Ваш заказ успешно оплачен ✅

        Детали заказа:
        Номер заказа: %s
        Дата оплаты: %s
        Сумма: %s Руб.

        Товары:
        %s

        Мы уже начали обработку вашего заказа.

        Спасибо, что выбрали наш сервис ❤️

        Если у вас есть вопросы — support@yourmarketplace.com
        """.formatted(
                response.getOrderNumber(),
                response.getCreatedAt(),
                response.getTotalPrice(),
                itemsBuilder
        );
            orderMailSender.send(response.getEmail(),
                    "Заказ №" + response.getOrderNumber() + "успешно оплачен"
                    , message);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
