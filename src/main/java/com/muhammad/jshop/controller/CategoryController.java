package com.muhammad.jshop.controller;

import com.muhammad.jshop.dto.CategoryDto;
import com.muhammad.jshop.exception.AlreadyExistException;
import com.muhammad.jshop.model.Category;
import com.muhammad.jshop.request.CategoryRequest;
import com.muhammad.jshop.response.ApiResponse;
import com.muhammad.jshop.service.category.ICategoryInterface;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ApiResponse> allCategories() {
        try {
            List<CategoryDto> categories = categoryService.getAllCategories();
            return  ResponseEntity.ok(new ApiResponse("Success", categories));
        } catch (Exception e) {
            return   ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(),null));
        }
    }
}