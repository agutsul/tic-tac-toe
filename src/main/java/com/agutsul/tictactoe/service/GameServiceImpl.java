package com.agutsul.tictactoe.service;

import org.hibernate.query.sqm.UnknownEntityException;
import org.slf4j.Logger;
import static org.slf4j.LoggerFactory.*;

import java.util.Objects;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agutsul.tictactoe.entity.GameEntity;
import com.agutsul.tictactoe.entity.GameStatus;
import com.agutsul.tictactoe.repository.GameRepository;

import jakarta.validation.constraints.NotNull;

@Service
public class GameServiceImpl implements GameService {

    private static final Logger LOGGER = getLogger(GameServiceImpl.class);
    
    private final GameRepository gameRepository;
    
    private final BoardService  boardService; 
    private final PlayerService playerService;
    
    public GameServiceImpl(GameRepository gameRepository,
                           BoardService boardService,
                           PlayerService playerService) {

        this.gameRepository = gameRepository;
        this.boardService   = boardService;
        this.playerService  = playerService;
    }

    @Override
    @Transactional
    public GameEntity createGame(@NotNull String playerName1, @NotNull String playerName2) {
        LOGGER.info("Creating game by player '{}' and '{}' is started", 
                playerName1, 
                playerName2
        );

        var player1 = playerService.getByName(playerName1);
        var player2 = playerService.getByName(playerName2);
        
        var game = new GameEntity();
        
        game.setPlayer1(player1);
        game.setPlayer2(player2);
        game.setActivePlayer(player1);
        game.setSessionId(UUID.randomUUID().toString());
        game.setStatus(GameStatus.RUNNING);
        game.setBoard(BoardService.DEFAULT_BOARD);
        
        var created = gameRepository.save(game);
        
        LOGGER.info("Creating game by player '{}' is done: {}", 
                playerName1, 
                created.getSessionId()
        );
        
        return created;
    }

    @Override
    @Transactional(readOnly = true)
    public GameEntity getBySessionId(@NotNull String sessionId) {
        LOGGER.info("Get game by sessionId '{}'", sessionId);

        var games = gameRepository.findBySessionId(sessionId);
        if (games.isEmpty()) {
            throw new UnknownEntityException(
                    String.format("Game '%s' not found", sessionId), 
                    GameEntity.class.getName()
            );
        }

        return games.get(0);
    }

    @Override
    @Transactional
    public GameEntity performMove(@NotNull String sessionId, 
                                  @NotNull String playerName, 
                                  @NotNull Integer position) {
        
        LOGGER.info("Performing move: gameId '{}', player '{}', position '{}'", 
                sessionId, 
                playerName, 
                position
        );
        
        var game = getBySessionId(sessionId);
        var player = playerService.getByName(playerName);
        
        if (!Objects.equals(game.getActivePlayer(), player)) {
            throw new IllegalStateException(String.format(
                    "Unable to perform move by inactive player ('%s')",
                    playerName
            ));
        }
        
        var symbol = Objects.equals(game.getPlayer1(), player) ? "X" : "O";
        try {
            // validate and set
            var board = boardService.setPosition(game.getBoard(), position, symbol);
            // update board
            game.setBoard(board);
            // evaluate board
            if (boardService.containsLine(board, symbol)) {
                game.setWinner(player);
                game.setStatus(GameStatus.FINISHED);
            } else {
                game.setStatus(GameStatus.RUNNING);
                // switch active player
                game.setActivePlayer(Objects.equals(game.getPlayer1(), player) 
                        ? game.getPlayer2()
                        : game.getPlayer1()
                );
            }
            
            // update game
            var updated = gameRepository.save(game);
            
            LOGGER.info("Performed move: gameId '{}', player '{}', position '{}'", 
                    sessionId, 
                    playerName, 
                    position
            );
            
            return updated;
        } catch (Exception e) {
            var message = String.format(
                    "Error while performing a move: sessionId '%s', player '%s', position '%d'",
                    sessionId, playerName, position
            );

            LOGGER.error(message, e);
        }

        game.setStatus(GameStatus.ERROR);
        return gameRepository.save(game);
    }
}