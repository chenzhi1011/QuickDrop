package com.quickdrop.scheduler;

import com.quickdrop.entity.FileRecord;
import com.quickdrop.repository.FileRecordRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class FileCleanupScheduler {

    private final FileRecordRepository repository;

    public FileCleanupScheduler(FileRecordRepository repository) {
        this.repository = repository;
    }

    @Scheduled(fixedRate = 600000) // 每10分钟执行
    public void cleanupExpiredFiles() {
        List<FileRecord> expiredFiles = repository.findByExpireTimeBefore(LocalDateTime.now());
        expiredFiles.forEach(record -> {
            try {
                java.nio.file.Files.deleteIfExists(java.nio.file.Paths.get(record.getStoragePath()));
                repository.delete(record);
            } catch (Exception ignored) {}
        });
    }
}
