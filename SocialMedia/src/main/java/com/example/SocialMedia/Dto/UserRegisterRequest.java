package com.example.SocialMedia.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRegisterRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 3, max = 30, message = "Name must be 3-30 characters")
    @Pattern(
        regexp = "^[a-zA-Z0-9._-]+$",
        message = "Name can contain only letters, numbers, dot, underscore, and hyphen"
    )
    private String name;

     @NotBlank(message = "Surname is required")
    @Size(min = 3, max = 30, message = "Surname must be 3-30 characters")
    @Pattern(
        regexp = "^[a-zA-Z0-9._-]+$",
        message = "Surname can contain only letters, numbers, dot, underscore, and hyphen"
    )
    private String surname;

    @NotBlank(message = "Email is required")
    @Email(message = "Email format is invalid")
    @Size(max = 120, message = "Email is too long")
    private String email;

    @NotBlank(message = "Password is required")

    private String password;;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name != null ? name.trim() : null;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname != null ? surname.trim() : null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email != null ? email.trim().toLowerCase() : null;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}