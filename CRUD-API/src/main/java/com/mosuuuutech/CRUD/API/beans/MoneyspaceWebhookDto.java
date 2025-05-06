package com.mosuuuutech.CRUD.API.beans;

import java.util.Date;

/**
 * Money Space Java SDK Test class
 * @author Money space company limited
 * @version 2.0.0
 *
 */
public class MoneyspaceWebhookDto {

    /* HTTP QueryString parameter names */
    public static final String PARAM_LOCALE = "locale";
    public static final String PARAM_STORE_NAME = "store_name";
    public static final String PARAM_STORE_LOGO = "store_logo";
    public static final String PARAM_STORE_PHONE = "store_phone";
    public static final String PARAM_NAME_CUSTOMER = "name_customer";
    public static final String PARAM_DATETIME = "datetime";
    public static final String PARAM_IDPAY = "idpay";
    public static final String PARAM_NAME_MERCHANT = "name_merchant";
    public static final String PARAM_AGREEMENT = "agreement";
    public static final String PARAM_DESCRIPTION = "description";
    public static final String PARAM_AMOUNT = "amount";
    public static final String PARAM_DATE = "date";
    public static final String PARAM_TIME = "time";
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_TRANSACTION_ID = "transactionId";

    /* data attribute */
    private String locale;
    private String storeName;
    private String storeLogo;
    private String storePhone;
    private String nameCustomer;
    // Date of a date and time "dd-MM-yyyy hh:mm" or "dd/MM/yy" and "hh:mm"
    private Date datetime;
    private String idpay;
    private String nameMerchant;
    private Integer agreement;
    private String description;
    private Double amount;
    private String type;
    private String transactionId;

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreLogo() {
        return storeLogo;
    }

    public void setStoreLogo(String storeLogo) {
        this.storeLogo = storeLogo;
    }

    public String getStorePhone() {
        return storePhone;
    }

    public void setStorePhone(String storePhone) {
        this.storePhone = storePhone;
    }

    public String getNameCustomer() {
        return nameCustomer;
    }

    public void setNameCustomer(String nameCustomer) {
        this.nameCustomer = nameCustomer;
    }

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date datetime) {
        this.datetime = datetime;
    }

    public String getIdpay() {
        return idpay;
    }

    public void setIdpay(String idpay) {
        this.idpay = idpay;
    }

    public String getNameMerchant() {
        return nameMerchant;
    }

    public void setNameMerchant(String nameMerchant) {
        this.nameMerchant = nameMerchant;
    }

    public Integer getAgreement() {
        return agreement;
    }

    public void setAgreement(Integer agreement) {
        this.agreement = agreement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        return "MoneyspaceWebhookCallbackDto -> {" +
                "locale: '" + locale + '\'' +
                ", storeName: '" + storeName + '\'' +
                ", storeLogo: '" + storeLogo + '\'' +
                ", storePhone: '" + storePhone + '\'' +
                ", nameCustomer: '" + nameCustomer + '\'' +
                ", datetime=" + datetime +
                ", idpay: '" + idpay + '\'' +
                ", nameMerchant: '" + nameMerchant + '\'' +
                ", agreement=" + agreement +
                ", description: '" + description + '\'' +
                ", amount=" + amount +
                ", type: '" + type + '\'' +
                ", transactionId: '" + transactionId + '\'' +
                '}';
    }
}
