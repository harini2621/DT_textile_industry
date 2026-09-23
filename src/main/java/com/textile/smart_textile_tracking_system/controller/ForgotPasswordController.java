package com.textile.smart_textile_tracking_system.controller;

import com.textile.smart_textile_tracking_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotPasswordController {

    @Autowired
    private UserService userService;

    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam String email,
                                        @RequestParam String newPassword,
                                        @RequestParam String confirmPassword,
                                        Model model) {

        if (email == null || email.isBlank()) {
            model.addAttribute("error", "Please enter your registered email.");
            return "forgot-password";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "forgot-password";
        }

        String strengthError = userService.validatePasswordStrength(newPassword);
        if (strengthError != null) {
            model.addAttribute("error", strengthError);
            return "forgot-password";
        }

        boolean success = userService.resetPassword(email, newPassword);

        if (!success) {
            model.addAttribute("error", "No account found with that email address.");
            return "forgot-password";
        }

        return "redirect:/login?reset=true";
    }
}
