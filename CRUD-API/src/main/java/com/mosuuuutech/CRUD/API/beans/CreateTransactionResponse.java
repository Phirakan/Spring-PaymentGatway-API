package com.mosuuuutech.CRUD.API.beans;

public class CreateTransactionResponse {

    public final static String PARAM_STATUS = "status";
    public final static String PARAM_TRANSACTION_ID = "transaction_ID";
    public final static String PARAM_IMAGE_QRPROM = "image_qrprom";
    public final static String PARAM_LINK_PAYMENT = "link_payment";

    protected String status;
    protected String transactionId;
    protected String imageQrprom;
    protected String linkPayment;

    public CreateTransactionResponse(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getImageQrprom() {
        return imageQrprom;
    }

    public void setImageQrprom(String imageQrprom) {
        this.imageQrprom = imageQrprom;
    }

    public String getLinkPayment() {
        return linkPayment;
    }

    public void setLinkPayment(String linkPayment) {
        this.linkPayment = linkPayment;
    }

    @Override
    public String toString() {
        return "CreateTransactionResponse -> {" +
                "status: '" + status + '\'' +
                ", transactionId: '" + transactionId + '\'' +
                ", imageQrprom: '" + imageQrprom + '\'' +
                ", linkPayment: '" + linkPayment + '\'' +
                '}';
    }
}

