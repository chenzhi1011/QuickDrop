package com.quickdrop.controller;

import com.quickdrop.entity.FileRecord;
import com.quickdrop.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class FileController {

    @Autowired
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file) throws Exception {
        FileRecord record = fileService.saveFile(file);
        Map<String, String> result = new HashMap<>();
        result.put("token", record.getToken());
        result.put("downloadUrl", "/download/" + record.getToken());
        return ResponseEntity.ok(result);
    }

    @GetMapping("/download/{token}")
    public ResponseEntity<Resource> download(@PathVariable String token) throws IOException {
        FileRecord record = fileService.getFileByToken(token);
        if (record == null || record.getExpireTime().isBefore(LocalDateTime.now())) {
            return ResponseEntity.notFound().build();
        }

        File file = new File(record.getStoragePath());
        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        // Encode file names to prevent garbled characters
        String encodedFilename = URLEncoder.encode(record.getOriginalFilename(), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .body(new FileSystemResource(file));
    }
}
