package com.agutsul.tictactoe.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ActionModel {

    @NotBlank
    private String playerName;

    @Min(1)
    @Max(9)
    @NotNull
    private Integer position;

    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String userName) {
        this.playerName = userName;
    }
    public Integer getPosition() {
        return position;
    }
    public void setPosition(Integer position) {
        this.position = position;
    }
}