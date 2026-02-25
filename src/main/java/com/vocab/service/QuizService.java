package com.vocab.service;

import com.vocab.dto.QuizResultRequest;
import com.vocab.dto.QuizStatsResponse;
import com.vocab.dto.VocabDto;
import com.vocab.entity.QuizResult;
import com.vocab.entity.User;
import com.vocab.entity.Vocabulary;
import com.vocab.repository.QuizResultRepository;
import com.vocab.repository.VocabularyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuizService {

    private final QuizResultRepository quizResultRepository;
    private final VocabularyRepository vocabRepository;

    public QuizService(QuizResultRepository quizResultRepository, VocabularyRepository vocabRepository) {
        this.quizResultRepository = quizResultRepository;
        this.vocabRepository = vocabRepository;
    }

    public void saveResult(User user, QuizResultRequest request) {
        Vocabulary vocab = vocabRepository.findByIdAndUser(request.getVocabId(), user)
                .orElseThrow(() -> new IllegalArgumentException("단어를 찾을 수 없습니다."));

        QuizResult.QuizType type = QuizResult.QuizType.valueOf(request.getQuizType());
        quizResultRepository.save(new QuizResult(user, vocab, request.getCorrect(), type));
    }

    public QuizStatsResponse getStats(User user) {
        long total = quizResultRepository.countByUser(user);
        long correct = quizResultRepository.countByUserAndCorrect(user, true);
        double rate = total > 0 ? (double) correct / total * 100 : 0;

        LocalDateTime since = LocalDateTime.now().minusDays(7);
        List<Object[]> dailyRaw = quizResultRepository.findDailyStats(user, since);

        List<QuizStatsResponse.DailyStat> trend = dailyRaw.stream()
                .map(row -> new QuizStatsResponse.DailyStat(
                        row[0].toString(),
                        ((Number) row[1]).longValue(),
                        ((Number) row[2]).longValue()
                ))
                .collect(Collectors.toList());

        return new QuizStatsResponse(total, correct, Math.round(rate * 10.0) / 10.0, trend);
    }

    public List<VocabDto> getWrongWords(User user) {
        List<Long> wrongIds = quizResultRepository.findWrongVocabIds(user);
        if (wrongIds.isEmpty()) return List.of();

        return vocabRepository.findByIdInAndUser(wrongIds, user)
                .stream()
                .map(VocabDto::from)
                .collect(Collectors.toList());
    }
}
