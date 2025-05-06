package com.mosuuuutech.CRUD.API.beans;

public class CancelPaymentResponse {

    public final static String PARAM_STATUS = "status";
    public final static String PARAM_MESSAGE = "message";
    public final static String PARAM_DESCRIPTION = "description";

    protected String status;
    protected String message;
    protected String description;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CancelPaymentResponse->{" +
                "status: '" + status + '\'' +
                ", message: " + message +
                ", description: '" + description + '\'' +
                '}';
    }
}