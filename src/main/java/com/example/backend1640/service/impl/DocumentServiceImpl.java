package com.example.backend1640.service.impl;

import com.example.backend1640.entity.Contribution;
import com.example.backend1640.entity.Document;
import com.example.backend1640.exception.ContributionNotExistsException;
import com.example.backend1640.repository.ContributionRepository;
import com.example.backend1640.repository.DocumentRepository;
import com.example.backend1640.service.DocumentService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final ContributionRepository contributionRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository, ContributionRepository contributionRepository) {
        this.documentRepository = documentRepository;
        this.contributionRepository = contributionRepository;
    }

    @Override
    public Document saveDocument(MultipartFile file) throws IOException {
        Document document = new Document();

        document.setName(file.getOriginalFilename());
        document.setType(file.getContentType());
        document.setData(file.getBytes());
        document.setCreatedAt(new Date());
        document.setUpdatedAt(new Date());

        return documentRepository.save(document);
    }

    @Override
    public Optional<Document> getDocument(Long id) {
        return documentRepository.findById(id);
    }

    @Override
    public List<Document> getAllDocument() {
        return documentRepository.findAll();
    }

    private Contribution validateContributionExists(Long contributionId) {
        Optional<Contribution> optionalContribution = contributionRepository.findById(contributionId);

        if (optionalContribution.isEmpty()) {
            throw new ContributionNotExistsException("ContributionNotExists");
        }
        return optionalContribution.get();
    }
}
