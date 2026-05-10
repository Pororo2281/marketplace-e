package order_service.order.Response;
import java.math.BigDecimal;
import java.time.Instant;

public class SellerStatsResponse {

    private Long totalOrders;           // Всего заказов
    private Long pendingOrders;         // Ожидают оплаты
    private Long paidOrders;            // Оплачены
    private Long completedOrders;       // Завершены
    private Long cancelledOrders;       // Отменены
    private Long refundedOrders;        // Возвращены

    private BigDecimal totalRevenue;    // Общая выручка (все PAID заказы)
    private BigDecimal pendingRevenue;  // Ожидающая выручка (PENDING заказы)
    private BigDecimal refundedAmount;  // Возвращенные деньги

    private Long totalProductsSold;     // Всего продано товаров (штук)
    private Long uniqueCustomers;       // Уникальных покупателей

    private BigDecimal averageOrderValue; // Средний чек

    private Instant firstOrderDate;     // Дата первого заказа
    private Instant lastOrderDate;      // Дата последнего заказа

    // Статистика за последние 30 дней
    private Stats last30Days;

    public SellerStatsResponse() {}

    // Getters and Setters
    public Long getTotalOrders() { return totalOrders; }
    public void setTotalOrders(Long totalOrders) { this.totalOrders = totalOrders; }

    public Long getPendingOrders() { return pendingOrders; }
    public void setPendingOrders(Long pendingOrders) { this.pendingOrders = pendingOrders; }

    public Long getPaidOrders() { return paidOrders; }
    public void setPaidOrders(Long paidOrders) { this.paidOrders = paidOrders; }

    public Long getCompletedOrders() { return completedOrders; }
    public void setCompletedOrders(Long completedOrders) { this.completedOrders = completedOrders; }

    public Long getCancelledOrders() { return cancelledOrders; }
    public void setCancelledOrders(Long cancelledOrders) { this.cancelledOrders = cancelledOrders; }

    public Long getRefundedOrders() { return refundedOrders; }
    public void setRefundedOrders(Long refundedOrders) { this.refundedOrders = refundedOrders; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public BigDecimal getPendingRevenue() { return pendingRevenue; }
    public void setPendingRevenue(BigDecimal pendingRevenue) { this.pendingRevenue = pendingRevenue; }

    public BigDecimal getRefundedAmount() { return refundedAmount; }
    public void setRefundedAmount(BigDecimal refundedAmount) { this.refundedAmount = refundedAmount; }

    public Long getTotalProductsSold() { return totalProductsSold; }
    public void setTotalProductsSold(Long totalProductsSold) { this.totalProductsSold = totalProductsSold; }

    public Long getUniqueCustomers() { return uniqueCustomers; }
    public void setUniqueCustomers(Long uniqueCustomers) { this.uniqueCustomers = uniqueCustomers; }

    public BigDecimal getAverageOrderValue() { return averageOrderValue; }
    public void setAverageOrderValue(BigDecimal averageOrderValue) { this.averageOrderValue = averageOrderValue; }

    public Instant getFirstOrderDate() { return firstOrderDate; }
    public void setFirstOrderDate(Instant firstOrderDate) { this.firstOrderDate = firstOrderDate; }

    public Instant getLastOrderDate() { return lastOrderDate; }
    public void setLastOrderDate(Instant lastOrderDate) { this.lastOrderDate = lastOrderDate; }

    public Stats getLast30Days() { return last30Days; }
    public void setLast30Days(Stats last30Days) { this.last30Days = last30Days; }


    public static class Stats {
        private Long orders;
        private BigDecimal revenue;
        private Long productsSold;
        private Long newCustomers;

        public Stats() {}

        public Stats(Long orders, BigDecimal revenue, Long productsSold, Long newCustomers) {
            this.orders = orders;
            this.revenue = revenue;
            this.productsSold = productsSold;
            this.newCustomers = newCustomers;
        }

        public Long getOrders() { return orders; }
        public void setOrders(Long orders) { this.orders = orders; }

        public BigDecimal getRevenue() { return revenue; }
        public void setRevenue(BigDecimal revenue) { this.revenue = revenue; }

        public Long getProductsSold() { return productsSold; }
        public void setProductsSold(Long productsSold) { this.productsSold = productsSold; }

        public Long getNewCustomers() { return newCustomers; }
        public void setNewCustomers(Long newCustomers) { this.newCustomers = newCustomers; }
    }
}
