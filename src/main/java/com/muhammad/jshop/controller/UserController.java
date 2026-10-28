package com.muhammad.jshop.controller;

import com.muhammad.jshop.dto.UserDto;
import com.muhammad.jshop.exception.ResourceNotFoundException;
import com.muhammad.jshop.request.UpdateUserRequest;
import com.muhammad.jshop.request.UserRequest;
import com.muhammad.jshop.response.ApiResponse;
import com.muhammad.jshop.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "${api.prefix}/users")
public class UserController {
    private final IUserService userService;

    @PostMapping("/")
    public ResponseEntity<ApiResponse> createUser(
            @RequestBody UserRequest userRequest
            )
    {
        try {
            UserDto userDto = userService.createUser(userRequest);
            return ResponseEntity.ok(new ApiResponse("User Created successfully", userDto));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse> getUser(@PathVariable Long userId) {
        try {
            UserDto userDto = userService.getUserById(userId);
            return ResponseEntity.ok(new ApiResponse("User Found successfully", userDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PatchMapping("/update/{userId}")
    public ResponseEntity<ApiResponse> updateUser(@PathVariable Long userId, @RequestBody UpdateUserRequest userRequest) {
        try {
            UserDto userDto = userService.updateUser(userId, userRequest);
            return ResponseEntity.ok(new ApiResponse("User Updated successfully", userDto));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
