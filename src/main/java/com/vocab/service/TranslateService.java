package com.vocab.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vocab.dto.TranslateResponse;
import com.vocab.entity.ApiUsage;
import com.vocab.entity.User;
import com.vocab.repository.ApiUsageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class TranslateService {

    private static final int FREE_MONTHLY_LIMIT = 500;

    private final ApiUsageRepository apiUsageRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${google.translate.api-key:}")
    private String apiKey;

    public TranslateService(ApiUsageRepository apiUsageRepository) {
        this.apiUsageRepository = apiUsageRepository;
    }

    public TranslateResponse translate(User user, String text, String source, String target) {
        if (!user.isPremium()) {
            ApiUsage usage = getOrCreateUsage(user);
            if (usage.getRequestCount() >= FREE_MONTHLY_LIMIT) {
                throw new RateLimitExceededException("월 무료 번역 한도(" + FREE_MONTHLY_LIMIT + "회)를 초과했습니다.");
            }
        }

        String url = String.format(
            "https://translation.googleapis.com/language/translate/v2?key=%s&q=%s&source=%s&target=%s",
            apiKey,
            java.net.URLEncoder.encode(text, java.nio.charset.StandardCharsets.UTF_8),
            source, target
        );

        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = objectMapper.readTree(response);
            JsonNode translations = root.path("data").path("translations");

            if (translations.isArray() && translations.size() > 0) {
                String translated = translations.get(0).path("translatedText").asText();
                String detected = translations.get(0).path("detectedSourceLanguage").asText(source);

                ApiUsage usage = getOrCreateUsage(user);
                usage.addUsage(text.length());
                apiUsageRepository.save(usage);

                return new TranslateResponse(text, translated, detected);
            }

            throw new RuntimeException("번역 결과가 없습니다.");
        } catch (RateLimitExceededException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("번역 실패: " + e.getMessage());
        }
    }

    public ApiUsage getOrCreateUsage(User user) {
        String monthYear = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM"));
        return apiUsageRepository.findByUserAndMonthYear(user, monthYear)
                .orElseGet(() -> apiUsageRepository.save(new ApiUsage(user, monthYear)));
    }

    public static class RateLimitExceededException extends RuntimeException {
        public RateLimitExceededException(String message) {
            super(message);
        }
    }
}
