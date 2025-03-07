package com.agutsul.tictactoe.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "games")
public class GameEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(nullable = false, unique = true, updatable = false)
    private String sessionId;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "player1")
    private PlayerEntity player1;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "player2")
    private PlayerEntity player2;
    
    @NotNull
    @Column(nullable = false)
    private String board;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "active_player")
    private PlayerEntity activePlayer;
    
    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private GameStatus status;
    
    @ManyToOne
    @JoinColumn(name = "winner_player")
    private PlayerEntity winner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public PlayerEntity getPlayer1() {
        return player1;
    }

    public void setPlayer1(PlayerEntity player1) {
        this.player1 = player1;
    }

    public PlayerEntity getPlayer2() {
        return player2;
    }

    public void setPlayer2(PlayerEntity player2) {
        this.player2 = player2;
    }

    public PlayerEntity getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(PlayerEntity activePlayer) {
        this.activePlayer = activePlayer;
    }

    public PlayerEntity getWinner() {
        return winner;
    }

    public void setWinner(PlayerEntity winner) {
        this.winner = winner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof GameEntity))
            return false;

        var game = (GameEntity) o;
        return Objects.equals(this.id, game.id)
                && Objects.equals(this.sessionId, game.sessionId)
                && Objects.equals(this.player1,   game.player1)
                && Objects.equals(this.player2,   game.player2)
                && Objects.equals(this.board,     game.board)
                && Objects.equals(this.status,    game.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id, this.sessionId, 
                this.player1, this.player2, this.board, this.status);
    }

    @Override
    public String toString() {
        return String.format("Game{id=%d,sessionId=%s,player1=%s,player2=%s,status=%s}", 
                this.id, this.sessionId, this.player1, this.player2, this.status
        );
    }
}