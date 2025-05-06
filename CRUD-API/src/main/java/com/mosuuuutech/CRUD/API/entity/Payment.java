package com.mosuuuutech.CRUD.API.entity;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table (name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentid;

    private String firstname;

    private String lastname;
    private String email;
    private int amount;
    private String message;
    private String orderid;
    private String transactionid;
    private String qrlink;
    private int status;
    private Timestamp createat;
    private Timestamp updateat;

    public Payment() {

    }

    public Payment(String firstname, String lastname, String email, int amount, String message, String orderid, String transactionid, String qrlink, int status, Timestamp createat, Timestamp updateat) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.amount = amount;
        this.message = message;
        this.orderid = orderid;
        this.transactionid = transactionid;
        this.qrlink = qrlink;
        this.status = status;
        this.createat = createat;
        this.updateat = updateat;
    }

    public int getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(int paymentid) {
        this.paymentid = paymentid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getQrlink() {
        return qrlink;
    }

    public void setQrlink(String qrlink) {
        this.qrlink = qrlink;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Timestamp getCreateat() {
        return createat;
    }

    public void setCreateat(Timestamp createat) {
        this.createat = createat;
    }

    public Timestamp getUpdateat() {
        return updateat;
    }

    public void setUpdateat(Timestamp updateat) {
        this.updateat = updateat;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentid=" + paymentid +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", amount=" + amount +
                ", message='" + message + '\'' +
                ", orderid='" + orderid + '\'' +
                ", transactionid='" + transactionid + '\'' +
                ", qrlink='" + qrlink + '\'' +
                ", status=" + status +
                ", createat=" + createat +
                ", updateat=" + updateat +
                '}';
    }
}
