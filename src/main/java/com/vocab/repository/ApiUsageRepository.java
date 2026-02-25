package com.vocab.repository;

import com.vocab.entity.ApiUsage;
import com.vocab.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiUsageRepository extends JpaRepository<ApiUsage, Long> {
    Optional<ApiUsage> findByUserAndMonthYear(User user, String monthYear);
}
