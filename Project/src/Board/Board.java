package Board;

import Pieces.*;

/** Stores the pieces, sets up the board, and applies basic legal moves. */
public class Board {
    /** The 8 by 8 grid of pieces. Empty squares contain null. */
    private final Piece[][] squares = new Piece[8][8];

    /** Creates and initializes the standard starting position. */
    public Board() { setup(); }

    /** Places the pieces in their starting squares. */
    public void setup() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) squares[row][col] = null;
        }
        for (int col = 0; col < 8; col++) {
            place(new Pawn(Color.BLACK, new Position(1, col)));
            place(new Pawn(Color.WHITE, new Position(6, col)));
        }
        setupBackRow(Color.BLACK, 0);
        setupBackRow(Color.WHITE, 7);
    }

    /** Puts the major pieces on one player's back row. */
    private void setupBackRow(Color color, int row) {
        place(new Rook(color, new Position(row, 0)));
        place(new Knight(color, new Position(row, 1)));
        place(new Bishop(color, new Position(row, 2)));
        place(new Queen(color, new Position(row, 3)));
        place(new King(color, new Position(row, 4)));
        place(new Bishop(color, new Position(row, 5)));
        place(new Knight(color, new Position(row, 6)));
        place(new Rook(color, new Position(row, 7)));
    }

    /** Places one piece onto the board. */
    private void place(Piece piece) {
        Position p = piece.getPosition();
        squares[p.getRow()][p.getColumn()] = piece;
    }

    /** Returns the piece at a given square, or null if it is empty. */
    public Piece getPiece(Position position) {
        return squares[position.getRow()][position.getColumn()];
    }

    /** Returns true if all squares strictly between two aligned squares are empty. */
    public boolean isPathClear(Position from, Position to) {
        if (from.getRow() == to.getRow() && from.getColumn() == to.getColumn()) {
            return false;
        }
        int dr = Integer.compare(to.getRow(), from.getRow());
        int dc = Integer.compare(to.getColumn(), from.getColumn());
        int row = from.getRow() + dr;
        int col = from.getColumn() + dc;
        while (row != to.getRow() || col != to.getColumn()) {
            if (squares[row][col] != null) return false;
            row += dr;
            col += dc;
        }
        return true;
    }

    /** Attempts a move; returns null on success or an error message otherwise. */
    public String movePiece(Position from, Position to, Color turn) {
        Piece moving = getPiece(from);
        Piece target = getPiece(to);
        if (moving == null) return "There is no piece on " + from + ".";
        if (moving.getColor() != turn) return "It is " + turn.displayName() + "'s turn.";
        if (from.getRow() == to.getRow() && from.getColumn() == to.getColumn())
            return "Choose a different destination square.";
        if (target != null && target.getColor() == turn)
            return "You cannot capture your own piece.";
        if (target instanceof King)
            return "Capturing the king is not supported; checkmate is not implemented.";
        if (!moving.canMove(this, to)) return "That piece cannot move there.";
        squares[to.getRow()][to.getColumn()] = moving;
        squares[from.getRow()][from.getColumn()] = null;
        moving.setPosition(to);
        return null;
    }

    /** Prints the current board with ranks and files. */
    public void display() {
        System.out.println("    A  B  C  D  E  F  G  H");
        for (int row = 0; row < 8; row++) {
            System.out.print((8 - row) + "  ");
            for (int col = 0; col < 8; col++) {
                Piece piece = squares[row][col];
                System.out.print((piece == null ? "##" : piece.getSymbol()) + " ");
            }
            System.out.println();
        }
    }
}