package com.example.backend1640.service;

import com.example.backend1640.entity.Document;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface DocumentService {
    Document saveDocument(MultipartFile file) throws IOException;
    Optional<Document> getDocument(Long id);
    List<Document> getAllDocument();
}
