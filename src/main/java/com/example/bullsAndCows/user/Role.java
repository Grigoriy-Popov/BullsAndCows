package com.example.bullsAndCows.user;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER;
//    USER, ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }
}
