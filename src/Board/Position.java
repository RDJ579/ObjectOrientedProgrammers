package board;

/** Stores a square on an 8 by 8 chessboard. */
public class Position {
    /** The row (0 is rank 8; 7 is rank 1). */
    private final int row;
    /** The column (0 is file A; 7 is file H). */
    private final int column;

    /** Creates a position using array row and column numbers. */
    public Position(int row, int column) {
        if (row < 0 || row > 7 || column < 0 || column > 7) {
            throw new IllegalArgumentException("Position is outside the board");
        }
        this.row = row;
        this.column = column;
    }

    /** Returns the zero-based row. */
    public int getRow() { return row; }

    /** Returns the zero-based column. */
    public int getColumn() { return column; }

    /** Converts a square such as E2 into a Position. */
    public static Position fromChess(String square) {
        String s = square.toUpperCase();
        if (!s.matches("[A-H][1-8]")) {
            throw new IllegalArgumentException("Invalid square: " + square);
        }
        return new Position(8 - (s.charAt(1) - '0'), s.charAt(0) - 'A');
    }

    /** Returns this position as a square such as E2. */
    @Override
    public String toString() {
        return "" + (char) ('A' + column) + (8 - row);
    }
}