package com.agutsul.tictactoe.model;

public class GameModel {

    public enum Status {
        IN_PROGRESS,
        WIN,
        DRAW,
        DEFEAT,
        ERROR
    }

    private String sessionId;
    private String board;
    private Status status;
    private String activePlayer;

    public String getSessionId() {
        return sessionId;
    }
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    public String getBoard() {
        return board;
    }
    public void setBoard(String board) {
        this.board = board;
    }
    public String getActivePlayer() {
        return activePlayer;
    }
    public void setActivePlayer(String activePlayer) {
        this.activePlayer = activePlayer;
    }
    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }
}