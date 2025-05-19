package com.mosuuuutech.CRUD.API.controllers;

import com.mosuuuutech.CRUD.API.beans.*;
import com.mosuuuutech.CRUD.API.entity.Payment;
import com.mosuuuutech.CRUD.API.entity.status;
import com.mosuuuutech.CRUD.API.repository.PaymentRepository;
import com.mosuuuutech.CRUD.API.repository.StatusRepository;
import com.mosuuuutech.CRUD.API.services.MoneyspaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private MoneyspaceService moneyspaceService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StatusRepository statusRepository;

    @PostMapping("/create-qr")
    public ResponseEntity<Map<String, Object>> createQRPayment(@RequestBody PaymentRequest request) throws Exception {
        request.setPaymentType(MerchantPaymentType.QRNONE);

        Payment payment = moneyspaceService.createTransaction(
                request.getFirstname(),
                request.getLastname(),
                request.getEmail(),
                request.getPhone(),
                request.getAmount(),
                request.getDescription(),
                request.getAddress(),
                request.getMessage(),
                request.getFeeType(),
                request.getOrderId(),
                MerchantPaymentType.QRNONE,
                request.getAgreement()
        );

        Map<String, Object> response = new HashMap<>();
        response.put("transactionId", payment.getTransactionid());
        response.put("qrCodeUrl", payment.getQrlink());
        response.put("amount", payment.getAmount());
        response.put("status", "PENDING");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/status/{transactionId}")
    public ResponseEntity<Map<String, Object>> checkPaymentStatus(@PathVariable String transactionId) throws Exception {

        Payment payment = moneyspaceService.checkPaymentStatus(transactionId);
        try {
            
            status currentStatus = statusRepository.findById(payment.getStatus()).orElse(null);
            String statusName = currentStatus != null ? currentStatus.getStatusname() : "UNKNOWN";

            Map<String, Object> response = new HashMap<>();
            response.put("transactionId", payment.getTransactionid());
            response.put("amount", payment.getAmount());
            response.put("description", payment.getMessage() != null ? payment.getMessage() : "ชำระค่าสินค้า");
            response.put("status", statusName);
//            response.put("qrCodeUrl", payment.getQrlink());
            response.put("qrlink", payment.getQrlink());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/status/order/{orderId}")
    public ResponseEntity<Payment> checkPaymentStatusByOrder(@PathVariable String orderId) throws Exception {
        Payment payment = moneyspaceService.checkPaymentStatusByOrder(orderId);
        return ResponseEntity.ok(payment);
    }

    @PostMapping("/cancel/{transactionId}")
    public ResponseEntity<Map<String, Object>> cancelPayment(@PathVariable String transactionId) throws Exception {
        try {

            Payment payment = moneyspaceService.cancelPayment(transactionId);

            Map<String, Object> response = new HashMap<>();
            response.put("transactionId", transactionId);
            response.put("message", "รหัสธุรกรรมที่ " + transactionId + " ได้ถูกยกเลิกแล้ว");
            response.put("status", "CANCELLED");
            response.put("timestamp", Timestamp.from(Instant.now()).toString());
           //response.put("payment", payment);

            return ResponseEntity.ok(response);
        } catch (Exception e) {

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("transactionId", transactionId);
            errorResponse.put("error", e.getMessage());
            errorResponse.put("message", "ไม่สามารถยกเลิกรหัสธุรกรรมที่ " + transactionId + " ได้");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    private String mapStatusNameFromResponse(String responseStatus) {
        if (responseStatus == null) {
            return "PENDING";
        }

        String status = responseStatus.toLowerCase();

        if (status.contains("success") || status.contains("pay success")) {
            return "SUCCESS";
        } else if (status.contains("fail")) {
            return "FAILED";
        } else if (status.contains("cancel")) {
            return "CANCELLED";
        } else {
            return "PENDING";
        }
    }
}