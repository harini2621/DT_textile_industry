package com.textile.smart_textile_tracking_system.service;

import com.textile.smart_textile_tracking_system.entity.User;
import com.textile.smart_textile_tracking_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean isUsernameTaken(String username) {
        if (username == null || username.isBlank()) return true;
        return userRepository.findByUsername(username.trim()).isPresent();
    }

    public boolean isEmailTaken(String email) {
        if (email == null || email.isBlank()) return false;
        return userRepository.findByEmail(email.trim()).isPresent();
    }

    public void saveUser(User user) {
        user.setUsername(user.getUsername().trim());
        user.setEmail(user.getEmail().trim());
        user.setFullName(user.getFullName().trim());
        user.setRole(user.getRole().trim().toUpperCase());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // New registrations require admin approval before they can log in.
        user.setApproved(false);
        userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        if (username == null || username.isBlank()) return Optional.empty();
        return userRepository.findByUsername(username.trim());
    }

    public void updateProfile(String username, String fullName, String email) {
        if (username == null || fullName == null || email == null) return;
        if (fullName.isBlank() || email.isBlank()) return;
        userRepository.findByUsername(username).ifPresent(user -> {
            user.setFullName(fullName.trim());
            user.setEmail(email.trim());
            userRepository.save(user);
        });
    }

    /**
     * Central password-strength rule used by register, change-password and reset-password.
     * Requires at least 6 characters, at least one letter and at least one digit,
     * and no leading/trailing whitespace. Returns null when valid, otherwise the
     * message to show the user.
     */
    public String validatePasswordStrength(String password) {
        if (password == null || password.isBlank()) {
            return "Password cannot be empty.";
        }
        if (!password.equals(password.trim())) {
            return "Password cannot start or end with spaces.";
        }
        if (password.length() < 6) {
            return "Password must be at least 6 characters.";
        }
        if (password.length() > 100) {
            return "Password must be at most 100 characters.";
        }
        boolean hasLetter = password.chars().anyMatch(Character::isLetter);
        boolean hasDigit = password.chars().anyMatch(Character::isDigit);
        if (!hasLetter || !hasDigit) {
            return "Password must contain at least one letter and one number.";
        }
        return null;
    }

    public boolean changePassword(String username, String currentPassword, String newPassword) {
        if (username == null || currentPassword == null || newPassword == null) return false;
        if (validatePasswordStrength(newPassword) != null) return false;
        Optional<User> optUser = userRepository.findByUsername(username);
        if (optUser.isPresent()) {
            User user = optUser.get();
            if (passwordEncoder.matches(currentPassword, user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }

    public boolean resetPassword(String email, String newPassword) {
        if (email == null || email.isBlank() || newPassword == null) return false;
        if (validatePasswordStrength(newPassword) != null) return false;
        Optional<User> optUser = userRepository.findByEmail(email.trim());
        if (optUser.isPresent()) {
            User user = optUser.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // ================= ADMIN: USER APPROVAL =================

    /** Users awaiting approval. A null flag is treated as "not pending". */
    public java.util.List<User> getPendingUsers() {
        return userRepository.findByApprovedFalse();
    }

    /** Number of users awaiting approval, counted in the database. */
    public long countPendingUsers() {
        return userRepository.countByApprovedFalse();
    }

    /** Users that are approved (includes legacy rows where the flag is null). */
    public java.util.List<User> getApprovedUsers() {
        return userRepository.findByApprovedTrue();
    }

    public void approveUser(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setApproved(true);
            userRepository.save(user);
        });
    }

    public void rejectUser(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setApproved(false);
            userRepository.save(user);
        });
    }
}
