package com.mosuuuutech.CRUD.API.services;

import com.mosuuuutech.CRUD.API.MoneyspaceAPIBuilderImpl;
import com.mosuuuutech.CRUD.API.beans.*;
import com.mosuuuutech.CRUD.API.entity.Payment;
import com.mosuuuutech.CRUD.API.entity.status;
import com.mosuuuutech.CRUD.API.http.MoneyspaceWebhookCallBack;
import com.mosuuuutech.CRUD.API.repository.PaymentRepository;
import com.mosuuuutech.CRUD.API.repository.StatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;

@Service
public class MoneyspaceService {

    private static final Logger logger = LoggerFactory.getLogger(MoneyspaceService.class);
    private final MoneyspaceAPIBuilderImpl apiBuilder;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StatusRepository statusRepository;

    public MoneyspaceService(String secretId, String secretKey, String successUrl, String failUrl, String cancelUrl) throws Exception {
        this.apiBuilder = new MoneyspaceAPIBuilderImpl(secretId, secretKey, successUrl, failUrl, cancelUrl);
    }

    public Payment createTransaction(String firstname, String lastname, String email, String phone,
                                     Double amount, String description, String address, String message,
                                     MerchantFeeType feeType, String orderId, MerchantPaymentType paymentType,
                                     MerchantAgreement agreement) throws Exception {


        CreateTransactionRequest request = new CreateTransactionRequest(
                firstname, lastname, email, phone, amount, description,
                address, message, feeType, orderId, paymentType, agreement
        );


        CreateTransactionResponse response = apiBuilder.createTransaction(request, 60);


        Payment payment = new Payment();
        payment.setFirstname(firstname);
        payment.setLastname(lastname);
        payment.setEmail(email);
        payment.setAmount(amount.intValue());
        payment.setMessage(message);
        payment.setOrderid(orderId);
        payment.setTransactionid(response.getTransactionId());
        payment.setQrlink(response.getImageQrprom());

        status pendingStatus = getOrCreateStatus("PENDING");

        payment.setStatus(pendingStatus.getStatusid());
        payment.setCreateat(Timestamp.from(Instant.now()));
        payment.setUpdateat(Timestamp.from(Instant.now()));

        return paymentRepository.save(payment);
    }

    public Payment createInstallmentTransaction(String firstname, String lastname, String email, String phone,
                                                Double amount, String description, String address, String message,
                                                MerchantFeeType feeType, String orderId, MerchantPaymentType paymentType,
                                                MerchantAgreement agreement, BankType bankType, Integer startTerm,
                                                Integer endTerm) throws Exception {

        // Create request object
        CreateTransactionIDRequest request = new CreateTransactionIDRequest(
                firstname, lastname, email, phone, amount, description,
                address, message, feeType, orderId, paymentType, agreement,
                bankType, startTerm, endTerm
        );

        // Call the API
        CreateTransactionResponse response = apiBuilder.createTransactionInstallment(request, 60);

        // Save to database
        Payment payment = new Payment();
        payment.setFirstname(firstname);
        payment.setLastname(lastname);
        payment.setEmail(email);
        payment.setAmount(amount.intValue());
        payment.setMessage(message);
        payment.setOrderid(orderId);
        payment.setTransactionid(response.getTransactionId());
        payment.setQrlink(response.getLinkPayment());

        // Set initial status (pending)
        status pendingStatus = getOrCreateStatus("PENDING");

        payment.setStatus(pendingStatus.getStatusid());
        payment.setCreateat(Timestamp.from(Instant.now()));
        payment.setUpdateat(Timestamp.from(Instant.now()));

        return paymentRepository.save(payment);
    }

    public Payment checkPaymentStatus(String transactionId) throws Exception {
        TransactionStatusResponse response = apiBuilder.checkPaymentStatusByTransaction(transactionId, 60);

        Payment payment = paymentRepository.findByTransactionid(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found with transaction ID: " + transactionId));

        // Update status based on response
        if (response.getStatusPayment() != null) {
            String statusPayment = response.getStatusPayment();
            System.out.println("สถานะจาก API: " + statusPayment);

            String statusName;
            if (statusPayment.contains("Success") || statusPayment.equals("Pay Success")) {
                statusName = "SUCCESS";
            } else if (statusPayment.contains("Fail") || statusPayment.equals("Fail")) {
                statusName = "FAILED";
            } else if (statusPayment.contains("Cancel") || statusPayment.equals("Cancel")) {
                statusName = "CANCELLED";
            } else {
                statusName = "PENDING";
            }

            status newStatus = getOrCreateStatus(statusName);

            payment.setStatus(newStatus.getStatusid());
            payment.setUpdateat(Timestamp.from(Instant.now()));

            return paymentRepository.save(payment);
        }

        return payment;
    }

    public Payment checkPaymentStatusByOrder(String orderId) throws Exception {
        OrderStatusResponse response = apiBuilder.checkPaymentStatusByOrder(orderId, 60);

        Payment payment = paymentRepository.findByOrderid(orderId)
                .orElseThrow(() -> new RuntimeException("Payment not found with order ID: " + orderId));

        // Update status based on response
        if (response.getStatusPayment() != null) {
            String statusName = mapStatusNameFromResponse(response.getStatusPayment());
            status newStatus = getOrCreateStatus(statusName);

            payment.setStatus(newStatus.getStatusid());
            payment.setUpdateat(Timestamp.from(Instant.now()));

            return paymentRepository.save(payment);
        }

        return payment;
    }

    public Payment cancelPayment(String transactionId) throws Exception {
        CancelPaymentResponse response = apiBuilder.cancelPayment(transactionId, 60);

        Payment payment = paymentRepository.findByTransactionid(transactionId)
                .orElseThrow(() -> new RuntimeException("Payment not found with transaction ID: " + transactionId));

        // Update status to CANCELLED
        status cancelledStatus = getOrCreateStatus("CANCELLED");

        payment.setStatus(cancelledStatus.getStatusid());
        payment.setUpdateat(Timestamp.from(Instant.now()));

        return paymentRepository.save(payment);
    }

    // Helper method to get or create a status
    private status getOrCreateStatus(String statusName) {
        return statusRepository.findByStatusname(statusName)
                .orElseGet(() -> {
                    status newStatus = new status();
                    newStatus.setStatusname(statusName);
                    newStatus.setCreateat(Timestamp.from(Instant.now()));
                    newStatus.setUpdateat(Timestamp.from(Instant.now()));
                    return statusRepository.save(newStatus);
                });
    }

    // Helper method to map MoneySpace status to our own status names
    private String mapStatusNameFromResponse(String responseStatus) {
        if (responseStatus == null) {
            return "EMPTY RESPONESE";
        }
        String status = responseStatus.toLowerCase();

        if (status.contains("success") || status.contains("pay success")) {
            return "SUCCESS";
        } else if (status.contains("fail")) {
            return "FAILED";
        } else if (status.contains("cancel")) {
            return "CANCELLED";
        } else {
            return "UNKNOW";
        }
    }

    // Method to start webhook server
    public void startWebhookServer(String hostname, int port, MoneyspaceWebhookCallBack callback) throws Exception {
        apiBuilder.startWebhook(hostname, port, callback);
    }

    // Method to stop webhook server
    public void stopWebhookServer() {
        apiBuilder.stopWebhook();
    }


}