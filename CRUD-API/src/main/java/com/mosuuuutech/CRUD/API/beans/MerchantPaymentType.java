package com.mosuuuutech.CRUD.API.beans;

public enum MerchantPaymentType {

    CARD("card"),
    QRNONE("qrnone");

    private final String label;

    MerchantPaymentType(String label) {
        this.label = label;
    }

    public String value() {
        return label;
    }

}
