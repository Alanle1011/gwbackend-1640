package com.example.backend1640.service;

import com.example.backend1640.entity.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface DocumentService {
    void saveDocument(MultipartFile file, String contributionId) throws IOException;
    Optional<Document> getDocument(Long id);
    List<Document> getAllDocuments();

    void updateDocument(MultipartFile file, String documentId);
}
