package com.textile.smart_textile_tracking_system.controller;

import com.textile.smart_textile_tracking_system.entity.User;
import com.textile.smart_textile_tracking_system.service.ActivityLogService;
import com.textile.smart_textile_tracking_system.service.NotificationService;
import com.textile.smart_textile_tracking_system.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityLogService activityLogService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("user") User user,
                           BindingResult bindingResult,
                           Model model) {

        if (userService.isUsernameTaken(user.getUsername())) {
            bindingResult.rejectValue("username", "duplicate",
                    "Username already exists. Please choose another.");
        }

        if (userService.isEmailTaken(user.getEmail())) {
            bindingResult.rejectValue("email", "duplicate",
                    "Email is already registered. Please use another.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "register";
        }

        userService.saveUser(user);
        activityLogService.log("USER_REGISTRATION", user.getUsername(),
                "New " + user.getRole() + " registered");
        notificationService.createNotification(
                "New Registration",
                user.getFullName() + " (" + user.getRole() + ") registered and is awaiting approval.",
                "admin");
        return "redirect:/login?registered=true";
    }
}
