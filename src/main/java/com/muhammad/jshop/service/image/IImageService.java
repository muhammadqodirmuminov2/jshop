package com.muhammad.jshop.service.image;

import com.muhammad.jshop.dto.ImageDto;
import com.muhammad.jshop.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    ImageDto save(MultipartFile file);
    List<ImageDto> multiUpload(List<MultipartFile> files);
    Image getImageById(Long id);
    byte[] getImageBytes(Long id);
}
