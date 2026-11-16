package chessphase2;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stores the complete Phase 2 game state independently of Swing.
 * Piece-specific chess movement validation is intentionally not applied.
 */
public final class GameState {
    private final ChessPiece[][] board = new ChessPiece[8][8];
    private final List<MoveRecord> history = new ArrayList<>();
    private ChessPiece.Side turn = ChessPiece.Side.WHITE;
    private ChessPiece.Side winner;

    /** Creates a new game with all pieces in their usual starting squares. */
    public GameState() { reset(); }

    /** Resets pieces, move history, captured pieces, and current player. */
    public void reset() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) board[row][col] = null;
        }
        history.clear();
        turn = ChessPiece.Side.WHITE;
        winner = null;
        ChessPiece.Type[] back = {
                ChessPiece.Type.ROOK, ChessPiece.Type.KNIGHT, ChessPiece.Type.BISHOP,
                ChessPiece.Type.QUEEN, ChessPiece.Type.KING, ChessPiece.Type.BISHOP,
                ChessPiece.Type.KNIGHT, ChessPiece.Type.ROOK
        };
        for (int col = 0; col < 8; col++) {
            board[0][col] = new ChessPiece(ChessPiece.Side.BLACK, back[col]);
            board[1][col] = new ChessPiece(ChessPiece.Side.BLACK, ChessPiece.Type.PAWN);
            board[6][col] = new ChessPiece(ChessPiece.Side.WHITE, ChessPiece.Type.PAWN);
            board[7][col] = new ChessPiece(ChessPiece.Side.WHITE, back[col]);
        }
    }

    /**
     * Gets the piece at a board square.
     * @param row zero-based board row
     * @param col zero-based board column
     * @return the piece, or null for an empty square
     */
    public ChessPiece getPiece(int row, int col) {
        requireSquare(row, col);
        return board[row][col];
    }

    /** @return the player who must move next */
    public ChessPiece.Side getTurn() { return turn; }

    /** @return the winner, or null while the game is still running */
    public ChessPiece.Side getWinner() { return winner; }

    /** @return an unmodifiable list of moves completed so far */
    public List<MoveRecord> getHistory() { return Collections.unmodifiableList(history); }

    /**
     * Moves the active player's piece to an empty square or a square occupied by
     * an opponent. Chess-specific movement restrictions are not checked.
     * @param fromRow starting row
     * @param fromCol starting column
     * @param toRow destination row
     * @param toCol destination column
     * @return a record of the completed move
     * @throws IllegalArgumentException for an empty start, wrong player, or own-piece capture
     */
    public MoveRecord move(int fromRow, int fromCol, int toRow, int toCol) {
        requireSquare(fromRow, fromCol);
        requireSquare(toRow, toCol);
        if (winner != null) throw new IllegalArgumentException("The game has already ended.");
        if (fromRow == toRow && fromCol == toCol) {
            throw new IllegalArgumentException("Choose a different destination square.");
        }
        ChessPiece piece = board[fromRow][fromCol];
        if (piece == null) throw new IllegalArgumentException("There is no piece on that square.");
        if (piece.getSide() != turn) throw new IllegalArgumentException("It is " + turn + "'s turn.");
        ChessPiece target = board[toRow][toCol];
        if (target != null && target.getSide() == piece.getSide()) {
            throw new IllegalArgumentException("You cannot capture your own piece.");
        }
        MoveRecord record = new MoveRecord(fromRow, fromCol, toRow, toCol, piece, target);
        board[toRow][toCol] = piece;
        board[fromRow][fromCol] = null;
        history.add(record);
        if (target != null && target.getType() == ChessPiece.Type.KING) {
            winner = piece.getSide();
        } else {
            turn = turn == ChessPiece.Side.WHITE ? ChessPiece.Side.BLACK : ChessPiece.Side.WHITE;
        }
        return record;
    }

    /**
     * Undoes the most recent move and restores any captured piece.
     * @return true if there was a move to undo; false otherwise
     */
    public boolean undo() {
        if (history.isEmpty()) return false;
        MoveRecord last = history.remove(history.size() - 1);
        board[last.getFromRow()][last.getFromCol()] = last.getMoved();
        board[last.getToRow()][last.getToCol()] = last.getCaptured();
        turn = last.getMoved().getSide();
        winner = null;
        return true;
    }

    /**
     * Saves the board, active player, winner, and move history to a text file.
     * @param file destination file path
     * @throws IOException if writing fails
     */
    public void save(Path file) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("CHESS_PHASE2_V1");
        lines.add("TURN " + turn.name());
        lines.add("WINNER " + (winner == null ? "NONE" : winner.name()));
        for (int row = 0; row < 8; row++) {
            StringBuilder line = new StringBuilder();
            for (int col = 0; col < 8; col++) {
                if (col > 0) line.append(' ');
                line.append(board[row][col] == null ? "--" : board[row][col].getCode());
            }
            lines.add(line.toString());
        }
        lines.add("HISTORY " + history.size());
        for (MoveRecord record : history) lines.add(record.toSaveLine());
        Files.write(file, lines, StandardCharsets.UTF_8);
    }

    /**
     * Loads a previously saved game, including the move history used by Undo.
     * @param file previously saved file path
     * @return a restored game state
     * @throws IOException for missing or unreadable files
     * @throws IllegalArgumentException for invalid save-file content
     */
    public static GameState load(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        if (lines.size() < 12 || !"CHESS_PHASE2_V1".equals(lines.get(0))) {
            throw new IllegalArgumentException("This is not a Phase 2 chess save file.");
        }
        GameState restored = new GameState();
        if (!lines.get(1).startsWith("TURN ") || !lines.get(2).startsWith("WINNER ")) {
            throw new IllegalArgumentException("Missing saved player information.");
        }
        restored.turn = ChessPiece.Side.valueOf(lines.get(1).substring(5));
        String winnerText = lines.get(2).substring(7);
        restored.winner = "NONE".equals(winnerText) ? null : ChessPiece.Side.valueOf(winnerText);
        for (int row = 0; row < 8; row++) {
            String[] cells = lines.get(row + 3).trim().split("\\s+");
            if (cells.length != 8) throw new IllegalArgumentException("Invalid saved board row.");
            for (int col = 0; col < 8; col++) restored.board[row][col] = ChessPiece.fromCode(cells[col]);
        }
        String countLine = lines.get(11);
        if (!countLine.startsWith("HISTORY ")) {
            throw new IllegalArgumentException("Missing saved history.");
        }
        int count = Integer.parseInt(countLine.substring(8));
        if (count < 0 || lines.size() != 12 + count) {
            throw new IllegalArgumentException("Incorrect saved move count.");
        }
        restored.history.clear();
        for (int i = 0; i < count; i++) {
            restored.history.add(MoveRecord.fromSaveLine(lines.get(12 + i)));
        }
        return restored;
    }

    private static void requireSquare(int row, int col) {
        if (row < 0 || row >= 8 || col < 0 || col >= 8) {
            throw new IllegalArgumentException("Square must be inside the 8x8 chessboard.");
        }
    }
}