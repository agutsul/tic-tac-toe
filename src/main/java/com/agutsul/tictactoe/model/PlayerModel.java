package com.agutsul.tictactoe.model;

import jakarta.validation.constraints.NotBlank;

public class PlayerModel {

    @NotBlank
    private String playerName;

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}