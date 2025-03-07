package com.agutsul.tictactoe.service;

import static org.apache.commons.lang3.StringUtils.repeat;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Objects;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class BoardServiceImpl implements BoardService {

    private static final Logger LOGGER = getLogger(BoardServiceImpl.class);

    @Override
    public String setPosition(String board, Integer position, String symbol) {
        LOGGER.info("Set '{}' position '{}' on board '{}'", symbol, position, board);

        var symbols = board.toCharArray();

        var index = position - 1;
        if (index < 0 || index >= symbols.length) {
            throw new IllegalArgumentException(String.format(
                    "Invalid position '%s'",
                    position
            ));
        }

        if (symbols[index] != ' ') {
            throw new IllegalArgumentException(String.format(
                    "Unable to set already filled position '%s'",
                    position
            ));
        }

        symbols[index] = symbol.charAt(0);

        var updated = String.valueOf(symbols);

        LOGGER.info("New board '{}'", updated);
        return updated;
    }

    @Override
    public boolean containsLine(String board, String symbol) {
        LOGGER.info("Check board '{}' for line of '{}'", board, symbol);

        var symbols = board.toCharArray();

        var lines = new String[] {
                line(symbols[0], symbols[1], symbols[2]), // horizontal line#1
                line(symbols[3], symbols[4], symbols[5]), // horizontal line#2
                line(symbols[6], symbols[7], symbols[8]), // horizontal line#3
                line(symbols[0], symbols[3], symbols[6]), // vertical line#1
                line(symbols[1], symbols[4], symbols[7]), // vertical line#2
                line(symbols[2], symbols[5], symbols[8]), // vertical line#3
                line(symbols[0], symbols[4], symbols[8]), // diagonal line#1
                line(symbols[6], symbols[4], symbols[2])  // diagonal line#2
        };

        var pattern = repeat(symbol, 3);
        var winnerLine = Stream.of(lines)
            .filter(line -> Objects.equals(line, pattern))
            .findFirst();

        return winnerLine.isPresent();
    }

    private static String line(char c1, char c2, char c3) {
        return String.format("%c%c%c", c1, c2, c3);
    }
}