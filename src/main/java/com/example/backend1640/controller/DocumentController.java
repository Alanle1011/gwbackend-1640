package com.example.backend1640.controller;

import com.example.backend1640.entity.Document;
import com.example.backend1640.service.DocumentService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("document")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping
    public String getAllDocument(Model model){
        List<Document> documents = documentService.getAllDocuments();
        model.addAttribute("documents", documents);
        return "document";
    }

    @PostMapping
    public String saveDocument(@RequestParam("documents") MultipartFile[] files) throws IOException {
        for (MultipartFile file : files) {
            documentService.saveDocument(file);
        }

        return "redirect:/";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<ByteArrayResource> downloadDocument(@PathVariable long id){
        Document document = documentService.getDocument(id).get();
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(document.getType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment: filename=\"" + document.getName() + "\"")
                .body(new ByteArrayResource(document.getData()));
    }
}
