package com.example.backend1640.service.impl;

import com.example.backend1640.entity.Contribution;
import com.example.backend1640.entity.Image;
import com.example.backend1640.exception.ContributionNotExistsException;
import com.example.backend1640.exception.ImageFormatNotValidException;
import com.example.backend1640.exception.ImageNotExistsException;
import com.example.backend1640.repository.ContributionRepository;
import com.example.backend1640.repository.ImageRepository;
import com.example.backend1640.service.ImageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {
    private final ImageRepository imageRepository;

    private final ContributionRepository contributionRepository;

    public ImageServiceImpl(ImageRepository imageRepository, ContributionRepository contributionRepository) {
        this.imageRepository = imageRepository;
        this.contributionRepository = contributionRepository;
    }

    @Override
    public void saveImage(MultipartFile file, String contributionId) {
        Contribution contribution = validateContributionExists(Long.valueOf(contributionId));

        Image image = new Image();

        image.setName(file.getOriginalFilename());
        image.setType(file.getContentType());
        try {
            image.setData(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        image.setCreatedAt(new Date());
        image.setUpdatedAt(new Date());
        image.setContributionId(contribution);

        if (validateImageIsValid(image)) {
            imageRepository.save(image);
        } else
            throw new ImageFormatNotValidException("Image Format Not Valid");
    }

    @Override
    public Optional<Image> getImage(Long id) {
        return imageRepository.findById(id);
    }

    @Override
    public List<Image> getAllImages() {
        return imageRepository.findAll();
    }

    @Override
    public void updateImage(MultipartFile file, String imageId) {
        Image image = validateImageExists(Long.valueOf(imageId));

        image.setName(file.getOriginalFilename());
        image.setType(file.getContentType());
        try {
            image.setData(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        image.setUpdatedAt(new Date());

        if (validateImageIsValid(image)) {
            imageRepository.save(image);
        } else
            throw new ImageFormatNotValidException("Image Format Not Valid");
    }

    private Contribution validateContributionExists(Long contributionId) {
        Optional<Contribution> optionalContribution = contributionRepository.findById(contributionId);

        if (optionalContribution.isEmpty()) {
            throw new ContributionNotExistsException("ContributionNotExists");
        }
        return optionalContribution.get();
    }

    private Image validateImageExists(Long id) {
        Optional<Image> optionalImage = imageRepository.findById(id);

        if (optionalImage.isEmpty()) {
            throw new ImageNotExistsException("Image does not exist");
        }
        return optionalImage.get();
    }

    private boolean validateImageIsValid(Image image) {
        return image.getType().equals("image/jpeg") || image.getType().equals("image/png");
    }
}
