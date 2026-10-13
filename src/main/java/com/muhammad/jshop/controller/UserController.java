package com.muhammad.jshop.controller;

import com.muhammad.jshop.dto.UserDto;
import com.muhammad.jshop.request.UserRequest;
import com.muhammad.jshop.response.ApiResponse;
import com.muhammad.jshop.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
