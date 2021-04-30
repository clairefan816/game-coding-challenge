package com.claire.mind.master.interactive.storage;

import com.claire.mind.master.interactive.model.Game;
import lombok.Synchronized;

import java.util.HashMap;
import java.util.Map;

// Singleton class
// Return only one instance of game storage
public class GameStorage {
    // Key is GameID, Value is Game
    private static Map<String, Game> games;
    private static GameStorage instance;
    private GameStorage(){
        games = new HashMap<>();
    }

    public static synchronized GameStorage getInstance(){
        if (instance == null){
            instance = new GameStorage();
        }
        return instance;
    }

    public Map<String, Game> getGames(){
        return games;
    }

    public Game getGame(String gameId) {
        return games.get(gameId);
    }

    public void setGame(Game game){
        games.put(game.getGameId(), game);
    }
}
