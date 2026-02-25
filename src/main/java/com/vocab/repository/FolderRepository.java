package com.vocab.repository;

import com.vocab.entity.Folder;
import com.vocab.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findByUserOrderByCreatedAtDesc(User user);
    Optional<Folder> findByIdAndUser(Long id, User user);
    Optional<Folder> findByUserAndName(User user, String name);
    long countByUser(User user);
}
