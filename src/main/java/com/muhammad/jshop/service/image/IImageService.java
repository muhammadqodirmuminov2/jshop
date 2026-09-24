package com.muhammad.jshop.service.image;

import com.muhammad.jshop.dto.ImageDto;
import com.muhammad.jshop.model.Image;
import org.springframework.web.multipart.MultipartFile;

public interface IImageService {
    ImageDto save(MultipartFile file);
}
