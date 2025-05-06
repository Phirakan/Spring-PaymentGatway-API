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

    @PostMapping("/create-installment")
    public ResponseEntity<Payment> createInstallmentPayment(@RequestBody InstallmentPaymentRequest request) throws Exception {
        Payment payment = moneyspaceService.createInstallmentTransaction(
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
                request.getPaymentType(),
                request.getAgreement(),
                request.getBankType(),
                request.getStartTerm(),
                request.getEndTerm()
        );

        return ResponseEntity.ok(payment);
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
            response.put("qrCodeUrl", payment.getQrlink());
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
    public ResponseEntity<Payment> cancelPayment(@PathVariable String transactionId) throws Exception {
        Payment payment = moneyspaceService.cancelPayment(transactionId);
        return ResponseEntity.ok(payment);
    }


    @GetMapping("/check-status-direct/{transactionId}")
    public ResponseEntity<Map<String, Object>> checkStatusDirect(@PathVariable String transactionId) {
        try {
            // HTTP response has body content: [{"Amount ":"1.00","Transaction ID ":"MQR0605254082169","Description ":" ","Status Payment ":"Pay Success"}]
            Payment payment = moneyspaceService.checkPaymentStatus(transactionId);

            status currentStatus = statusRepository.findById(payment.getStatus()).orElse(null);
            String statusName = currentStatus != null ? currentStatus.getStatusname() : "UNKNOWN";

            return ResponseEntity.ok(Map.of(
                    "transactionId", payment.getTransactionid(),
                    "amount", payment.getAmount(),
                    "status", statusName,
                    "qrlink", payment.getQrlink(),
                    "paymentUpdated", true
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleMoneyspaceWebhook(@RequestBody Map<String, Object> payload) {
        try {

            System.out.println("ได้รับ Webhook จาก MoneySpace: " + payload);

            String transactionId = (String) payload.get("transactionId");
            if (transactionId == null) {
                System.err.println("ไม่พบ Transaction ID ใน webhook payload");
                return ResponseEntity.badRequest().body("Missing Transaction ID");
            }

            String paymentStatus = (String) payload.get("status");
            if (paymentStatus == null) {
                System.err.println("ไม่พบสถานะการชำระเงินใน webhook payload");
                return ResponseEntity.badRequest().body("Missing Payment Status");
            }

            Payment payment = paymentRepository.findByTransactionid(transactionId)
                    .orElse(null);

            if (payment == null) {
                System.err.println("ไม่พบรายการชำระเงินสำหรับ Transaction ID: " + transactionId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
            }

            String statusName = mapStatusNameFromResponse(paymentStatus);

            status newStatus = statusRepository.findByStatusname(statusName)
                    .orElseGet(() -> {
                        status s = new status();
                        s.setStatusname(statusName);
                        s.setCreateat(Timestamp.from(Instant.now()));
                        s.setUpdateat(Timestamp.from(Instant.now()));
                        return statusRepository.save(s);
                    });

            payment.setStatus(newStatus.getStatusid());
            payment.setUpdateat(Timestamp.from(Instant.now()));

            paymentRepository.save(payment);

            System.out.println("อัพเดตสถานะการชำระเงินสำเร็จ: " + statusName);

            return ResponseEntity.ok("Webhook processed successfully");

        } catch (Exception e) {
            System.err.println("เกิดข้อผิดพลาดในการประมวลผล webhook: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error processing webhook: " + e.getMessage());
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