package com.vocab.dto;

import java.util.List;

public class QuizStatsResponse {

    private long totalQuizzes;
    private long correctCount;
    private double correctRate;
    private List<DailyStat> recentTrend;

    public QuizStatsResponse() {}

    public QuizStatsResponse(long totalQuizzes, long correctCount, double correctRate, List<DailyStat> recentTrend) {
        this.totalQuizzes = totalQuizzes;
        this.correctCount = correctCount;
        this.correctRate = correctRate;
        this.recentTrend = recentTrend;
    }

    public long getTotalQuizzes() { return totalQuizzes; }
    public long getCorrectCount() { return correctCount; }
    public double getCorrectRate() { return correctRate; }
    public List<DailyStat> getRecentTrend() { return recentTrend; }

    public static class DailyStat {
        private String date;
        private long total;
        private long correct;

        public DailyStat(String date, long total, long correct) {
            this.date = date;
            this.total = total;
            this.correct = correct;
        }

        public String getDate() { return date; }
        public long getTotal() { return total; }
        public long getCorrect() { return correct; }
    }
}
