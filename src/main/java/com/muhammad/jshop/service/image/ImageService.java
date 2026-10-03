package com.muhammad.jshop.service.image;

import com.muhammad.jshop.dto.ImageDto;
import com.muhammad.jshop.exception.ResourceNotFoundException;
import com.muhammad.jshop.model.Image;
import com.muhammad.jshop.repository.ImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class ImageService implements IImageService {
    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public List<ImageDto> multiUpload(List<MultipartFile> files) {
        List<ImageDto> savedImageDto = new ArrayList<>();
        for (MultipartFile file : files) {
            savedImageDto.add(uploadToDb(file));
        }
        return  savedImageDto;
    }

    @Override
    @Transactional
    public ImageDto save(MultipartFile file) {
        return uploadToDb(file);
    }

    @Override
    public Image getImageById(Long id) {
        return imageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No image found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getImageBytes(Long id) {
        Image image = getImageById(id);
        try {
            Blob blob = image.getImage();
            return blob.getBytes(1, (int) blob.length());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to get image bytes", e);
        }
    }

    private ImageDto uploadToDb(MultipartFile file) {
        try {
            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            Image savedImage = imageRepository.save(image);

            String buildDownloadUrl = "/api/v1/images/image/download/";
            String downloadUrl = buildDownloadUrl + savedImage.getId();
            savedImage.setDownloadUrl(downloadUrl);

            ImageDto imageDto = new ImageDto();
            imageDto.setId(savedImage.getId());
            imageDto.setFileName(savedImage.getFileName());
            imageDto.setDownloadUrl(downloadUrl);
            return  imageDto;
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Failed to save image: " + file.getOriginalFilename(), e);
        }
    }
}
