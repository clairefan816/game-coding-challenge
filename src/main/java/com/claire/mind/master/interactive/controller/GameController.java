package com.claire.mind.master.interactive.controller;

import com.claire.mind.master.interactive.exception.*;
import com.claire.mind.master.interactive.model.Constants;
import com.claire.mind.master.interactive.model.Game;
import com.claire.mind.master.interactive.model.GameGuess;
import com.claire.mind.master.interactive.model.PlayerPreference;
import com.claire.mind.master.interactive.service.GameService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("/v1/mindmaster")
public class GameController {
    private final GameService gameService;


    /**
     * Create a new game
     * @param playerPreference Object
     * @return Game
     * @throws IOException
     * @throws InterruptedException
     * @throws NoResponseException
     */
    @PostMapping("/game")
    public ResponseEntity<Game> startNewGame(@RequestBody PlayerPreference playerPreference) throws IOException, InterruptedException, NoResponseException {
        // Wrap response into entity
        // gameService.createGame(playerPreference);
        if (!Constants.ALLOWED_LEVELS.contains(playerPreference.getPreference())){
            throw new BadRequestException(String.format("Game level must be one of: %s",
                    String.join(", ", Constants.ALLOWED_LEVELS)));
        }
        return ResponseEntity.ok(gameService.createGame(playerPreference));
    }

    /**
     * Check result, update guesses and results
     * @param gameGuess Object
     * @return Game
     * @throws InvalidGameException
     * @throws NotFoundException
     * @throws InvalidGuessException
     */
    @PostMapping("/game/guess")
    public ResponseEntity<Game> gamePlay(@RequestBody GameGuess gameGuess) throws InvalidGameException, NotFoundException, InvalidGuessException {
        log.info("gameplay : {}", gameGuess);
        Game game = gameService.playGame(gameGuess);
        return ResponseEntity.ok(game);
    }

    /**
     * Get a game from GameStorage by gameId
     * @param gameId String
     * @return Game
     * @throws InvalidParamException
     */
    @GetMapping("/game/{gameId}")
    public ResponseEntity<Game> retrieveOneGame(@PathVariable String gameId) throws InvalidParamException {
        return ResponseEntity.ok(gameService.retrieveGame(gameId));
    }

}
