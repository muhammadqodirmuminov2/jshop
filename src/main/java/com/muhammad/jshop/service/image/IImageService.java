package com.muhammad.jshop.service.image;

import com.muhammad.jshop.dto.ImageDto;
import org.springframework.web.multipart.MultipartFile;

public interface IImageService {
    ImageDto save(MultipartFile file);
}
