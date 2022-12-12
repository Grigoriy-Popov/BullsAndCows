package com.example.bullsAndCows.attempt;

import com.example.bullsAndCows.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {

    List<Attempt> findAllByGameIdOrderByIdDesc(Long gameId);

    Long countAllByUserAndGameFinishedIsTrue(User user);
}
