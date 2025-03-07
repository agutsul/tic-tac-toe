package com.agutsul.tictactoe.service;

public interface BoardService {
    String DEFAULT_BOARD = "         ";

    String setPosition(String board, Integer position, String symbol);

    boolean containsLine(String board, String symbol);
}