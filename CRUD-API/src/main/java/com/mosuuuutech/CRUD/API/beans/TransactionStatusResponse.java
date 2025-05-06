package com.mosuuuutech.CRUD.API.beans;

public class TransactionStatusResponse {

    public final static String PARAM_TRANSACTION_ID = "Transaction ID ";
    public final static String PARAM_AMOUNT = "Amount ";
    public final static String PARAM_DESCRIPTION = "Description ";
    public final static String PARAM_STATUS_PAYMENT = "Status Payment ";

    private String transactionId;
    private Double amount;
    private String description;
    private String statusPayment;

    public TransactionStatusResponse(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
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
        return "TransactionStatusResponse->{" +
                "amount: " + amount +
                ", description: '" + description + '\'' +
                ", statusPayment: '" + statusPayment + '\'' +
                ", transactionId: '" + transactionId + '\'' +
                '}';
    }
}

