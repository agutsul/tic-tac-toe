package com.agutsul.tictactoe.repository;

import com.agutsul.tictactoe.entity.GameEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, Long> {
    List<GameEntity> findBySessionId(String sessionId);
}