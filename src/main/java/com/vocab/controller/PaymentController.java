package com.vocab.controller;

import com.vocab.dto.PaymentConfirmRequest;
import com.vocab.dto.PaymentRequest;
import com.vocab.entity.User;
import com.vocab.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/request")
    public ResponseEntity<?> createRequest(@AuthenticationPrincipal User user,
                                           @Valid @RequestBody PaymentRequest request) {
        try {
            return ResponseEntity.ok(paymentService.createPaymentRequest(user, request.getPlan()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<?> confirm(@AuthenticationPrincipal User user,
                                     @Valid @RequestBody PaymentConfirmRequest request) {
        try {
            paymentService.confirmPayment(user, request.getPaymentKey(), request.getOrderId(), request.getAmount());
            return ResponseEntity.ok(Map.of("message", "결제 완료", "plan", "PREMIUM"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/status")
    public ResponseEntity<?> status(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(paymentService.getStatus(user));
    }
}
