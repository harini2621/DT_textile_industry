package com.textile.smart_textile_tracking_system.controller;

import com.textile.smart_textile_tracking_system.entity.User;
import com.textile.smart_textile_tracking_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        @RequestParam String role,
                        Model model) {

        User user = userService.login(username, password, role);

        if (user == null) {
            model.addAttribute("error", "Invalid Username, Password or Role");
            return "login";
        }

        if ("OWNER".equalsIgnoreCase(user.getRole())) {
            return "redirect:/owner-dashboard";
        }

        if ("WORKER".equalsIgnoreCase(user.getRole())) {
            return "redirect:/worker-dashboard";
        }

        model.addAttribute("error", "Invalid Role");
        return "login";
    }
}