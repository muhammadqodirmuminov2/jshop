package com.muhammad.jshop.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserRequest {
    @NotNull(message = "firstName is required")
    @NotBlank(message = "firstName is cannot be blank!")
    private String firstName;

    @NotNull(message = "lastName is required")
    @NotBlank(message = "lastName is cannot be blank!")
    private String lastName;

    @Email(message = "Email is must be an email!")
    @NotNull
    @NotBlank
    private String email;

    @NotNull
    @NotBlank
    @NotEmpty
    private  String password;

}
