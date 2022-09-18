package com.example.bullsAndCows.users;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER;
//    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
