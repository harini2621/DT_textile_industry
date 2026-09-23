package com.textile.smart_textile_tracking_system.config;

import com.textile.smart_textile_tracking_system.entity.User;
import com.textile.smart_textile_tracking_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Seeds a single admin account on startup if none exists, so the Admin Module is
 * reachable without allowing self-registration as an administrator.
 * The account is created once and never overwritten afterwards.
 */
@Component
public class AdminDataInitializer implements CommandLineRunner {

    static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_EMAIL    = "admin@trizen.com";
    private static final String ADMIN_PASSWORD = "admin123";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername(ADMIN_USERNAME).isEmpty()
                && userRepository.findByRole("ADMIN").isEmpty()) {

            User admin = new User();
            admin.setFullName("System Administrator");
            admin.setUsername(ADMIN_USERNAME);
            admin.setEmail(ADMIN_EMAIL);
            admin.setRole("ADMIN");
            admin.setPassword(passwordEncoder.encode(ADMIN_PASSWORD));
            admin.setApproved(true);
            userRepository.save(admin);
        }
    }
}
