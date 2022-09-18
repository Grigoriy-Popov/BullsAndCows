package com.example.bullsAndCows.games;

import com.example.bullsAndCows.attempts.Attempt;
import com.example.bullsAndCows.attempts.AttemptRepository;
import com.example.bullsAndCows.exceptions.NotFoundException;
import com.example.bullsAndCows.games.model.BullsAndCows;
import com.example.bullsAndCows.games.model.Game;
import com.example.bullsAndCows.games.model.Record;
import com.example.bullsAndCows.users.User;
import com.example.bullsAndCows.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.bullsAndCows.constants.Constants.NUMBER_OF_DIGITS;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final AttemptRepository attemptRepository;
    private final UserRepository userRepository;

    @Override
    public Game newGame(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        List<Integer> separatedRandomNum = generateSeparatedRandomNum();
        String puzzledNumber = getNumberFromSeparatedNumber(separatedRandomNum);
        Game game = Game.builder()
                .user(user)
                .puzzledNumber(puzzledNumber)
                .finished(false)
                .build();
        game = gameRepository.save(game);
        user.setActiveGameId(game.getId());
        userRepository.save(user);
        System.out.println(puzzledNumber);
        return game;
    }

    @Override
    public List<String> newAttempt(String usersNumber, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User is not found"));
        Game game = gameRepository.findById(user.getActiveGameId())
                .orElseThrow(() -> new NotFoundException("Game is not found"));
        Attempt attempt = Attempt.builder()
                .game(game)
                .user(game.getUser())
                .build();
        List<Integer> separatedRandomNum = separateNumber(game.getPuzzledNumber());
        List<Integer> separatedUserNum = separateNumber(usersNumber);
        BullsAndCows bullsAndCows = checkCowsAndBulls(separatedRandomNum, separatedUserNum);
        String result;
        if (bullsAndCows.getBulls().equals(NUMBER_OF_DIGITS)) {
            result = usersNumber + " - " + bullsAndCows.getCows() + "C" + bullsAndCows.getBulls() + "B." +
                    " Congrats, you guessed the number!" ;
            game.setFinished(true);
            user.setActiveGameId(null);
            gameRepository.save(game);
            userRepository.save(user);
        } else {
            result = usersNumber + " - " + bullsAndCows.getCows() + "C" + bullsAndCows.getBulls() + "B.";
        }
        attempt.setResult(result);
        attemptRepository.save(attempt);
        return getGameAttemptsAsString(game.getId());
    }

    @Override
    public List<String> continueGame(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));
        Game game = gameRepository.findById(user.getActiveGameId())
                .orElseThrow(() -> new NotFoundException("Game is not found"));
        return getGameAttemptsAsString(game.getId());
    }

    @Override
    public List<Record> getRecords() {
        List<Record> recordsList = new ArrayList<>();
        List<User> usersList = userRepository.findAll();
        for (User user : usersList) {
            Long countUsersFinishedGames = gameRepository.countAllByUserAndFinishedIsTrue(user);
            if (!countUsersFinishedGames.equals(0L)) {
                Long countUsersFinishedAttempts = attemptRepository.countAllByUserAndGameFinishedIsTrue(user);
                Long averageAttempts = countUsersFinishedAttempts / countUsersFinishedGames;
                recordsList.add(new Record(user.getUsername(), averageAttempts));
            }
        }
        recordsList.sort(Record::compareTo);
        return recordsList;
    }

//    Все попытки пользователя в конкретной игру
    @Override
    public List<String> getGameAttemptsAsString(Long gameId) {
        List<Attempt> gameAttemptsList = attemptRepository.findAllByGameIdOrderByIdDesc(gameId);
        return gameAttemptsList.stream()
                .map(Attempt::getResult)
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkWin(Long gameId) {
        return gameRepository.checkFinishedGame(gameId);
    }

    private List<Integer> generateSeparatedRandomNum() {
        Random random = new Random();
        Set<Integer> numbers = new HashSet<>();
        while (numbers.size() != NUMBER_OF_DIGITS) {
            numbers.add(random.nextInt(10));
        }
        return new ArrayList<>(numbers);
    }

    private List<Integer> separateNumber(String number) {
        List<Integer> separatedUserNum = new ArrayList<>();
        char[] numArr = number.toCharArray();
        for (char num : numArr) {
            separatedUserNum.add(Character.getNumericValue(num));
        }
        return separatedUserNum;
    }

    private BullsAndCows checkCowsAndBulls(List<Integer> separatedRandomNum, List<Integer> separatedUserNum) {
        int cows = 0;
        int bulls = 0;
        for (int i = 0; i < NUMBER_OF_DIGITS; i++) {
            for (int j = 0; j < NUMBER_OF_DIGITS; j++) {
                if (separatedRandomNum.get(i).equals(separatedUserNum.get(j))) {
                    if (i == j) {
                        bulls++;
                    } else {
                        cows++;
                    }
                }
            }
        }
        return new BullsAndCows(cows, bulls);
    }

    private String getNumberFromSeparatedNumber(List<Integer> separatedNum) {
        StringBuilder stringNum = new StringBuilder();
        for (int i = 0; separatedNum.size() - i > 0; i++) {
            stringNum.append(separatedNum.get(i).toString());
        }
        return stringNum.toString();
    }
}
