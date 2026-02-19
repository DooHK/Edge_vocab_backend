package com.vocab.repository;

import com.vocab.entity.User;
import com.vocab.entity.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
    List<Vocabulary> findByUserOrderByCreatedAtDesc(User user);
    Optional<Vocabulary> findByIdAndUser(Long id, User user);
    void deleteByUser(User user);
}
