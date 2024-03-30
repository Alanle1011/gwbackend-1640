package com.example.backend1640.controller;

import com.example.backend1640.dto.ReadContributionByCoordinatorIdDTO;
import com.example.backend1640.entity.Document;
import com.example.backend1640.service.ContributionService;
import com.example.backend1640.service.DocumentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@CrossOrigin
@RestController
@RequestMapping("document")
public class DocumentController {
    private final DocumentService documentService;
    private final ContributionService contributionService;

    public DocumentController(DocumentService documentService, ContributionService contributionService) {
        this.documentService = documentService;
        this.contributionService = contributionService;
    }

    @GetMapping
    public String getAllDocument(Model model){
        List<Document> documents = documentService.getAllDocuments();
        model.addAttribute("documents", documents);
        return "document";
    }

    @PostMapping
    public String saveDocument(@RequestParam("doc") MultipartFile[] files, @RequestParam(value="contributionId", required=true) String contributionId) throws IOException {
        for (MultipartFile file : files) {
            documentService.saveDocument(file, contributionId);
        }

        return "redirect:/";
    }

    @PutMapping
    public void updateDocument(@RequestParam("doc") MultipartFile[] files, @RequestParam(value="documentId", required=true) String documentId) throws IOException {
        for (MultipartFile file : files) {
            documentService.updateDocument(file, documentId);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ByteArrayResource> downloadDocument(@PathVariable long id){
        Document document = documentService.getDocument(id).get();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getType()))
                .header("Content-Disposition", "attachment: filename=\"" + document.getName() + "\"")
                .body(new ByteArrayResource(document.getData()));
    }

    @GetMapping("downloadByCoordinatorId/{id}")
    public ResponseEntity<byte[]> downloadDocumentAsZip(@PathVariable long id) {
        List<ReadContributionByCoordinatorIdDTO> contributions = contributionService.findByCoordinatorId(id);
        if (contributions.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

            for (ReadContributionByCoordinatorIdDTO contribution : contributions) {
                if (contribution.getDocumentId() != null) {
                    byte[] documentData = documentService.getDocument(contribution.getDocumentId()).get().getData();
                    String documentName = documentService.getDocument(contribution.getDocumentId()).get().getName();

                    ZipEntry entry = new ZipEntry(documentName);
                    zipOutputStream.putNextEntry(entry);
                    zipOutputStream.write(documentData);
                    zipOutputStream.closeEntry();
                }
            }

            zipOutputStream.finish();
            zipOutputStream.close();

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"documents.zip\"")
                    .body(outputStream.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("downloadSelectedDocuments")
    public ResponseEntity<byte[]> downloadSelectedDocuments(@RequestParam List<Long> documentIds) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream);

            for (Long documentId : documentIds) {
                Optional<Document> documentOptional = documentService.getDocument(documentId);
                if (documentOptional.isPresent()) {
                    Document document = documentOptional.get();
                    byte[] documentData = document.getData();
                    String documentName = document.getName();

                    ZipEntry entry = new ZipEntry(documentName);
                    zipOutputStream.putNextEntry(entry);
                    zipOutputStream.write(documentData);
                    zipOutputStream.closeEntry();
                }
            }

            zipOutputStream.finish();
            zipOutputStream.close();

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"selected_documents.zip\"")
                    .body(outputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
