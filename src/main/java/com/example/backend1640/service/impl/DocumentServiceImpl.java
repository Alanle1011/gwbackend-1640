package com.example.backend1640.service.impl;

import com.example.backend1640.constants.UserRoleEnum;
import com.example.backend1640.dto.ReadContributionByCoordinatorIdDTO;
import com.example.backend1640.entity.Contribution;
import com.example.backend1640.entity.Document;
import com.example.backend1640.entity.User;
import com.example.backend1640.exception.*;
import com.example.backend1640.repository.ContributionRepository;
import com.example.backend1640.repository.DocumentRepository;
import com.example.backend1640.repository.UserRepository;
import com.example.backend1640.service.ContributionService;
import com.example.backend1640.service.DocumentService;
import com.example.backend1640.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final ContributionRepository contributionRepository;
    private final ContributionService contributionService;
    private final UserRepository userRepository;

    public DocumentServiceImpl(DocumentRepository documentRepository, ContributionRepository contributionRepository, ContributionService contributionService, UserRepository userRepository) {
        this.documentRepository = documentRepository;
        this.contributionRepository = contributionRepository;
        this.contributionService = contributionService;
        this.userRepository = userRepository;
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

        if (validateDocumentFormatIsValid(document)) {
            documentRepository.save(document);
        } else
            throw new DocumentFormatNotValidException("Document Format Not Valid");
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Document> getDocument(Long id) {
        return documentRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public void updateDocument(MultipartFile file, String documentId) {
        Document document = validateDocumentExists(Long.valueOf(documentId));

        document.setName(file.getOriginalFilename());
        document.setType(file.getContentType());
        try {
            document.setData(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        document.setUpdatedAt(new Date());

        if (validateDocumentFormatIsValid(document)) {
            documentRepository.save(document);
        } else
            throw new DocumentFormatNotValidException("Document Format Not Valid");
    }

    @Override
    public byte[] downloadAllDocumentsAsZip() throws IOException {
        List<Document> documents = documentRepository.findAll();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

        for (Document document : documents) {
            ZipEntry entry = new ZipEntry(document.getName());
            zipOutputStream.putNextEntry(entry);
            zipOutputStream.write(document.getData());
            zipOutputStream.closeEntry();
        }

        zipOutputStream.finish();
        zipOutputStream.close();

        return outputStream.toByteArray();
    }

    @Override
    public byte[] downloadDocumentsByCoordinatorIdAsZip(long id) throws IOException {
        User coordinator = validateUserExists(id);
        if (coordinator.getUserRole() != UserRoleEnum.COORDINATOR) {
            throw new UserNotCoordinatorException("User Not Coordinator");
        }

        List<ReadContributionByCoordinatorIdDTO> contributions = contributionService.findByCoordinatorId(coordinator.getId());
        if (contributions.isEmpty()) {
            return new byte[0];
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

        for (ReadContributionByCoordinatorIdDTO contribution : contributions) {
            if (contribution.getDocumentId() != null) {
                byte[] documentData = documentRepository.findById(contribution.getDocumentId()).get().getData();
                String documentName = documentRepository.findById(contribution.getDocumentId()).get().getName();

                ZipEntry entry = new ZipEntry(documentName);
                zipOutputStream.putNextEntry(entry);
                zipOutputStream.write(documentData);
                zipOutputStream.closeEntry();
            }
        }

        zipOutputStream.finish();
        zipOutputStream.close();

        return outputStream.toByteArray();
    }

    private User validateUserExists(long id) {
        Optional<User> optionalUser = userRepository.findById(id);

        if (optionalUser.isEmpty()) {
            throw new UserNotExistsException("User Not Exists");
        }
        return optionalUser.get();
    }

    private Contribution validateContributionExists(Long contributionId) {
        Optional<Contribution> optionalContribution = contributionRepository.findById(contributionId);

        if (optionalContribution.isEmpty()) {
            throw new ContributionNotExistsException("ContributionNotExists");
        }
        return optionalContribution.get();
    }

    private Document validateDocumentExists(Long id) {
        Optional<Document> optionalDocument = documentRepository.findById(id);

        if (optionalDocument.isEmpty()) {
            throw new DocumentNotExistsException("Document Not Exists");
        }
        return optionalDocument.get();
    }

    private boolean validateDocumentFormatIsValid(Document document) {
        return document.getType().equals("application/pdf") || document.getType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    }
}
