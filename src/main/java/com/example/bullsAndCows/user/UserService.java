package com.example.bullsAndCows.user;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User createUser(User user);

    User getUserByUsername(String username);

    User getUserById(Long id);

    boolean existsByUsername(String username);

    Long findActiveGameIdByUserId(Long userId);

}
