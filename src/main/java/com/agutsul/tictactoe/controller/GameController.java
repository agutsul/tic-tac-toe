package com.agutsul.tictactoe.controller;

import java.util.Objects;

import javax.lang.model.UnknownEntityException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.agutsul.tictactoe.entity.GameEntity;
import com.agutsul.tictactoe.entity.GameStatus;
import com.agutsul.tictactoe.model.ActionModel;
import com.agutsul.tictactoe.model.GameModel;
import com.agutsul.tictactoe.model.StartGameModel;
import com.agutsul.tictactoe.service.GameService;

import jakarta.validation.Valid;

@RestController
class GameController {

    private final GameService gameService;

    GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/games/")
    public ResponseEntity<?> startGame(@RequestBody @Valid StartGameModel model) {
        try {
            var game = gameService.createGame(model.getPlayerName1(), model.getPlayerName2());
            return ResponseEntity.ok(createGameModel(game));
        } catch (UnknownEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping("/games/{gameId}/move")
    public ResponseEntity<?> performMove(@PathVariable String gameId, 
                                         @RequestBody @Valid ActionModel action) {
        try {
            var game = gameService.performMove(gameId, action.getPlayerName(), action.getPosition());
            return ResponseEntity.ok(createGameModel(game));
        } catch (UnknownEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(e.getMessage());
        }
    }

    @GetMapping("/games/{gameId}")
    public ResponseEntity<?> getState(@PathVariable String gameId) {
        try {
            var game = gameService.getBySessionId(gameId);
            return ResponseEntity.ok(createGameModel(game));
        } catch (UnknownEntityException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    // utility methods

    private static GameModel createGameModel(GameEntity game) {
        var gameModel = new GameModel();

        gameModel.setSessionId(game.getSessionId());
        gameModel.setBoard(game.getBoard());
        gameModel.setStatus(statusOf(game));
        gameModel.setActivePlayer(game.getActivePlayer().getName());

        return gameModel;
    }

    private static GameModel.Status statusOf(GameEntity entity) {
        var winner = entity.getWinner();

        switch (entity.getStatus()) {
        case GameStatus.ERROR:
            return GameModel.Status.ERROR; 
        case GameStatus.RUNNING:
            return GameModel.Status.IN_PROGRESS;
        case GameStatus.FINISHED:
            return winner == null
                ? GameModel.Status.DRAW
                : Objects.equals(entity.getActivePlayer(), winner) 
                            ? GameModel.Status.WIN 
                            : GameModel.Status.DEFEAT;
        }

        return null;
    }
}