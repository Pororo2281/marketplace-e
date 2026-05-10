package payment_service.payment_service.Response;

public class PaymentSucceededEvent {
    private Long mainOrderId;
    private String yookassaPaymentId;
    private String paymentStatus;
    private String internalToken;

    public PaymentSucceededEvent(Long orderId, String yookassaPaymentId, String paymentStatus, String internalToken) {
        this.internalToken = internalToken;
        this.mainOrderId = orderId;
        this.yookassaPaymentId = yookassaPaymentId;
        this.paymentStatus = paymentStatus;
    }

    public String getInternalToken() {
        return internalToken;
    }

    public void setInternalToken(String internalToken) {
        this.internalToken = internalToken;
    }

    public Long getMainOrderId() {
        return mainOrderId;
    }

    public void setMainOrderId(Long mainOrderId) {
        this.mainOrderId = mainOrderId;
    }

    public String getYookassaPaymentId() {
        return yookassaPaymentId;
    }

    public void setYookassaPaymentId(String yookassaPaymentId) {
        this.yookassaPaymentId = yookassaPaymentId;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

}
