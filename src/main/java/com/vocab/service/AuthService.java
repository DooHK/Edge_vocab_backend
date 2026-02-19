package com.vocab.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vocab.dto.AuthResponse;
import com.vocab.entity.User;
import com.vocab.repository.UserRepository;
import com.vocab.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final String googleClientId;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthService(UserRepository userRepository,
                       JwtTokenProvider jwtTokenProvider,
                       @Value("${google.client-id}") String googleClientId) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.googleClientId = googleClientId;
    }

    /**
     * Google ID Token을 검증하고 자체 JWT를 발급합니다.
     * Google tokeninfo API로 토큰을 검증하고 sub, email, name을 추출합니다.
     */
    public AuthResponse authenticateWithGoogle(String credential) {
        String url = "https://oauth2.googleapis.com/tokeninfo?id_token=" + credential;

        try {
            String response = restTemplate.getForObject(url, String.class);
            JsonNode node = objectMapper.readTree(response);

            // aud(audience) 검증 - 우리 앱의 Client ID와 일치해야 함
            String aud = node.path("aud").asText();
            if (!googleClientId.equals(aud)) {
                throw new IllegalArgumentException("Invalid Google token audience");
            }

            String googleId = node.path("sub").asText();
            String email    = node.path("email").asText();
            String name     = node.path("name").asText(email.split("@")[0]);

            User user = userRepository.findByGoogleId(googleId)
                    .orElseGet(() -> userRepository.save(new User(googleId, email, name)));

            // 이름/이메일 변경 반영
            if (!user.getName().equals(name) || !user.getEmail().equals(email)) {
                user.setName(name);
                user.setEmail(email);
                userRepository.save(user);
            }

            String jwt = jwtTokenProvider.generateToken(user.getId());
            return new AuthResponse(jwt, user.getEmail(), user.getName());

        } catch (Exception e) {
            throw new IllegalArgumentException("Google 인증 실패: " + e.getMessage());
        }
    }
}
