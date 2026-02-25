package com.vocab.dto;

import jakarta.validation.constraints.NotBlank;

public class PaymentRequest {

    @NotBlank
    private String plan;

    public String getPlan() { return plan; }
    public void setPlan(String plan) { this.plan = plan; }
}
