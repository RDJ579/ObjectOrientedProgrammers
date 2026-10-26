package game;

import Pieces.Color;

/** Represents one player in a local two-player game. */
public class Player {
    /** The player's piece color. */
    private final Color color;

    /** Creates a player with the given color. */
    public Player(Color color) { this.color = color; }

    /** Returns the player's color. */
    public Color getColor() { return color; }
}