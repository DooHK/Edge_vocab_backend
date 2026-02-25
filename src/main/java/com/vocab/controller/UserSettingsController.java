package com.vocab.controller;

import com.vocab.entity.User;
import com.vocab.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserSettingsController {

    private final UserRepository userRepository;

    public UserSettingsController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/settings")
    public ResponseEntity<?> getSettings(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(Map.of(
                "autoAdd", user.isAutoAdd(),
                "plan", user.getPlan().name(),
                "isPremium", user.isPremium()
        ));
    }

    @PatchMapping("/settings")
    public ResponseEntity<?> updateSettings(@AuthenticationPrincipal User user,
                                            @RequestBody Map<String, Object> body) {
        if (body.containsKey("autoAdd")) {
            user.setAutoAdd((Boolean) body.get("autoAdd"));
        }
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "설정 저장 완료"));
    }
}
