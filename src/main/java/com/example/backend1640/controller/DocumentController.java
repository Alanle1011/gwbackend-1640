package com.example.backend1640.controller;

import com.example.backend1640.entity.Document;
import com.example.backend1640.service.ContributionService;
import com.example.backend1640.service.DocumentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Optional;

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
    public String getAllDocument(Model model) {
        List<Document> documents = documentService.getAllDocuments();
        model.addAttribute("documents", documents);
        return "document";
    }

    @PostMapping
    public String saveDocument(@RequestParam("doc") MultipartFile[] files, @RequestParam(value = "contributionId", required = true) String contributionId) throws IOException {
        for (MultipartFile file : files) {
            documentService.saveDocument(file, contributionId);
        }

        return "redirect:/";
    }

    @PutMapping
    public void updateDocument(@RequestParam("doc") MultipartFile[] files, @RequestParam(value = "documentId", required = true) String documentId) throws IOException {
        for (MultipartFile file : files) {
            documentService.updateDocument(file, documentId);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity<ByteArrayResource> getDocument(@PathVariable Long id) {
        Optional<Document> documentOptional = documentService.getDocument(id);
        if (documentOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Document document = documentOptional.get();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getType()))
                .header("Content-Disposition", "inline; filename=\"" + document.getName() + "\"")
                .body(new ByteArrayResource(document.getData()));
    }

    @GetMapping("downloadAll")
    public ResponseEntity<byte[]> downloadAllDocumentsAsZip() {
        try {
            byte[] zipData = documentService.downloadAllDocumentsAsZip();
            if (zipData.length == 0) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment: filename=\"AllDocuments.zip\"")
                    .body(zipData);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("downloadByCoordinatorId/{id}")
    public ResponseEntity<byte[]> downloadDocumentAsZip(@PathVariable long id) {
        try {
            byte[] zipData = documentService.downloadDocumentsByCoordinatorIdAsZip(id);
            if (zipData.length == 0) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=\"documents.zip\"")
                    .body(zipData);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
