package Pieces;

import Board.Board;
import Board.Position;

/** A pawn that moves forward and captures diagonally. */
public class Pawn extends Piece {
    /** Creates a pawn. */
    public Pawn(Color color, Position position) { super(color, position); }

    /** Checks basic pawn movement, without en passant or promotion. */
    @Override
    public boolean canMove(Board board, Position target) {
        int dr = target.getRow() - getPosition().getRow();
        int dc = target.getColumn() - getPosition().getColumn();
        int direction = getColor() == Color.WHITE ? -1 : 1;
        int startRow = getColor() == Color.WHITE ? 6 : 1;

        if (dc == 0 && dr == direction && board.getPiece(target) == null) return true;
        if (dc == 0 && dr == 2 * direction && getPosition().getRow() == startRow
                && board.getPiece(target) == null
                && board.getPiece(new Position(getPosition().getRow() + direction,
                        getPosition().getColumn())) == null) return true;
        return Math.abs(dc) == 1 && dr == direction && board.getPiece(target) != null
                && board.getPiece(target).getColor() != getColor();
    }

    /** Returns the pawn symbol letter. */
    @Override
    public String getLetter() { return "p"; }
}