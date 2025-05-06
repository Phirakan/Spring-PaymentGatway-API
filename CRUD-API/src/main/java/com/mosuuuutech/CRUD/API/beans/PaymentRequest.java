package com.mosuuuutech.CRUD.API.beans;

public class PaymentRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private Double amount;
    private String description;
    private String address;
    private String message;
    private MerchantFeeType feeType;
    private String orderId;
    private MerchantPaymentType paymentType;
    private MerchantAgreement agreement;

    // Getters and setters
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public MerchantFeeType getFeeType() {
        return feeType;
    }

    public void setFeeType(MerchantFeeType feeType) {
        this.feeType = feeType;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public MerchantPaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(MerchantPaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public MerchantAgreement getAgreement() {
        return agreement;
    }

    public void setAgreement(MerchantAgreement agreement) {
        this.agreement = agreement;
    }
}