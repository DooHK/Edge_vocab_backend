package com.vocab.repository;

import com.vocab.entity.Folder;
import com.vocab.entity.User;
import com.vocab.entity.Vocabulary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VocabularyRepository extends JpaRepository<Vocabulary, Long> {
    List<Vocabulary> findByUserOrderByCreatedAtDesc(User user);
    List<Vocabulary> findByUserAndFolderOrderByCreatedAtDesc(User user, Folder folder);
    List<Vocabulary> findByUserAndFolderIsNullOrderByCreatedAtDesc(User user);
    List<Vocabulary> findByIdInAndUser(List<Long> ids, User user);
    Optional<Vocabulary> findByIdAndUser(Long id, User user);
    void deleteByUser(User user);
    long countByUserAndFolder(User user, Folder folder);
    long countByUserAndFolderIsNull(User user);
}
