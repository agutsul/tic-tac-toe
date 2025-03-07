package com.agutsul.tictactoe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BoardServiceImplTest {

    @Test
    void testSetPositionWithEmptyBoard() {
        var boardService = new BoardServiceImpl();

        var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> boardService.setPosition(StringUtils.EMPTY, 1, "X")
        );

        assertEquals("Invalid position '1'", thrown.getMessage());
    }
    
    @Test
    void testSetAlreadyFilledPosition() {
        var boardService = new BoardServiceImpl();

        var thrown = assertThrows(
                IllegalArgumentException.class,
                () -> boardService.setPosition("X        ", 1, "X")
        );

        assertEquals("Unable to set already filled position '1'", thrown.getMessage());
    }
    
    @Test
    void testSetPositionWithDefaultBoard() {
        var boardService = new BoardServiceImpl();
        var board = boardService.setPosition(BoardService.DEFAULT_BOARD, 1, "X");
        assertEquals("X        ", board);
    }
    
    @Test
    void testContainsLineOnDefaultBoard() {
        var boardService = new BoardServiceImpl();
        assertFalse(boardService.containsLine(BoardService.DEFAULT_BOARD, "X"));
    }
    
    @Test
    void testContainsLineOnNonDefaultBoard() {
        var boardService = new BoardServiceImpl();
        assertFalse(boardService.containsLine("X  OO XX ", "X"));
    }
    
    @Test
    void testContainsLine() {
        var boardService = new BoardServiceImpl();

        assertTrue(boardService.containsLine("X  XO XO ", "X"));
        assertTrue(boardService.containsLine("XXX OO O ", "X"));
        assertTrue(boardService.containsLine("X O XO  X", "X"));
    }
}