package com.example.bullsAndCows.game;

import com.example.bullsAndCows.game.model.Game;
import com.example.bullsAndCows.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameRepository extends JpaRepository<Game, Long> {

    long countAllByUserAndFinishedIsTrue(User user);

    @Query("SELECT g.finished FROM Game g WHERE g.id = ?1")
    boolean checkFinishedGame(Long gameId);

}
