package payment_service.payment_service.Mapper;

import payment_service.payment_service.Entity.PaymentEntity;
import payment_service.payment_service.Response.PaymentResponse;
import ru.loolzaaa.youkassa.model.Payment;

import java.math.BigDecimal;

public class PaymentMapper {
    public static PaymentResponse paymentResponse(Payment payment){
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentId(payment.getId());
        paymentResponse.setAmount(new BigDecimal(payment.getAmount().getValue()));
        paymentResponse.setStatus(payment.getStatus().toUpperCase());
        paymentResponse.setConfirmationUrl(payment.getConfirmation().getConfirmationUrl());
        return  paymentResponse;
    }

    public static PaymentResponse paymentResponse(PaymentEntity entity){
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPaymentId(entity.getId().toString());
        paymentResponse.setAmount(entity.getAmount());
        paymentResponse.setStatus(entity.getStatus().name().toUpperCase());
        paymentResponse.setConfirmationUrl(entity.getConfirmationUrl());
        return  paymentResponse;
    }
}
