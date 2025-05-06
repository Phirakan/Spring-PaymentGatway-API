package com.mosuuuutech.CRUD.API.beans;

public enum MerchantAgreement {

    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    private final int val;

    MerchantAgreement(int val) {
        this.val = val;
    }

    public int value() {
        return val;
    }
}

