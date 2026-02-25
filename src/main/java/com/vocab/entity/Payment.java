package com.vocab.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    public enum PaymentPlan { MONTHLY, ANNUAL }
    public enum PaymentStatus { PENDING, DONE, CANCELED, FAILED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "order_id", unique = true, nullable = false)
    private String orderId;

    @Column(name = "payment_key")
    private String paymentKey;

    @Column(nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan", nullable = false)
    private PaymentPlan plan;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    public Payment() {}

    public Payment(User user, String orderId, int amount, PaymentPlan plan) {
        this.user = user;
        this.orderId = orderId;
        this.amount = amount;
        this.plan = plan;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getOrderId() { return orderId; }
    public String getPaymentKey() { return paymentKey; }
    public int getAmount() { return amount; }
    public PaymentPlan getPlan() { return plan; }
    public PaymentStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }

    public void setPaymentKey(String paymentKey) { this.paymentKey = paymentKey; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
}
