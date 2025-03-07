package com.agutsul.tictactoe.service;

import com.agutsul.tictactoe.entity.GameEntity;

public interface GameService {
    GameEntity createGame(String playerName1, String playerName2);
    GameEntity getBySessionId(String sessionId);
    GameEntity performMove(String sessionId, String playerName, Integer position);
}