package com.textile.smart_textile_tracking_system.config;

import com.textile.smart_textile_tracking_system.service.ActivityLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/**
 * Records a "LOGIN" activity whenever a user successfully authenticates.
 * Uses Spring Security's event instead of touching LoginController,
 * so the existing login flow and its routes remain untouched.
 */
@Component
public class LoginActivityListener {

    @Autowired
    private ActivityLogService activityLogService;

    @EventListener
    public void onAuthenticationSuccess(AuthenticationSuccessEvent event) {
        String username = event.getAuthentication().getName();
        activityLogService.log("LOGIN", username, "User logged in successfully");
    }
}
