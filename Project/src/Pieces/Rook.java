package Pieces;

import Board.Board;
import Board.Position;

/** A rook that moves horizontally or vertically. */
public class Rook extends Piece {
    /** Creates a rook. */
    public Rook(Color color, Position position) { super(color, position); }

    /** Checks rook movement and verifies that the path is clear. */
    @Override
    public boolean canMove(Board board, Position target) {
        boolean straight = getPosition().getRow() == target.getRow()
                || getPosition().getColumn() == target.getColumn();
        return straight && board.isPathClear(getPosition(), target);
    }

    /** Returns the rook symbol letter. */
    @Override
    public String getLetter() { return "R"; }
}