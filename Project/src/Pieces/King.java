package Pieces;

import board.Board;
import board.Position;

/** A king that moves one square in any direction. */
public class King extends Piece {
    /** Creates a king. */
    public King(Color color, Position position) { super(color, position); }

    /** Checks one-square moves, without checking for check or castling. */
    @Override
    public boolean canMove(Board board, Position target) {
        int dr = Math.abs(target.getRow() - getPosition().getRow());
        int dc = Math.abs(target.getColumn() - getPosition().getColumn());
        return dr <= 1 && dc <= 1 && (dr + dc > 0);
    }

    /** Returns the king symbol letter. */
    @Override
    public String getLetter() { return "K"; }
}