package com.agutsul.tictactoe.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.agutsul.tictactoe.model.PlayerModel;
import com.agutsul.tictactoe.service.PlayerService;

import jakarta.validation.Valid;

@RestController
class PlayerController {

    private final PlayerService playerService;

    PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/players/")
    public ResponseEntity<?> createPlayer(@RequestBody @Valid PlayerModel model) {
        try {
            playerService.create(model.getPlayerName());
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(String.format(
                    "Player creation failed: %s", 
                    e.getMessage()
            ));
        }
    }
}
