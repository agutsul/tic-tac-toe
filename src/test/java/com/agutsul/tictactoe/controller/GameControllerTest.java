package com.agutsul.tictactoe.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.agutsul.tictactoe.entity.GameEntity;
import com.agutsul.tictactoe.entity.GameStatus;
import com.agutsul.tictactoe.entity.PlayerEntity;
import com.agutsul.tictactoe.model.ActionModel;
import com.agutsul.tictactoe.model.GameModel;
import com.agutsul.tictactoe.model.StartGameModel;
import com.agutsul.tictactoe.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(GameController.class)
public class GameControllerTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    GameService service;

    @Test
    void testStartGame() throws Exception {
        when(service.createGame(anyString(), anyString()))
            .thenAnswer(inv -> createGame(
                    inv.getArgument(0), 
                    inv.getArgument(1), 
                    GameStatus.FINISHED, 
                    "XXX      "
            ));

        var model = new StartGameModel();
        model.setPlayerName1("test1");
        model.setPlayerName2("test2");

        var content = this.mockMvc.perform(post("/games/")
                            .content(objectMapper.writeValueAsString(model))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

        var response = objectMapper.readValue(content, GameModel.class);

        assertNotNull(response.getSessionId());
        assertEquals(GameModel.Status.DRAW, response.getStatus());
        assertNotNull(response.getBoard());
    }

    @Test
    void testGetGameById() throws Exception {
        var playerName1 = "test1";
        var playerName2 = "test2";

        when(service.getBySessionId(anyString()))
            .thenAnswer(inv -> {
                var game = createGame(
                        playerName1, 
                        playerName2, 
                        GameStatus.RUNNING, 
                        "XXX      "
                );

                game.setSessionId(inv.getArgument(0));
                return game;
            });
        
        var sessionId = UUID.randomUUID().toString();
        var content = this.mockMvc.perform(get("/games/{gameId}", sessionId))
                                    .andDo(print())
                                    .andExpect(status().isOk())
                                    .andReturn()
                                    .getResponse()
                                    .getContentAsString();

        var response = objectMapper.readValue(content, GameModel.class);

        assertEquals(sessionId, response.getSessionId());
        assertEquals(GameModel.Status.IN_PROGRESS, response.getStatus());
        assertNotNull(response.getBoard());
    }

    @Test
    void testPlayerMove() throws Exception {
        var playerName1 = "test1";
        var playerName2 = "test2";

        when(service.performMove(anyString(), anyString(), any()))
            .thenAnswer(inv -> {
                var game = createGame(
                        inv.getArgument(1), 
                        playerName2, 
                        GameStatus.RUNNING, 
                        "XXX      "
                );

                game.setSessionId(inv.getArgument(0));
                return game;
            });

        var sessionId = UUID.randomUUID().toString();

        var model = new ActionModel();
        model.setPlayerName(playerName1);
        model.setPosition(1);

        var content = this.mockMvc.perform(post("/games/{gameId}/move", sessionId)
                            .content(objectMapper.writeValueAsString(model))
                            .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();

        var response = objectMapper.readValue(content, GameModel.class);

        assertEquals(sessionId, response.getSessionId());
        assertEquals(GameModel.Status.IN_PROGRESS, response.getStatus());
        assertNotNull(response.getBoard());
    }

    private static PlayerEntity createPlayer(String playerName) {
        var player = new PlayerEntity();
        player.setName(playerName);
        return player;
    }

    private static GameEntity createGame(String playerName1, String playerName2, 
                                         GameStatus gameStatus, String board) {

        var player1 = createPlayer(playerName1);

        var gameEntity = new GameEntity();

        gameEntity.setPlayer1(player1);
        gameEntity.setPlayer2(createPlayer(playerName2));
        gameEntity.setSessionId(UUID.randomUUID().toString());
        gameEntity.setStatus(gameStatus);
        gameEntity.setActivePlayer(player1);
        gameEntity.setBoard(board);

        return gameEntity;
    }
}