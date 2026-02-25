package com.vocab.dto;

import jakarta.validation.constraints.NotNull;

public class QuizResultRequest {

    @NotNull
    private Long vocabId;

    @NotNull
    private Boolean correct;

    @NotNull
    private String quizType;

    public Long getVocabId() { return vocabId; }
    public Boolean getCorrect() { return correct; }
    public String getQuizType() { return quizType; }

    public void setVocabId(Long vocabId) { this.vocabId = vocabId; }
    public void setCorrect(Boolean correct) { this.correct = correct; }
    public void setQuizType(String quizType) { this.quizType = quizType; }
}
