package com.textile.smart_textile_tracking_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDTO {

    private Long id;

    @NotBlank(message = "Full name is required.")
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters.")
    private String fullName;

    @NotBlank(message = "Email is required.")
    @Email(message = "Please enter a valid email address.")
    private String email;

    @NotBlank(message = "Role is required.")
    private String role;

    public UserDTO() {
    }

    public UserDTO(Long id, String fullName, String email, String role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}