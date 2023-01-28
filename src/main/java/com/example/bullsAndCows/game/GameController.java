package com.example.bullsAndCows.game;

import com.example.bullsAndCows.user.User;
import com.example.bullsAndCows.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import static com.example.bullsAndCows.constants.Constants.NUMBER_OF_DIGITS;

@Controller
@RequestMapping(path = "/main/game")
@RequiredArgsConstructor
public class GameController {
    private final GameService gameService;
    private final UserService userService;

    @GetMapping
    public String newGame(@AuthenticationPrincipal User user) {
        gameService.newGame(user.getId());
        return "game";
    }

    @PostMapping("/attempt")
    public String newAttempt(@AuthenticationPrincipal User user,
                             @RequestParam String userNumber,
                             Model model) {
        Long activeGameId = userService.findActiveGameIdByUserId(user.getId());
        List<String> previousAttempts = gameService.getAllGameAttemptsAsListOfStrings(activeGameId);
        if (userNumber.length() != NUMBER_OF_DIGITS) {
            model.addAttribute("errorMessage",
                    String.format("You need to enter a %d-digit number", NUMBER_OF_DIGITS));
            model.addAttribute("resultList", previousAttempts);
            return "game";
        }
        List<String> resultList = gameService.newAttempt(userNumber, user.getId());
        if (gameService.checkWin(activeGameId)) {
            model.addAttribute("resultList", previousAttempts);
            model.addAttribute("resultList", resultList);
            return "finishedGame";
        }
        model.addAttribute("resultList", resultList);
        return "game";
    }

    @GetMapping("/attempt")
    public String continueGame(@AuthenticationPrincipal User user, Model model) {
        Long activeGameId = userService.findActiveGameIdByUserId(user.getId());
        if (activeGameId == null) {
            return "incorrectGameContinue";
        }
        List<String> resultList = gameService.continueGame(user.getId());
        model.addAttribute("resultList", resultList);
        return "game";
    }
}