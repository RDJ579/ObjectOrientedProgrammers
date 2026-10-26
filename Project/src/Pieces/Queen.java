package Pieces;

import Board.Board;
import Board.Position;

/** A queen that moves straight or diagonally. */
public class Queen extends Piece {
    /** Creates a queen. */
    public Queen(Color color, Position position) { super(color, position); }

    /** Checks queen movement and verifies that the path is clear. */
    @Override
    public boolean canMove(Board board, Position target) {
        int dr = Math.abs(target.getRow() - getPosition().getRow());
        int dc = Math.abs(target.getColumn() - getPosition().getColumn());
        return (dr == 0 || dc == 0 || dr == dc)
                && board.isPathClear(getPosition(), target);
    }

    /** Returns the queen symbol letter. */
    @Override
    public String getLetter() { return "Q"; }
}