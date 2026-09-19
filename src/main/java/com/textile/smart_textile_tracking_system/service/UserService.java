package com.textile.smart_textile_tracking_system.service;

import com.textile.smart_textile_tracking_system.entity.User;
import com.textile.smart_textile_tracking_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User login(String username, String password, String role) {
        return userRepository.findByUsernameAndPasswordAndRole(
                username,
                password,
                role
        );
    }
}