package com.vocab.repository;

import com.vocab.entity.QuizResult;
import com.vocab.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface QuizResultRepository extends JpaRepository<QuizResult, Long> {

    long countByUser(User user);

    long countByUserAndCorrect(User user, boolean correct);

    @Query("SELECT qr.vocabulary.id FROM QuizResult qr " +
           "WHERE qr.user = :user AND qr.correct = false " +
           "GROUP BY qr.vocabulary.id " +
           "HAVING SUM(CASE WHEN qr.correct = true THEN 1 ELSE 0 END) = 0 " +
           "OR MAX(CASE WHEN qr.correct = false THEN qr.answeredAt END) > " +
           "   COALESCE(MAX(CASE WHEN qr.correct = true THEN qr.answeredAt END), '1970-01-01')")
    List<Long> findWrongVocabIds(@Param("user") User user);

    @Query("SELECT FUNCTION('DATE', qr.answeredAt) as quizDate, " +
           "COUNT(qr) as total, " +
           "SUM(CASE WHEN qr.correct = true THEN 1 ELSE 0 END) as correctCount " +
           "FROM QuizResult qr " +
           "WHERE qr.user = :user AND qr.answeredAt >= :since " +
           "GROUP BY FUNCTION('DATE', qr.answeredAt) " +
           "ORDER BY quizDate DESC")
    List<Object[]> findDailyStats(@Param("user") User user, @Param("since") LocalDateTime since);
}
