package com.mosuuuutech.CRUD.API.beans;

public enum MerchantFeeType {

    INCLUDE("include"),
    EXCLUSE("exclude");

    private final String label;

    MerchantFeeType(String label) {
        this.label = label;
    }

    public String value() {
        return label;
    }
}
