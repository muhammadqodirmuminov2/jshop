package com.muhammad.jshop.controller;

import com.muhammad.jshop.exception.AlreadyExistException;
import com.muhammad.jshop.model.Category;
import com.muhammad.jshop.request.CategoryRequest;
import com.muhammad.jshop.response.ApiResponse;
import com.muhammad.jshop.service.category.ICategoryInterface;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/categories")
public class CategoryController {

    private  final ICategoryInterface categoryService;

    @PostMapping("/add")
    public ResponseEntity<ApiResponse> addProduct(@RequestBody CategoryRequest categoryRequest) {
        try {
            Category category = categoryService.addCategory(categoryRequest);
            return ResponseEntity.ok(new ApiResponse("Success",category));
        } catch (AlreadyExistException e) {
            throw new RuntimeException(e);
        }
    }
}