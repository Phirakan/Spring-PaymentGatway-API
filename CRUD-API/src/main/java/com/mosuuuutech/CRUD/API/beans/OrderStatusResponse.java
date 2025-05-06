package com.mosuuuutech.CRUD.API.beans;

public class OrderStatusResponse {

    public final static String PARAM_ORDER_ID = "Order ID ";
    public final static String PARAM_AMOUNT = "Amount ";
    public final static String PARAM_DESCRIPTION = "Description ";
    public final static String PARAM_STATUS_PAYMENT = "Status Payment ";

    private String orderId;
    private Double amount;
    private String description;
    private String statusPayment;

    public OrderStatusResponse(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatusPayment() {
        return statusPayment;
    }

    public void setStatusPayment(String statusPayment) {
        this.statusPayment = statusPayment;
    }

    @Override
    public String toString() {
        return "OrderStatusResponse->{" +
                "orderId: '" + orderId + '\'' +
                ", amount: " + amount +
                ", description: '" + description + '\'' +
                ", statusPayment: '" + statusPayment + '\'' +
                '}';
    }
}
