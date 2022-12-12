package com.example.bullsAndCows.record;

import com.example.bullsAndCows.attempt.AttemptRepository;
import com.example.bullsAndCows.game.GameRepository;
import com.example.bullsAndCows.user.User;
import com.example.bullsAndCows.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecordServiceImpl implements RecordService {
    private final UserRepository userRepository;
    private final GameRepository gameRepository;
    private final AttemptRepository attemptRepository;

    @Override
    public List<Record> getRecords() {
        List<Record> recordsList = new ArrayList<>();
        List<User> usersList = userRepository.findAll();
        for (User user : usersList) {
            long countUsersFinishedGames = gameRepository.countAllByUserAndFinishedIsTrue(user);
            if (countUsersFinishedGames != 0) {
                long countUsersFinishedAttempts = attemptRepository.countAllByUserAndGameFinishedIsTrue(user);
                long averageAttempts = countUsersFinishedAttempts / countUsersFinishedGames;
                recordsList.add(new Record(user.getUsername(), averageAttempts));
            }
        }
        recordsList.sort(Record::compareTo);
        return recordsList;
    }
}
