package com.textile.smart_textile_tracking_system.repository;

import com.textile.smart_textile_tracking_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findByRole(String role);

    User findByUsernameAndPasswordAndRole(String username, String password, String role);

    List<User> findByApprovedFalse();

    List<User> findByApprovedTrue();

    List<User> findByApproved(Boolean approved);

    long countByRole(String role);

    // Users awaiting approval, counted in the database.
    long countByApprovedFalse();
}
