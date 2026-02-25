package com.vocab.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PaymentConfirmRequest {

    @NotBlank
    private String paymentKey;

    @NotBlank
    private String orderId;

    @NotNull
    private Integer amount;

    public String getPaymentKey() { return paymentKey; }
    public String getOrderId() { return orderId; }
    public Integer getAmount() { return amount; }

    public void setPaymentKey(String paymentKey) { this.paymentKey = paymentKey; }
    public void setOrderId(String orderId) { this.orderId = orderId; }
    public void setAmount(Integer amount) { this.amount = amount; }
}
