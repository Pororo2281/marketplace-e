package order_service.order.Response;

public class UpdatePaymentStatusResponse {
    private Long mainOrderId;
    private String yookassaPaymentId;
    private String paymentStatus;
    private String internalToken;

    public UpdatePaymentStatusResponse(Long orderId, String yookassaPaymentId, String paymentStatus) {
        this.mainOrderId = orderId;
        this.yookassaPaymentId = yookassaPaymentId;
        this.paymentStatus = paymentStatus;
    }

    public UpdatePaymentStatusResponse() {
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
