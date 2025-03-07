package com.agutsul.tictactoe.service;

import static org.slf4j.LoggerFactory.getLogger;

import java.util.List;

import org.hibernate.query.sqm.UnknownEntityException;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.agutsul.tictactoe.entity.PlayerEntity;
import com.agutsul.tictactoe.repository.PlayerRepository;

import jakarta.validation.constraints.NotNull;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final Logger LOGGER = getLogger(PlayerServiceImpl.class);
    
    private final PlayerRepository playerRepository;

    public PlayerServiceImpl(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerEntity getById(@NotNull Long id) {
        LOGGER.info("Get player by id '{}'", id);
        return playerRepository.getReferenceById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PlayerEntity> findByName(@NotNull String name) {
        LOGGER.info("Find player by name '{}'", name);
        return playerRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerEntity getByName(@NotNull String name) {
        LOGGER.info("Get player by name '{}'", name);

        var players = playerRepository.findByName(name);
        if (players.isEmpty()) {
            throw new UnknownEntityException(
                    String.format("Player with name '%s' not found", name),
                    PlayerEntity.class.getName()
            );
        }

        return players.get(0);
    }
    
    @Override
    @Transactional
    public PlayerEntity create(@NotNull String name) {
        LOGGER.info("Creating player: '{}'", name);

        var players = playerRepository.findByName(name);
        if (!players.isEmpty()) {
            throw new IllegalArgumentException(
                    String.format("Player name '%s' already in use", name)
            );
        }

        var player = new PlayerEntity();
        player.setName(name);

        var saved = playerRepository.save(player);
        LOGGER.info("Created player: '{}'", name);

        return saved;
    }
}