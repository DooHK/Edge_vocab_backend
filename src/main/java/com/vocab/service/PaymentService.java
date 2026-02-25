package com.vocab.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vocab.entity.Payment;
import com.vocab.entity.Payment.PaymentPlan;
import com.vocab.entity.Payment.PaymentStatus;
import com.vocab.entity.User;
import com.vocab.repository.PaymentRepository;
import com.vocab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {

    private static final int MONTHLY_PRICE = 2900;
    private static final int ANNUAL_PRICE = 19900;

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${toss.secret-key:}")
    private String tossSecretKey;

    public PaymentService(PaymentRepository paymentRepository, UserRepository userRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
    }

    public Map<String, Object> createPaymentRequest(User user, String planStr) {
        PaymentPlan plan = PaymentPlan.valueOf(planStr.toUpperCase());
        int amount = plan == PaymentPlan.MONTHLY ? MONTHLY_PRICE : ANNUAL_PRICE;
        String orderId = "VOCAB_" + UUID.randomUUID().toString().replace("-", "").substring(0, 16);

        Payment payment = new Payment(user, orderId, amount, plan);
        paymentRepository.save(payment);

        return Map.of(
            "orderId", orderId,
            "amount", amount,
            "orderName", plan == PaymentPlan.MONTHLY ? "영어단어장 프리미엄 (월간)" : "영어단어장 프리미엄 (연간)"
        );
    }

    public void confirmPayment(User user, String paymentKey, String orderId, int amount) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("결제 정보를 찾을 수 없습니다."));

        if (payment.getAmount() != amount) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new IllegalArgumentException("결제 금액이 일치하지 않습니다.");
        }

        String auth = Base64.getEncoder().encodeToString((tossSecretKey + ":").getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + auth);

        Map<String, Object> body = Map.of(
            "paymentKey", paymentKey,
            "orderId", orderId,
            "amount", amount
        );

        try {
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(
                "https://api.tosspayments.com/v1/payments/confirm",
                HttpMethod.POST, entity, String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                payment.setPaymentKey(paymentKey);
                payment.setStatus(PaymentStatus.DONE);

                LocalDateTime expiresAt = payment.getPlan() == PaymentPlan.MONTHLY
                        ? LocalDateTime.now().plusMonths(1)
                        : LocalDateTime.now().plusYears(1);
                payment.setExpiresAt(expiresAt);
                paymentRepository.save(payment);

                user.setPlan(User.Plan.PREMIUM);
                user.setPremiumExpiresAt(expiresAt);
                userRepository.save(user);
            } else {
                payment.setStatus(PaymentStatus.FAILED);
                paymentRepository.save(payment);
                throw new RuntimeException("토스 결제 승인 실패");
            }
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            payment.setStatus(PaymentStatus.FAILED);
            paymentRepository.save(payment);
            throw new RuntimeException("결제 승인 중 오류: " + e.getMessage());
        }
    }

    public Map<String, Object> getStatus(User user) {
        return Map.of(
            "plan", user.getPlan().name(),
            "isPremium", user.isPremium(),
            "premiumExpiresAt", user.getPremiumExpiresAt() != null ? user.getPremiumExpiresAt().toString() : ""
        );
    }
}
