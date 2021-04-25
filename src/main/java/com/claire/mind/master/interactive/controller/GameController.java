package com.claire.mind.master.interactive.controller;

import com.claire.mind.master.interactive.exception.InvalidGameException;
import com.claire.mind.master.interactive.exception.InvalidGuessException;
import com.claire.mind.master.interactive.exception.NotFoundException;
import com.claire.mind.master.interactive.model.Game;
import com.claire.mind.master.interactive.model.GameGuess;
import com.claire.mind.master.interactive.model.Player;
import com.claire.mind.master.interactive.service.GameService;
import com.claire.mind.master.interactive.storage.GameStorage;
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
    public ResponseEntity<Game> startNewGame(@RequestBody Player player) throws IOException, InterruptedException {
        log.info("start game request: {}", player);
        // Wrap response into entity
        // gameService.createGame(player);
        return ResponseEntity.ok(gameService.createGame(player));
    }

    @PostMapping("/game/guess")
    public ResponseEntity<Game> gamePlay(@RequestBody GameGuess gameGuess) throws InvalidGameException, NotFoundException, InvalidGuessException {
        log.info("gameplay : {}", gameGuess);
        Game game = gameService.playGame(gameGuess);
        return ResponseEntity.ok(game);
    }
}
