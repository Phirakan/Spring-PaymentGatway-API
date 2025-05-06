package com.mosuuuutech.CRUD.API.beans;

public class CreateTransactionIDRequest extends CreateTransactionRequest {

    private BankType bankType;
    private Integer startTerm;
    private Integer endTerm;

    /**
     * Constructor-method
     */
    public CreateTransactionIDRequest() {
    }

    /**
     * Constructor-method
     */
    public CreateTransactionIDRequest(String firstname
            , String lastname
            , String email
            , String phone
            , Double amount
            , String description
            , String address
            , String message
            , MerchantFeeType feeType
            , String orderId
            , MerchantPaymentType paymentType
            , MerchantAgreement agreement
            , BankType bankType
            , Integer startTerm
            , Integer endTerm) {
        super(firstname
                , lastname
                , email
                , phone
                , amount
                , description
                , address
                , message
                , feeType
                , orderId
                , paymentType
                , agreement);
        this.bankType = bankType;
        this.startTerm = startTerm;
        this.endTerm = endTerm;
    }

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
