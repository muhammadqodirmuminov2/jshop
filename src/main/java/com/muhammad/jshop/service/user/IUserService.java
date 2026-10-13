package com.muhammad.jshop.service.user;

import com.muhammad.jshop.dto.UserDto;
import com.muhammad.jshop.request.UpdateUserRequest;
import com.muhammad.jshop.request.UserRequest;

public interface IUserService {
    UserDto createUser(UserRequest userRequest);
    UserDto updateUser(Long userId, UpdateUserRequest request);
    UserDto getUserById(Long userId);
}
