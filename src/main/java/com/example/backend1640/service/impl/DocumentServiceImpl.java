package com.example.backend1640.service.impl;

import com.example.backend1640.entity.Contribution;
import com.example.backend1640.entity.Document;
import com.example.backend1640.exception.ContributionNotExistsException;
import com.example.backend1640.exception.DocumentNotPDFException;
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
    public void saveDocument(MultipartFile file, String contributionId) {
        Contribution contribution = validateContributionExists(Long.valueOf(contributionId));

        Document document = new Document();

        document.setName(file.getOriginalFilename());
        document.setType(file.getContentType());
        try {
            document.setData(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        document.setCreatedAt(new Date());
        document.setUpdatedAt(new Date());
        document.setContributionId(contribution);

        if (validateDocumentIsPDF(document)) {
            documentRepository.save(document);
        } else
            throw new DocumentNotPDFException("DocumentIsNotPDF");
    }

    @Override
    public Optional<Document> getDocument(Long id) {
        return documentRepository.findById(id);
    }

    @Override
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    private Contribution validateContributionExists(Long contributionId) {
        Optional<Contribution> optionalContribution = contributionRepository.findById(contributionId);

        if (optionalContribution.isEmpty()) {
            throw new ContributionNotExistsException("ContributionNotExists");
        }
        return optionalContribution.get();
    }

    private boolean validateDocumentIsPDF(Document document) {
        return document.getType().equals("application/pdf");
    }
}
