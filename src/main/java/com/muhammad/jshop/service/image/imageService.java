package com.muhammad.jshop.service.image;

import com.muhammad.jshop.dto.ImageDto;
import com.muhammad.jshop.model.Image;
import com.muhammad.jshop.repository.ImageRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;

@AllArgsConstructor
@Service
public class imageService implements IImageService {
    private final ImageRepository imageRepository;

    @Override
    public ImageDto save(MultipartFile file) {
        try{
            Image image = new Image();
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));

            String buildDownloadUrl = "/api/v1/images/image/download/";
            String downloadUrl = buildDownloadUrl + image.getId();
            image.setDownloadUrl(downloadUrl);

            Image savedImage = imageRepository.save(image);

            ImageDto imageDto = new ImageDto();
            imageDto.setId(savedImage.getId());
            imageDto.setFileName(savedImage.getFileName());
            imageDto.setDownloadUrl(downloadUrl);

            return imageDto;
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
