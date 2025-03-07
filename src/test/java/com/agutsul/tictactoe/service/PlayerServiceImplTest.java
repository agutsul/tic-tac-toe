package com.agutsul.tictactoe.service;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.sqm.UnknownEntityException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.agutsul.tictactoe.entity.PlayerEntity;
import com.agutsul.tictactoe.repository.PlayerRepository;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceImplTest {

    @Mock
    PlayerRepository playerRepository;
    
    @InjectMocks
    PlayerServiceImpl playerService;
    
    @Test
    void testGetByNameThrowsException() {
        when(playerRepository.findByName(anyString()))
            .thenReturn(emptyList());
    
        var thrown = assertThrows(
                UnknownEntityException.class, 
                () -> playerService.getByName(StringUtils.EMPTY)
        );
        
        assertEquals("Player with name '' not found", thrown.getMessage());
    }
    
    @Test
    void testGetByName() {
        var player = mock(PlayerEntity.class);
        when(playerRepository.findByName(anyString()))
            .thenReturn(List.of(player));

        assertEquals(player, playerService.getByName(StringUtils.EMPTY));
    }
    
    @Test
    void testCreateThrowsException() {
        when(playerRepository.findByName(anyString()))
            .thenReturn(List.of(mock(PlayerEntity.class)));

        var thrown = assertThrows(
                IllegalArgumentException.class, 
                () -> playerService.create(StringUtils.EMPTY)
        );
        
        assertEquals("Player name '' already in use", thrown.getMessage());
    }
    
    @Test
    void testCreate() {
        when(playerRepository.findByName(anyString()))
            .thenReturn(emptyList());
        
        when(playerRepository.save(any()))
            .then(inv -> inv.getArgument(0));

        var playerName = "test";
        var player = playerService.create(playerName);
        
        assertEquals(playerName, player.getName());
    }
}