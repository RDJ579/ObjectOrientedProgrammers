package Pieces;

/** The two possible chess piece colors. */
public enum Color {
    WHITE, BLACK;

    /** Returns w for white or b for black. */
    public String prefix() { return this == WHITE ? "w" : "b"; }

    /** Returns the name displayed in the console. */
    public String displayName() { return this == WHITE ? "White" : "Black"; }

    /** Returns the color of the other player. */
    public Color opposite() { return this == WHITE ? BLACK : WHITE; }
}