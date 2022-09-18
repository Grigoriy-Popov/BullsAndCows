package com.example.bullsAndCows.games;

import com.example.bullsAndCows.games.model.Game;
import com.example.bullsAndCows.users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameRepository extends JpaRepository<Game, Long> {
    Long countAllByUserAndFinishedIsTrue(User user);

    @Query("select g.finished from Game g where g.id = ?1")
    boolean checkFinishedGame(Long gameId);
}
