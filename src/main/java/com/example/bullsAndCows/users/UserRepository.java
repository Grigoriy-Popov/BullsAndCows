package com.example.bullsAndCows.users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("select u.activeGameId from User u where u.id = ?1")
    Long findActiveGameIdByUserId(Long id);
}
