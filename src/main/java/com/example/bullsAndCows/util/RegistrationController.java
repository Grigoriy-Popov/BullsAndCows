package com.example.bullsAndCows.util;

import com.example.bullsAndCows.users.User;
import com.example.bullsAndCows.users.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final UserService userService;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String createUser(User user, Map<String, Object> model) {
        if (userService.existsByUsername(user.getUsername())) {
            model.put("message", "This username is already taken");
            return "registration";
        }
        userService.createUser(user);
        return "redirect:/login";
    }
}
