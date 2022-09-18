package com.example.bullsAndCows.games;

import com.example.bullsAndCows.games.model.Game;
import com.example.bullsAndCows.games.model.Record;

import java.util.List;

public interface GameService {
    Game newGame(Long userId);

    List<String> newAttempt(String usersNumber, Long gameId);

    List<Record> getRecords();

    List<String> getGameAttemptsAsString(Long gameId);

    List<String> continueGame(Long userId);

    boolean checkWin(Long gameId);
}
