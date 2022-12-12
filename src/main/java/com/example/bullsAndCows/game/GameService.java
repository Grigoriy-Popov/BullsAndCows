package com.example.bullsAndCows.game;

import com.example.bullsAndCows.game.model.Game;
import com.example.bullsAndCows.record.Record;

import java.util.List;

public interface GameService {
    Game newGame(Long userId);

    List<String> newAttempt(String usersNumber, Long gameId);

    List<String> getAllGameAttemptsAsListOfStrings(Long gameId);

    List<String> continueGame(Long userId);

    boolean checkWin(Long gameId);
}
