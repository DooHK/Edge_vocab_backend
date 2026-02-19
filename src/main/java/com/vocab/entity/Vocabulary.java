package com.vocab.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "vocabularies")
public class Vocabulary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String word;

    @Column(nullable = false)
    private String translation;

    @Column(name = "added_date")
    private String addedDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public Vocabulary() {}

    public Vocabulary(User user, String word, String translation, String addedDate) {
        this.user = user;
        this.word = word;
        this.translation = translation;
        this.addedDate = addedDate;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getWord() { return word; }
    public String getTranslation() { return translation; }
    public String getAddedDate() { return addedDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
