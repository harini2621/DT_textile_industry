package com.textile.smart_textile_tracking_system.controller;

import com.textile.smart_textile_tracking_system.entity.User;
import com.textile.smart_textile_tracking_system.service.ActivityLogService;
import com.textile.smart_textile_tracking_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityLogService activityLogService;

    @GetMapping("/profile")
    public String profilePage(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        userService.getUserByUsername(userDetails.getUsername()).ifPresent(user ->
                model.addAttribute("user", user));
        return "profile";
    }

    @PostMapping("/update-profile")
    public String updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                @RequestParam String fullName,
                                @RequestParam String email,
                                RedirectAttributes redirectAttributes) {

        userService.updateProfile(userDetails.getUsername(), fullName, email);
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully.");
        return "redirect:/profile";
    }

    @PostMapping("/change-password")
    public String changePassword(@AuthenticationPrincipal UserDetails userDetails,
                                 @RequestParam String currentPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 RedirectAttributes redirectAttributes) {

        if (!newPassword.equals(confirmPassword)) {
            redirectAttributes.addFlashAttribute("error", "New passwords do not match.");
            return "redirect:/profile";
        }

        String strengthError = userService.validatePasswordStrength(newPassword);
        if (strengthError != null) {
            redirectAttributes.addFlashAttribute("error", strengthError);
            return "redirect:/profile";
        }

        boolean changed = userService.changePassword(userDetails.getUsername(), currentPassword, newPassword);

        if (!changed) {
            redirectAttributes.addFlashAttribute("error", "Current password is incorrect.");
        } else {
            redirectAttributes.addFlashAttribute("success", "Password changed successfully.");
            activityLogService.log("PASSWORD_CHANGE", userDetails.getUsername(),
                    "Password changed successfully");
        }

        return "redirect:/profile";
    }
}
