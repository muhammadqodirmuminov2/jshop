package com.muhammad.jshop.request;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Email
    private String email;
    private String firstName;
    private String lastName;
}
