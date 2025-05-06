package com.mosuuuutech.CRUD.API.beans;

public class CreateTransactionIDResponse extends CreateTransactionResponse {

    public final static String PARAM_MSKEY = "mskey";

    private String mskey;

    public CreateTransactionIDResponse(String status) {
        super(status);
    }

    public String getMskey() {
        return mskey;
    }

    public void setMskey(String mskey) {
        this.mskey = mskey;
    }

    @Override
    public String toString() {
        return "CreateTransactionMskeyResponse->{" +
                "mskey: '" + mskey + '\'' +
                ", status: '" + status + '\'' +
                ", transactionId: '" + transactionId + '\'' +
                ", imageQrprom: '" + imageQrprom + '\'' +
                ", linkPayment: '" + linkPayment + '\'' +
                '}';
    }
}

