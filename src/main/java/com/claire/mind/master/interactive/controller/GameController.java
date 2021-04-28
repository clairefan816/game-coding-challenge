package com.claire.mind.master.interactive.controller;

import com.claire.mind.master.interactive.exception.InvalidGameException;
import com.claire.mind.master.interactive.exception.InvalidGuessException;
import com.claire.mind.master.interactive.exception.NoResponseException;
import com.claire.mind.master.interactive.exception.NotFoundException;
import com.claire.mind.master.interactive.model.Game;
import com.claire.mind.master.interactive.model.GameGuess;
import com.claire.mind.master.interactive.model.PlayerPreference;
import com.claire.mind.master.interactive.service.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/v1/mindmaster")
public class GameController {
    private final GameService gameService;

    @PostMapping("/game")
    public ResponseEntity<Game> startNewGame(@RequestBody PlayerPreference playerPreference) throws IOException, InterruptedException, NoResponseException {
        log.info("start game request: {}", playerPreference);
        // Wrap response into entity
        // gameService.createGame(playerPreference);
        return ResponseEntity.ok(gameService.createGame(playerPreference));
    }

    @PostMapping("/game/guess")
    public ResponseEntity<Game> gamePlay(@RequestBody GameGuess gameGuess) throws InvalidGameException, NotFoundException, InvalidGuessException {
        log.info("gameplay : {}", gameGuess);
        Game game = gameService.playGame(gameGuess);
        return ResponseEntity.ok(game);
    }
}
