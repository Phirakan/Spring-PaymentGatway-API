package com.mosuuuutech.CRUD.API.beans;

public enum BankType {

    KTC("KTC"),
    BAY("BAY"),
    FCY("FCY");

    public static final Integer[] KTCStartTerm = {3, 4, 5, 6, 7, 8, 9, 10};
    public static final Integer[] BAYStartTerm = {3, 4, 6, 9, 10};
    public static final Integer[] FCYStartTerm = {3, 4, 6, 9, 10};

    private final String label;

    BankType(String label) {
        this.label = label;
    }

    public String value() {
        return label;
    }
}
