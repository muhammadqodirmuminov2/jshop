package com.muhammad.jshop.service.user;

import com.muhammad.jshop.dto.UserDto;
import com.muhammad.jshop.exception.AlreadyExistException;
import com.muhammad.jshop.exception.ResourceNotFoundException;
import com.muhammad.jshop.model.User;
import com.muhammad.jshop.repository.UserRepository;
import com.muhammad.jshop.request.UpdateUserRequest;
import com.muhammad.jshop.request.UserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@RequiredArgsConstructor
@Service
public class UserService implements  IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserDto createUser(UserRequest userRequest) {
        String email = normalizeEmail(userRequest.getEmail());

        if (userRepository.existsByEmail(email)) {
            throw new AlreadyExistException("Email is already registered: " + email);
        }

        User user = new User();
        user.setEmail(email);
        user.setFirstName(userRequest.getFirstName().trim());
        user.setLastName(userRequest.getLastName().trim());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        return modelMapper.map(userRepository.save(user), UserDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(Long userId) {
        return modelMapper.map(getUserOrThrow(userId), UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String email = authentication.getName();
            return userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }
        return null;
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UpdateUserRequest request) {
        User user = getUserOrThrow(userId);

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            String email = normalizeEmail(request.getEmail());
            if (userRepository.existsByEmailAndIdNot(email, userId)) {
                throw new AlreadyExistException("Email is already in use: " + email);
            }
            user.setEmail(email);
        }
        if (request.getFirstName() != null && !request.getFirstName().isBlank()) {
            user.setFirstName(request.getFirstName().trim());
        }
        if (request.getLastName() != null && !request.getLastName().isBlank()) {
            user.setLastName(request.getLastName().trim());
        }

        return modelMapper.map(user, UserDto.class);
    }

    private User getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase(Locale.ROOT);
    }
}
