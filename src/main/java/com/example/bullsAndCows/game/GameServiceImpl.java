package com.example.bullsAndCows.game;

import com.example.bullsAndCows.attempt.Attempt;
import com.example.bullsAndCows.attempt.AttemptRepository;
import com.example.bullsAndCows.exceptions.NotFoundException;
import com.example.bullsAndCows.game.model.BullsAndCows;
import com.example.bullsAndCows.game.model.Game;
import com.example.bullsAndCows.user.User;
import com.example.bullsAndCows.user.UserRepository;
import com.example.bullsAndCows.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.bullsAndCows.constants.Constants.NUMBER_OF_DIGITS;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;
    private final AttemptRepository attemptRepository;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public Game newGame(Long userId) {
        User user = userService.getUserById(userId);
        String puzzledNumber = generateRandomNumberAsString();
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
    @Transactional
    public List<String> newAttempt(String usersNumber, Long userId) {
        User user = userService.getUserById(userId);
        Game activeGame = getGameByIdOrThrowException(user.getActiveGameId());
        Attempt attempt = Attempt.builder()
                .game(activeGame)
                .user(activeGame.getUser())
                .build();
        BullsAndCows bullsAndCows = getBullsAndCowsInUsersNumber(activeGame.getPuzzledNumber(), usersNumber);
        String result;
        if (bullsAndCows.getBulls().equals(NUMBER_OF_DIGITS)) {
            result = String.format("%s - %dC%dB. Congrats, you guessed the number!", usersNumber,
                    bullsAndCows.getCows(), bullsAndCows.getBulls());
            activeGame.setFinished(true);
            user.setActiveGameId(null);
            gameRepository.save(activeGame);
            userRepository.save(user);
        } else {
            result = String.format("%s - %dC%dB.", usersNumber, bullsAndCows.getCows(), bullsAndCows.getBulls());
        }
        attempt.setResult(result);
        attemptRepository.save(attempt);
        return getAllGameAttemptsAsListOfStrings(activeGame.getId());
    }

    @Override
    public List<String> continueGame(Long userId) {
        User user = userService.getUserById(userId);
        Game activeGame = getGameByIdOrThrowException(user.getActiveGameId());
        return getAllGameAttemptsAsListOfStrings(activeGame.getId());
    }

    @Override
    public List<String> getAllGameAttemptsAsListOfStrings(Long gameId) {
        List<Attempt> gameAttemptsList = attemptRepository.findAllByGameIdOrderByIdDesc(gameId);
        return gameAttemptsList.stream()
                .map(Attempt::getResult)
                .collect(Collectors.toList());
    }

    @Override
    public boolean checkWin(Long gameId) {
        return gameRepository.checkFinishedGame(gameId);
    }

    private String generateRandomNumberAsString() {
        Random random = new Random();
        Set<Integer> numberAsSet = new HashSet<>(NUMBER_OF_DIGITS);
        while (numberAsSet.size() != NUMBER_OF_DIGITS) {
            numberAsSet.add(random.nextInt(10));
        }
        return getNumberAsStringFromIterableOfIntegers(numberAsSet);
    }

    private String getNumberAsStringFromIterableOfIntegers(Iterable<Integer> separatedNum) {
        StringBuilder stringNum = new StringBuilder();
        for (Integer num : separatedNum) {
            stringNum.append(num);
        }
        return stringNum.toString();
    }

    private List<Integer> separateNumberAsStringToListOfIntegers(String number) {
        List<Integer> separatedUserNum = new ArrayList<>();
        char[] numArr = number.toCharArray();
        for (char num : numArr) {
            separatedUserNum.add(Character.getNumericValue(num));
        }
        return separatedUserNum;
    }

    private BullsAndCows getBullsAndCowsInUsersNumber(String puzzledNumBer, String usersNumber) {
        List<Integer> separatedPuzzledNumber = separateNumberAsStringToListOfIntegers(puzzledNumBer);
        List<Integer> separatedUsersNumber = separateNumberAsStringToListOfIntegers(usersNumber);
        int cows = 0;
        int bulls = 0;
        for (int i = 0; i < NUMBER_OF_DIGITS; i++) {
            for (int j = 0; j < NUMBER_OF_DIGITS; j++) {
                if (separatedPuzzledNumber.get(i).equals(separatedUsersNumber.get(j))) {
                    if (i == j) {
                        bulls++;
                    } else {
                        cows++;
                    }
                }
            }
        }
        return new BullsAndCows(bulls, cows);
    }

    private Game getGameByIdOrThrowException(Long gameId) {
        return gameRepository.findById(gameId)
                .orElseThrow(() -> new NotFoundException("Game not found"));
    }
}
