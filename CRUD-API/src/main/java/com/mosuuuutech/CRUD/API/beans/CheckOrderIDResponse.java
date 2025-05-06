package com.mosuuuutech.CRUD.API.beans;

public class CheckOrderIDResponse {

    public final static String PARAM_ORDER_ID = "order id";
    public final static String PARAM_AMOUNT = "amount";
    public final static String PARAM_DESCRIPTION = "description";
    public final static String PARAM_STATUS = "status";

    protected Double amount;
    protected String description;
    protected String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CheckOrderIDResponse->{" +
                "amount: " + amount +
                ", description: '" + description + '\'' +
                ", status: '" + status + '\'' +
                '}';
    }
}