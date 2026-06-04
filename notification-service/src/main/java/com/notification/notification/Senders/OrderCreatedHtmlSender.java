package com.notification.notification.Senders;

import com.notification.notification.Event.OrderItemEvent;
import com.notification.notification.Event.OrderEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedHtmlSender implements HtmlSender<OrderEvent>{

    private final JavaMailSender mailSender;
    private final EmailLoader emailLoader;

    public OrderCreatedHtmlSender(JavaMailSender mailSender, EmailLoader emailLoader) {
        this.mailSender = mailSender;
        this.emailLoader = emailLoader;
    }

    @Override
    public void send(String to, OrderEvent response) {

        try {
            StringBuilder itemsHtml = new StringBuilder();
            for (OrderItemEvent item : response.getOrderItems()) {
                itemsHtml.append("""
                <tr>
                    <td>
                        <span class="item-name">%s</span>
                        <span class="item-qty">%d шт.</span>
                    </td>
                    <td>%s ₽</td>
                </tr>
            """.formatted(
                        item.getProductTitle(),
                        item.getQuantity(),
                        item.getSubtotal()
                ));
            }

            String htmlBody = emailLoader.loadTemplate("order-created.html");

            String mainHtml = htmlBody.replace("${orderNumber}",   response.getOrderNumber())
            .replace("${createdAt}",     response.getCreatedAt().toString())
            .replace("${totalPrice}",    response.getTotalPrice().toString())
            .replace("<!-- ITEMS_ROWS -->", itemsHtml.toString());

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(response.getEmail());
            helper.setSubject("Заказ №" + response.getOrderNumber() + " ожидает оплаты");
            helper.setText(mainHtml, true);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

}
