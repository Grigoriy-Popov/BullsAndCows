package com.example.bullsAndCows.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("SELECT u.activeGameId FROM User u WHERE u.id = :id")
    Long findActiveGameIdByUserId(Long id);

}
