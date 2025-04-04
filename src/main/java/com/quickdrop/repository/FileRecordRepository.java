package com.quickdrop.repository;

import com.quickdrop.entity.FileRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

public interface FileRecordRepository extends JpaRepository<FileRecord, Long> {
    Optional<FileRecord> findByToken(String token);
    List<FileRecord> findByExpireTimeBefore(LocalDateTime now);
}