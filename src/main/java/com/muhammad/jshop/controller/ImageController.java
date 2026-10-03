package com.muhammad.jshop.controller;

import com.muhammad.jshop.dto.ImageDto;
import com.muhammad.jshop.response.ApiResponse;
import com.muhammad.jshop.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final IImageService imageService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> upload(@RequestParam MultipartFile file) {
        try {
            ImageDto imageDto = imageService.save(file);
            return ResponseEntity.ok(new ApiResponse("Image uploaded successfully", imageDto));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
