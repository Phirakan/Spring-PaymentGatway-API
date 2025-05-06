package com.mosuuuutech.CRUD.API.beans;

public class InstallmentPaymentRequest extends PaymentRequest {
    private BankType bankType;
    private Integer startTerm;
    private Integer endTerm;

    public BankType getBankType() {
        return bankType;
    }

    public void setBankType(BankType bankType) {
        this.bankType = bankType;
    }

    public Integer getStartTerm() {
        return startTerm;
    }

    public void setStartTerm(Integer startTerm) {
        this.startTerm = startTerm;
    }

    public Integer getEndTerm() {
        return endTerm;
    }

    public void setEndTerm(Integer endTerm) {
        this.endTerm = endTerm;
    }
}