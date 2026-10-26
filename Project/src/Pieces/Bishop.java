package Pieces;

import Board.Board;
import Board.Position;

/** A bishop that moves diagonally. */
public class Bishop extends Piece {
    /** Creates a bishop. */
    public Bishop(Color color, Position position) { super(color, position); }

    /** Checks diagonal movement and verifies that the path is clear. */
    @Override
    public boolean canMove(Board board, Position target) {
        int dr = Math.abs(target.getRow() - getPosition().getRow());
        int dc = Math.abs(target.getColumn() - getPosition().getColumn());
        return dr == dc && board.isPathClear(getPosition(), target);
    }

    /** Returns the bishop symbol letter. */
    @Override
    public String getLetter() { return "B"; }
}