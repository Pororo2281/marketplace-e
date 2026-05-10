package payment_service.payment_service.Service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import payment_service.payment_service.Client.OrderClient;
import payment_service.payment_service.Entity.PaymentEntity;
import payment_service.payment_service.Entity.RefundEntity;
import payment_service.payment_service.Enum.PaymentStatus;
import payment_service.payment_service.Enum.RefundStatus;
import payment_service.payment_service.Mapper.PaymentMapper;
import payment_service.payment_service.RabbitMq.RabbitProducer;
import payment_service.payment_service.Repository.PaymentRepo;
import payment_service.payment_service.Repository.RefundRepo;
import payment_service.payment_service.Request.CreatePaymentRequest;
import payment_service.payment_service.Response.PaymentResponse;
import payment_service.payment_service.Response.PaymentSucceededEvent;
import ru.loolzaaa.youkassa.client.ApiClient;
import ru.loolzaaa.youkassa.client.ApiClientBuilder;
import ru.loolzaaa.youkassa.model.Payment;
import ru.loolzaaa.youkassa.model.Refund;
import ru.loolzaaa.youkassa.pojo.Amount;
import ru.loolzaaa.youkassa.pojo.Confirmation;
import ru.loolzaaa.youkassa.pojo.Currency;
import ru.loolzaaa.youkassa.processors.PaymentProcessor;
import ru.loolzaaa.youkassa.processors.RefundProcessor;

import java.math.BigDecimal;

@Service
public class YookassaService {

    private final PaymentRepo repo;
    private final RefundRepo refundRepo;
    private final RabbitProducer rabbitProducer;
    private final OrderClient orderClient;

    @Value("${yookassa.key}")
    private String secretKey;

    @Value("${yookassa.shopId}")
    private String shopId;

    @Value("${internal.secret-token}")
    private String internalToken;

    public YookassaService(OrderClient orderClient, RabbitProducer rabbitProducer, RefundRepo refundRepo, PaymentRepo repo) {
        this.orderClient = orderClient;
        this.rabbitProducer = rabbitProducer;
        this.refundRepo = refundRepo;
        this.repo = repo;
    }

    public PaymentResponse createPayment(@Valid CreatePaymentRequest request, Long userId) {
        ApiClient client = ApiClientBuilder.newBuilder()
                .configureBasicAuth(shopId, secretKey)
                .build();

        PaymentProcessor paymentProcessor = new PaymentProcessor(client);

        Payment payment = paymentProcessor.create(Payment.builder()
                .amount(Amount.builder().value(String.valueOf(request.getAmount())).currency(Currency.RUB).build())
                .description("New payment")
                .confirmation(Confirmation.builder()
                        .type(Confirmation.Type.REDIRECT)
                        .returnUrl("https://www.example.com/return_url")
                        .build())
                        .capture(true)
                .build(), null);

        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setAmount(request.getAmount());
        paymentEntity.setPaymentMethod(paymentEntity.getPaymentMethod());
        paymentEntity.setCurrency(payment.getAmount().getCurrency());
        paymentEntity.setDescription(payment.getDescription());
        paymentEntity.setYookassaPaymentId(payment.getId());
        paymentEntity.setConfirmationUrl(payment.getConfirmation().getConfirmationUrl());
        paymentEntity.setOrderId(request.getMainOrderId());
        paymentEntity.setStatus(PaymentStatus.valueOf(payment.getStatus().toUpperCase()));
        paymentEntity.setUserId(userId);
        repo.save(paymentEntity);
        return PaymentMapper.paymentResponse(payment);
    }

    public PaymentResponse getStatus(Long paymentId,Long userId) {
        return repo.findById(paymentId)
                .filter(payment -> payment.getUserId().equals(userId))
                .map(PaymentMapper::paymentResponse)
                .orElseThrow(()->new RuntimeException("not found by id: " + paymentId));
    }

    public PaymentResponse refundPayment(Long paymentId, Long userId) {
        ApiClient client = ApiClientBuilder.newBuilder()
                .configureBasicAuth(shopId, secretKey)
                .build();

        PaymentEntity paymentEntity = repo.findById(paymentId)
                .filter(payment -> payment.getUserId().equals(userId))
                .orElseThrow(()->new RuntimeException("not found by id: " + paymentId));

        if (paymentEntity.getStatus() == PaymentStatus.REFUNDED){
            throw new RuntimeException("payment already refunded");
        }

        RefundProcessor refundProcessor = new RefundProcessor(client);

        Refund refund = refundProcessor.create(Refund.builder()
                .paymentId(paymentEntity.getYookassaPaymentId())
                .amount(Amount.builder().value(paymentEntity.getAmount().toString()).currency(Currency.RUB).build())
                .build(), null);

        RefundEntity refundEntity = new RefundEntity();
        refundEntity.setPayment(paymentEntity);
        refundEntity.setYookassaRefundId(refund.getId());
        refundEntity.setAmount(BigDecimal.valueOf(Double.valueOf(refund.getAmount().getValue())));
        refundEntity.setCurrency(refund.getAmount().getCurrency());
        refundEntity.setStatus(RefundStatus.valueOf(refund.getStatus().toUpperCase()));
        paymentEntity.setStatus(PaymentStatus.REFUNDED);
        repo.save(paymentEntity);
        refundRepo.save(refundEntity);

        PaymentEntity payment = refundEntity.getPayment();

        rabbitProducer.sendPaymentEvent(new PaymentSucceededEvent(payment.getOrderId(),
                payment.getYookassaPaymentId(),
                payment.getStatus().name(),
                internalToken
                ));

        return PaymentMapper.paymentResponse(paymentEntity);
    }
}
