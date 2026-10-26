package Pieces;

import Board.Board;
import Board.Position;

/** A knight that moves in an L shape and can jump over pieces. */
public class Knight extends Piece {
    /** Creates a knight. */
    public Knight(Color color, Position position) { super(color, position); }

    /** Checks whether the target is an L-shaped move away. */
    @Override
    public boolean canMove(Board board, Position target) {
        int dr = Math.abs(target.getRow() - getPosition().getRow());
        int dc = Math.abs(target.getColumn() - getPosition().getColumn());
        return (dr == 2 && dc == 1) || (dr == 1 && dc == 2);
    }

    /** Returns the knight symbol letter. */
    @Override
    public String getLetter() { return "N"; }
}