package com.vocab.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quiz_results")
public class QuizResult {

    public enum QuizType { MULTIPLE_CHOICE, WRITTEN }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vocabulary_id", nullable = false)
    private Vocabulary vocabulary;

    @Column(name = "is_correct", nullable = false)
    private boolean correct;

    @Enumerated(EnumType.STRING)
    @Column(name = "quiz_type", nullable = false)
    private QuizType quizType;

    @Column(name = "answered_at")
    private LocalDateTime answeredAt = LocalDateTime.now();

    public QuizResult() {}

    public QuizResult(User user, Vocabulary vocabulary, boolean correct, QuizType quizType) {
        this.user = user;
        this.vocabulary = vocabulary;
        this.correct = correct;
        this.quizType = quizType;
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public Vocabulary getVocabulary() { return vocabulary; }
    public boolean isCorrect() { return correct; }
    public QuizType getQuizType() { return quizType; }
    public LocalDateTime getAnsweredAt() { return answeredAt; }
}
