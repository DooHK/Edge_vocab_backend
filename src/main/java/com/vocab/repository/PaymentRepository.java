package com.vocab.repository;

import com.vocab.entity.Payment;
import com.vocab.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderId(String orderId);
    Optional<Payment> findTopByUserOrderByCreatedAtDesc(User user);
}
