package com.mosuuuutech.CRUD.API.beans;

public enum PaymentTransactionStatus {

    SUCCESS("success"),
    FAIL("fail"),
    CANCEL("cancel");

    private final String label;

    PaymentTransactionStatus(String label) {
        this.label = label;
    }

    public String value() {
        return label;
    }

}
