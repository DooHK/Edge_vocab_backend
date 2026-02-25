package com.vocab.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    public enum Plan { FREE, PREMIUM }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "google_id", unique = true, nullable = false)
    private String googleId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "plan", nullable = false)
    private Plan plan = Plan.FREE;

    @Column(name = "auto_add")
    private boolean autoAdd = false;

    @Column(name = "premium_expires_at")
    private LocalDateTime premiumExpiresAt;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public User() {}

    public User(String googleId, String email, String name) {
        this.googleId = googleId;
        this.email = email;
        this.name = name;
    }

    public Long getId() { return id; }
    public String getGoogleId() { return googleId; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public Plan getPlan() { return plan; }
    public boolean isAutoAdd() { return autoAdd; }
    public LocalDateTime getPremiumExpiresAt() { return premiumExpiresAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPlan(Plan plan) { this.plan = plan; }
    public void setAutoAdd(boolean autoAdd) { this.autoAdd = autoAdd; }
    public void setPremiumExpiresAt(LocalDateTime premiumExpiresAt) { this.premiumExpiresAt = premiumExpiresAt; }

    public boolean isPremium() {
        return plan == Plan.PREMIUM && premiumExpiresAt != null && premiumExpiresAt.isAfter(LocalDateTime.now());
    }
}
