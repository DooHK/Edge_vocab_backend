package com.vocab.controller;

import com.vocab.dto.QuizResultRequest;
import com.vocab.dto.QuizStatsResponse;
import com.vocab.dto.VocabDto;
import com.vocab.entity.User;
import com.vocab.service.QuizService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/result")
    public ResponseEntity<?> saveResult(@AuthenticationPrincipal User user,
                                        @Valid @RequestBody QuizResultRequest request) {
        try {
            quizService.saveResult(user, request);
            return ResponseEntity.ok(Map.of("message", "결과 저장 완료"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<QuizStatsResponse> getStats(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(quizService.getStats(user));
    }

    @GetMapping("/wrong")
    public ResponseEntity<List<VocabDto>> getWrongWords(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(quizService.getWrongWords(user));
    }
}
