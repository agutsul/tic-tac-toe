package com.agutsul.tictactoe.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.agutsul.tictactoe.entity.PlayerEntity;
import com.agutsul.tictactoe.model.PlayerModel;
import com.agutsul.tictactoe.service.PlayerService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(PlayerController.class)
public class PlayerControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PlayerService service;

    @Test
    void testCreateFromService() throws Exception {
        when(service.create(any()))
            .thenAnswer(inv -> { 
                var player = new PlayerEntity();
                player.setName(inv.getArgument(0));
                return player;
            });
        
        var playerModel = new PlayerModel();
        playerModel.setPlayerName("test");
        
        this.mockMvc.perform(post("/players/")
                                .content(objectMapper.writeValueAsString(playerModel))
                                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isOk());
    }
    
    @Test
    void testCreateWithServiceException() throws Exception {
        when(service.create(any()))
            .thenThrow(new IllegalStateException("test"));
        
        var playerModel = new PlayerModel();
        playerModel.setPlayerName("test");
        
        var content = this.mockMvc.perform(post("/players/")
                                .content(objectMapper.writeValueAsString(playerModel))
                                .contentType(MediaType.APPLICATION_JSON)
            )
            .andDo(print())
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();
        
        assertEquals("Player creation failed: test", content);
    }
}