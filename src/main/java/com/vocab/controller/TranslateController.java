package com.vocab.controller;

import com.vocab.dto.TranslateRequest;
import com.vocab.dto.TranslateResponse;
import com.vocab.entity.User;
import com.vocab.service.TranslateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/translate")
public class TranslateController {

    private final TranslateService translateService;

    public TranslateController(TranslateService translateService) {
        this.translateService = translateService;
    }

    @PostMapping
    public ResponseEntity<?> translate(@AuthenticationPrincipal User user,
                                       @Valid @RequestBody TranslateRequest request) {
        try {
            TranslateResponse response = translateService.translate(
                    user, request.getText(), request.getSource(), request.getTarget());
            return ResponseEntity.ok(response);
        } catch (TranslateService.RateLimitExceededException e) {
            return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/usage")
    public ResponseEntity<?> getUsage(@AuthenticationPrincipal User user) {
        var usage = translateService.getOrCreateUsage(user);
        return ResponseEntity.ok(Map.of(
                "requestCount", usage.getRequestCount(),
                "charCount", usage.getCharCount(),
                "monthYear", usage.getMonthYear(),
                "isPremium", user.isPremium(),
                "limit", user.isPremium() ? -1 : 500
        ));
    }
}
