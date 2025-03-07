package com.agutsul.tictactoe.service;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.sqm.UnknownEntityException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.agutsul.tictactoe.entity.GameEntity;
import com.agutsul.tictactoe.entity.GameStatus;
import com.agutsul.tictactoe.entity.PlayerEntity;
import com.agutsul.tictactoe.repository.GameRepository;

@ExtendWith(MockitoExtension.class)
public class GameServiceImplTest {

    @Mock
    GameRepository gameRepository;
    @Mock
    BoardService   boardService;
    @Mock
    PlayerService  playerService;
    
    @InjectMocks
    GameServiceImpl gameService;
    
    @Test
    void testCreateGame() {
        var player1 = mock(PlayerEntity.class);
        var player2 = mock(PlayerEntity.class);
        
        when(playerService.getByName(anyString()))
            .thenAnswer(inv -> "player1".equals(inv.getArgument(0)) ? player1 : player2);
        
        when(gameRepository.save(any()))
            .then(inv -> inv.getArgument(0));
        
        var game = gameService.createGame("player1", "player2");

        assertNotNull(game);
        
        assertNotNull(game.getSessionId());
        assertEquals(BoardService.DEFAULT_BOARD, game.getBoard());
        assertEquals(GameStatus.RUNNING, game.getStatus());
        assertNull(game.getWinner());
        
        assertEquals(player1, game.getPlayer1());
        assertEquals(player2, game.getPlayer2());
        assertEquals(player1, game.getActivePlayer());
        
        verify(playerService, times(2)).getByName(any());
    }
    
    @Test
    void testGetBySessionIdThrowsException() {
        when(gameRepository.findBySessionId(anyString()))
            .thenReturn(emptyList());
        
        var thrown = assertThrows(
                UnknownEntityException.class, 
                () -> gameService.getBySessionId(StringUtils.EMPTY)
        );
        
        assertEquals("Game '' not found", thrown.getMessage());
    }
    
    @Test
    void testGetBySessionId() {
        var game = mock(GameEntity.class);
        
        when(gameRepository.findBySessionId(anyString()))
            .thenReturn(List.of(game));
        
        assertEquals(game, gameService.getBySessionId(StringUtils.EMPTY));
    }
    
    @Test
    void testPerformMoveByInactivePlayer() {
        var player1 = mock(PlayerEntity.class);
        var player2 = mock(PlayerEntity.class);
        
        var game = mock(GameEntity.class);

        when(game.getActivePlayer())
            .thenReturn(player1);

        when(gameRepository.findBySessionId(anyString()))
            .thenReturn(List.of(game));
        
        when(playerService.getByName(anyString()))
            .thenReturn(player2);
        
        var thrown = assertThrows(
                IllegalStateException.class,
                () -> gameService.performMove(StringUtils.EMPTY, "test", 1)
        );
        
        assertEquals("Unable to perform move by inactive player ('test')", thrown.getMessage());
    }
    
    @Test
    void testPerformMoveWithLineFound() {
        var board = "XXX      ";
        
        var player1 = mock(PlayerEntity.class);
        var player2 = mock(PlayerEntity.class);
        
        var game = new GameEntity();
        game.setPlayer1(player1);
        game.setPlayer2(player2);
        game.setActivePlayer(player1);
        game.setBoard(board);
        
        var gameMock = spy(game);

        doCallRealMethod()
            .when(gameMock).getActivePlayer();
        doCallRealMethod()
            .when(gameMock).getBoard();
        doCallRealMethod()
            .when(gameMock).setBoard(anyString());

        when(gameRepository.findBySessionId(anyString()))
            .thenReturn(List.of(gameMock));
        
        when(gameRepository.save(any()))
            .then(inv -> inv.getArgument(0));
        
        when(playerService.getByName(anyString()))
            .thenReturn(player1);
        
        when(boardService.setPosition(anyString(), any(), anyString()))
            .thenReturn(board);
        
        when(boardService.containsLine(anyString(), anyString()))
            .thenReturn(true);
        
        var gameEntity = gameService.performMove(StringUtils.EMPTY, "test", 1);
        
        assertEquals(board, gameEntity.getBoard());
        assertEquals(GameStatus.FINISHED, gameEntity.getStatus());
        assertEquals(player1, gameEntity.getWinner());
    }
    
    @Test
    void testPerformMoveWithLineNotFound() {
        var board = "X X    X ";
        
        var player1 = mock(PlayerEntity.class);
        var player2 = mock(PlayerEntity.class);
        
        var game = new GameEntity();
        game.setPlayer1(player1);
        game.setPlayer2(player2);
        game.setActivePlayer(player1);
        game.setBoard(board);
        
        var gameMock = spy(game);

        doCallRealMethod()
            .when(gameMock).getActivePlayer();
        doCallRealMethod()
            .when(gameMock).setActivePlayer(any());
        doCallRealMethod()
            .when(gameMock).getBoard();
        doCallRealMethod()
            .when(gameMock).setBoard(anyString());

        when(gameRepository.findBySessionId(anyString()))
            .thenReturn(List.of(gameMock));
        
        when(gameRepository.save(any()))
            .then(inv -> inv.getArgument(0));
        
        when(playerService.getByName(anyString()))
            .thenReturn(player1);
        
        when(boardService.setPosition(anyString(), any(), anyString()))
            .thenReturn(board);
        
        when(boardService.containsLine(anyString(), anyString()))
            .thenReturn(false);
        
        var gameEntity = gameService.performMove(StringUtils.EMPTY, "test", 1);
        
        assertEquals(board, gameEntity.getBoard());
        assertEquals(GameStatus.RUNNING, gameEntity.getStatus());
        assertNull(gameEntity.getWinner());
        assertEquals(player2, gameEntity.getActivePlayer());
    }
    
    @Test
    void testPerformMoveWithException() {
        var board = "X X    X ";
        
        var player1 = mock(PlayerEntity.class);
        var player2 = mock(PlayerEntity.class);
        
        var game = new GameEntity();
        game.setPlayer1(player1);
        game.setPlayer2(player2);
        game.setActivePlayer(player1);
        game.setBoard(board);
        
        var gameMock = spy(game);
        when(gameRepository.findBySessionId(anyString()))
            .thenReturn(List.of(gameMock));
    
        when(playerService.getByName(anyString()))
            .thenReturn(player1);
    
        when(gameRepository.save(any()))
            .then(inv -> inv.getArgument(0));
        
        when(boardService.setPosition(anyString(), any(), anyString()))
            .thenThrow(new IllegalStateException("test"));
        
        var gameEntity = gameService.performMove(StringUtils.EMPTY, "test", 1);
        assertEquals(GameStatus.ERROR, gameEntity.getStatus());
    }
}