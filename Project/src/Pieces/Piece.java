package Pieces;

import Board.Board;
import Board.Position;

/** Base class shared by all chess pieces. */
public abstract class Piece {
    /** Which player owns this piece. */
    private final Color color;
    /** The current square occupied by this piece. */
    private Position position;

    /** Creates a piece with a color and starting position. */
    public Piece(Color color, Position position) {
        this.color = color;
        this.position = position;
    }

    /** Returns the piece color. */
    public Color getColor() { return color; }

    /** Returns the current position. */
    public Position getPosition() { return position; }

    /** Updates the position after a successful move. */
    public void setPosition(Position position) { this.position = position; }

    /** Returns true when this piece moves in its normal pattern to the target square. */
    public abstract boolean canMove(Board board, Position target);

    /** Returns the piece letter used in the console, such as P or K. */
    public abstract String getLetter();

    /** Returns a two-character symbol, such as wp or bK. */
    public String getSymbol() { return color.prefix() + getLetter(); }
}
