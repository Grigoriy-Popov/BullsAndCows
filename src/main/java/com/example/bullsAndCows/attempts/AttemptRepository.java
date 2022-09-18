package com.example.bullsAndCows.attempts;

import com.example.bullsAndCows.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {

    List<Attempt> findAllByGameIdOrderByIdDesc(Long gameId);

    Long countAllByUserAndGameFinishedIsTrue(User user);
}
