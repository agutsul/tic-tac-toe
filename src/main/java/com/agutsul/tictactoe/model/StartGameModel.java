package com.agutsul.tictactoe.model;

import jakarta.validation.constraints.NotBlank;

public class StartGameModel {

    @NotBlank
    private String playerName1;
    @NotBlank
    private String playerName2;

    public String getPlayerName1() {
        return playerName1;
    }
    public void setPlayerName1(String playerName1) {
        this.playerName1 = playerName1;
    }
    public String getPlayerName2() {
        return playerName2;
    }
    public void setPlayerName2(String playerName2) {
        this.playerName2 = playerName2;
    }
}