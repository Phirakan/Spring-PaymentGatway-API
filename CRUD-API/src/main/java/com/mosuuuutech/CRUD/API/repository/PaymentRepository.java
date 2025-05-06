package com.mosuuuutech.CRUD.API.repository;

import com.mosuuuutech.CRUD.API.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Optional<Payment> findByTransactionid(String transactionId);
    Optional<Payment> findByOrderid(String orderId);
}