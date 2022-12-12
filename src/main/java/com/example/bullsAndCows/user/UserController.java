package com.example.bullsAndCows.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/users")
    public User getUser() {
        return userRepository.findByUsername("q").orElse(new User());
    }

    @GetMapping("/users1")
    public User getUser(@AuthenticationPrincipal User user) {
        return user;
    }
}
