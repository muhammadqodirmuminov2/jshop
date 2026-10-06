package com.muhammad.jshop.controller;

import com.muhammad.jshop.dto.CategoryDto;
import com.muhammad.jshop.exception.AlreadyExistException;
import com.muhammad.jshop.exception.ResourceNotFoundException;
import com.muhammad.jshop.request.CategoryRequest;
import com.muhammad.jshop.response.ApiResponse;
import com.muhammad.jshop.response.PageResponse;
import com.muhammad.jshop.service.category.ICategoryInterface;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    private  final ICategoryInterface categoryService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody CategoryRequest categoryRequest) {
        try {
            CategoryDto category = categoryService.addCategory(categoryRequest);
            return ResponseEntity.ok(new ApiResponse("Success", category));
        } catch (AlreadyExistException e) {
            return  ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse> allCategories(
            @RequestParam (required = false) String search,
            @ParameterObject @PageableDefault(size = 10, sort = "name") Pageable pageable
    ) {
        try {
            Page<CategoryDto> page = categoryService.getAllCategories(search, pageable);
            return ResponseEntity.ok(new ApiResponse("Success", PageResponse.of(page)));
        } catch (Exception e) {
            return   ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @GetMapping("/find-one/{categoryId}")
    public ResponseEntity<ApiResponse> findCategoryById(@PathVariable("categoryId") Long categoryId) {
        try {
            CategoryDto categoryDto = categoryService.findCategoryById(categoryId);
            return ResponseEntity.ok(new ApiResponse("Success", categoryDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @PatchMapping("/update/{categoryId}")
    public ResponseEntity<ApiResponse> updateCategory(
            @PathVariable Long categoryId,
            @RequestBody CategoryRequest categoryRequest
    ) {
        try {
            CategoryDto categoryDto = categoryService.updateCategory(categoryId,categoryRequest);
            return  ResponseEntity.ok(new ApiResponse("Success", categoryDto));
        } catch (ResourceNotFoundException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }

    @DeleteMapping("/delete/{categoryId}")
    public  ResponseEntity<ApiResponse> deleteCategory(
            @PathVariable Long categoryId
    ) {
        try {
            categoryService.deleteCategory(categoryId);
            return  ResponseEntity.ok(new ApiResponse("Success", null));
        } catch (ResourceNotFoundException e) {
            return   ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(),null));
        }
    }
}