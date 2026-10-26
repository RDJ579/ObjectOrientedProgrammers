package utils;

import board.Position;

/** Converts console move input into board positions. */
public class InputParser {
    /** Returns the starting and ending positions, or null for bad input. */
    public static Position[] parseMove(String text) {
        if (text == null) return null;
        String[] parts = text.trim().toUpperCase().split("\\s+");
        if (parts.length != 2) return null;
        if (!parts[0].matches("[A-H][1-8]") || !parts[1].matches("[A-H][1-8]")) {
            return null;
        }
        return new Position[] {Position.fromChess(parts[0]), Position.fromChess(parts[1])};
    }
}