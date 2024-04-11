package com.example.backend1640.service;

import com.example.backend1640.entity.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public interface ImageService {
    void saveImage(MultipartFile file, String contributionId) throws IOException;
    Optional<Image> getImage(Long id);
    Image getImageByUserId(Long id);
    List<Image> getAllImages();
    void updateImage(MultipartFile file, String imageId);
    void saveUserImage(MultipartFile file, String userId);
    byte[] zipImages() throws IOException;
}
