package com.muhammad.jshop.controller;

import com.muhammad.jshop.dto.ImageDto;
import com.muhammad.jshop.model.Image;
import com.muhammad.jshop.response.ApiResponse;
import com.muhammad.jshop.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

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

    @PostMapping(value = "/multi-upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse> multiUpload(
            @RequestParam List<MultipartFile> files
    ) {
        try {
            List<ImageDto> imageDto = imageService.multiUpload(files);
            return ResponseEntity.ok(new ApiResponse("Images uploaded successfully", imageDto));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> download(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        byte[] bytes = imageService.getImageBytes(imageId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + image.getFileName() + "\"")
                .body(new ByteArrayResource(bytes));
    }

    @GetMapping("/image/view/{imageId}")
    public ResponseEntity<Resource> view(@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        byte[] bytes = imageService.getImageBytes(imageId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(image.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + image.getFileName() + "\"")
                .body(new ByteArrayResource(bytes));
    }
}
