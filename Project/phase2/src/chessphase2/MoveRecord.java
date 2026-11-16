package Project.phase2.src.chessphase2;

/** Record of a completed move, including any captured piece for Undo. */
public final class MoveRecord {
    private final int fromRow;
    private final int fromCol;
    private final int toRow;
    private final int toCol;
    private final ChessPiece moved;
    private final ChessPiece captured;

    /**
     * Creates a completed move record.
     * @param fromRow starting row, zero-based
     * @param fromCol starting column, zero-based
     * @param toRow destination row, zero-based
     * @param toCol destination column, zero-based
     * @param moved piece that moved
     * @param captured opponent piece that was captured, or null
     */
    public MoveRecord(int fromRow, int fromCol, int toRow, int toCol,
                      ChessPiece moved, ChessPiece captured) {
        if (moved == null) throw new IllegalArgumentException("Moved piece cannot be null.");
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.moved = moved;
        this.captured = captured;
    }

    /** @return the starting row */
    public int getFromRow() { return fromRow; }

    /** @return the starting column */
    public int getFromCol() { return fromCol; }

    /** @return the destination row */
    public int getToRow() { return toRow; }

    /** @return the destination column */
    public int getToCol() { return toCol; }

    /** @return the moved piece */
    public ChessPiece getMoved() { return moved; }

    /** @return the captured piece, or null */
    public ChessPiece getCaptured() { return captured; }

    /** @return human-readable notation for the move history panel */
    public String describe() {
        String result = moved.getName() + " " + squareName(fromRow, fromCol) +
                " → " + squareName(toRow, toCol);
        if (captured != null) result += " (captured " + captured.getName() + ")";
        return result;
    }

    /**
     * Converts a board index to chess notation.
     * @param row row from 0 to 7
     * @param col column from 0 to 7
     * @return coordinate such as E2
     */
    public static String squareName(int row, int col) {
        return "" + (char) ('A' + col) + (8 - row);
    }

    /** @return a single line suitable for the save file */
    public String toSaveLine() {
        return fromRow + " " + fromCol + " " + toRow + " " + toCol + " " +
                moved.getCode() + " " + (captured == null ? "--" : captured.getCode());
    }

    /**
     * Recreates one move from a save-file line.
     * @param line line containing six space-separated fields
     * @return reconstructed move record
     */
    public static MoveRecord fromSaveLine(String line) {
        String[] parts = line.trim().split("\\s+");
        if (parts.length != 6) throw new IllegalArgumentException("Invalid move record.");
        int r1 = Integer.parseInt(parts[0]);
        int c1 = Integer.parseInt(parts[1]);
        int r2 = Integer.parseInt(parts[2]);
        int c2 = Integer.parseInt(parts[3]);
        if (r1 < 0 || r1 > 7 || c1 < 0 || c1 > 7 ||
                r2 < 0 || r2 > 7 || c2 < 0 || c2 > 7) {
            throw new IllegalArgumentException("Invalid saved move coordinates.");
        }
        return new MoveRecord(r1, c1, r2, c2,
                ChessPiece.fromCode(parts[4]), ChessPiece.fromCode(parts[5]));
    }
}
