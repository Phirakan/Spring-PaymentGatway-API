package com.mosuuuutech.CRUD.API.webhook;

import com.mosuuuutech.CRUD.API.beans.MoneyspaceWebhookDto;
import com.mosuuuutech.CRUD.API.entity.Payment;
import com.mosuuuutech.CRUD.API.entity.status;
import com.mosuuuutech.CRUD.API.http.MoneyspaceWebhookCallBack;
import com.mosuuuutech.CRUD.API.repository.PaymentRepository;
import com.mosuuuutech.CRUD.API.repository.StatusRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Optional;

@Component
public class MoneyspaceWebhookHandler implements MoneyspaceWebhookCallBack {

    private static final Logger logger = LoggerFactory.getLogger(MoneyspaceWebhookHandler.class);

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Override
    public void handle(MoneyspaceWebhookDto moneyspaceWebhookDto) throws Exception {
        logger.info("Received webhook callback: {}", moneyspaceWebhookDto);

        // Process the payment update
        if (moneyspaceWebhookDto.getTransactionId() != null) {
            Optional<Payment> paymentOpt = paymentRepository.findByTransactionid(moneyspaceWebhookDto.getTransactionId());

            if (paymentOpt.isPresent()) {
                Payment payment = paymentOpt.get();

                // Update payment details if needed
                if (moneyspaceWebhookDto.getAmount() != null) {
                    payment.setAmount(moneyspaceWebhookDto.getAmount().intValue());
                }

                // Update payment status based on the webhook type
                String statusName = "PENDING";
                if (moneyspaceWebhookDto.getType() != null) {
                    switch (moneyspaceWebhookDto.getType().toLowerCase()) {
                        case "success":
                            statusName = "SUCCESS";
                            break;
                        case "fail":
                            statusName = "FAILED";
                            break;
                        case "cancel":
                            statusName = "CANCELLED";
                            break;
                    }
                }

                // Get or create the status
                Optional<status> statusOpt = statusRepository.findByStatusname(statusName);
                status newStatus;
                if (statusOpt.isPresent()) {
                    newStatus = statusOpt.get();
                } else {
                    status status = new status();
                    status.setStatusname(statusName);
                    status.setCreateat(Timestamp.from(Instant.now()));
                    status.setUpdateat(Timestamp.from(Instant.now()));
                    newStatus = statusRepository.save(status);
                }

                payment.setStatus(newStatus.getStatusid());
                payment.setUpdateat(Timestamp.from(Instant.now()));

                // Save the updated payment
                paymentRepository.save(payment);

                logger.info("Payment updated: {}", payment);
            } else {
                logger.warn("Payment not found for transaction ID: {}", moneyspaceWebhookDto.getTransactionId());
            }
        } else {
            logger.warn("No transaction ID in webhook data");
        }
    }
}