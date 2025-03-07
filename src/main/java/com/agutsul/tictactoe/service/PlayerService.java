package com.agutsul.tictactoe.service;

import java.util.List;

import com.agutsul.tictactoe.entity.PlayerEntity;

public interface PlayerService {

    String BOT_NAME = "PlayerBot";

    PlayerEntity create(String name);

    PlayerEntity getById(Long id);
    PlayerEntity getByName(String name);

    List<PlayerEntity> findByName(String name);
}