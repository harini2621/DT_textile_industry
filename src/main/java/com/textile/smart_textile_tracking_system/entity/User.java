package com.textile.smart_textile_tracking_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "Full name is required.")
    @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters.")
    @Column(nullable = false)
    private String fullName;

    @NotBlank(message = "Username is required.")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters.")
    @Pattern(regexp = "^[A-Za-z0-9._-]+$", message = "Username may only contain letters, numbers, dot, underscore or hyphen.")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password is required.")
    @Size(min = 6, max = 100, message = "Password must be at least 6 characters.")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Role is required.")
    @Column(nullable = false)
    private String role;

    @NotBlank(message = "Email is required.")
    @Email(message = "Please enter a valid email address.")
    @Size(max = 150, message = "Email is too long.")
    @Column(nullable = false)
    private String email;

    // Null means "existing user" and is treated as approved so existing accounts stay unaffected.
    // New registrations are explicitly set to FALSE until an admin approves them.
    @Column(name = "approved")
    private Boolean approved;

    public User() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    /**
     * Existing rows have a null flag and must remain able to log in,
     * so null is treated as approved.
     */
    public boolean isApprovedUser() {
        return approved == null || approved;
    }
}