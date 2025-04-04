package com.quickdrop.service;

import com.quickdrop.entity.FileRecord;
import com.quickdrop.properties.FileProperties;
import com.quickdrop.repository.FileRecordRepository;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    private FileProperties fileProperties;

    private final FileRecordRepository repository;

    public FileService(FileRecordRepository repository) {
        this.repository = repository;
    }

    public FileRecord saveFile(MultipartFile file) throws IOException {
        String uploadDir = fileProperties.getUploadDir();
        String token = UUID.randomUUID().toString().replace("-", "");
        String filename = token + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, filename);

        Files.copy(file.getInputStream(), filePath); //copy file to filePath

        FileRecord record = new FileRecord();
        record.setToken(token);
        record.setOriginalFilename(file.getOriginalFilename());
        record.setStoragePath(filePath.toString());
        record.setUploadTime(LocalDateTime.now());
        record.setExpireTime(LocalDateTime.now().plusMinutes(30));

        return repository.save(record);
    }

    public FileRecord getFileByToken(String token) {
        return repository.findByToken(token).orElse(null);
    }

    public void deleteRecord(FileRecord record) {
        try {
            Files.deleteIfExists(Paths.get(record.getStoragePath()));
        } catch (IOException ignored) {}
        repository.delete(record);
    }
}