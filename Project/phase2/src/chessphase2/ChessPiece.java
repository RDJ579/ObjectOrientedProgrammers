package chessphase2;

/**
 * Immutable description of one chess piece. Piece-specific move rules are
 * deliberately not implemented in Phase 2.
 */
public final class ChessPiece {
    /** Player controlling a chess piece. */
    public enum Side { WHITE, BLACK }

    /** Six possible types of chess pieces. */
    public enum Type { PAWN, ROOK, KNIGHT, BISHOP, QUEEN, KING }

    private final Side side;
    private final Type type;

    /**
     * Constructs a piece of the specified side and type.
     * @param side the white or black player
     * @param type the type of chess piece
     */
    public ChessPiece(Side side, Type type) {
        if (side == null || type == null) {
            throw new IllegalArgumentException("Piece side and type are required.");
        }
        this.side = side;
        this.type = type;
    }

    /** @return the side controlling this piece */
    public Side getSide() { return side; }

    /** @return the type of this piece */
    public Type getType() { return type; }

    /** @return a readable name such as White Pawn */
    public String getName() {
        return (side == Side.WHITE ? "White " : "Black ") +
                type.name().substring(0, 1) + type.name().substring(1).toLowerCase();
    }

    /** @return a two-character save-file code such as WP or BK */
    public String getCode() {
        char prefix = side == Side.WHITE ? 'W' : 'B';
        char suffix;
        switch (type) {
            case PAWN: suffix = 'P'; break;
            case ROOK: suffix = 'R'; break;
            case KNIGHT: suffix = 'N'; break;
            case BISHOP: suffix = 'B'; break;
            case QUEEN: suffix = 'Q'; break;
            default: suffix = 'K';
        }
        return "" + prefix + suffix;
    }

    /**
     * Reconstructs a piece from its save-file code.
     * @param code two-letter code, or -- for an empty square
     * @return the piece, or null for an empty square
     */
    public static ChessPiece fromCode(String code) {
        if ("--".equals(code)) return null;
        if (code == null || code.length() != 2 ||
                (code.charAt(0) != 'W' && code.charAt(0) != 'B')) {
            throw new IllegalArgumentException("Invalid piece code: " + code);
        }
        Side side = code.charAt(0) == 'W' ? Side.WHITE : Side.BLACK;
        Type type;
        switch (code.charAt(1)) {
            case 'P': type = Type.PAWN; break;
            case 'R': type = Type.ROOK; break;
            case 'N': type = Type.KNIGHT; break;
            case 'B': type = Type.BISHOP; break;
            case 'Q': type = Type.QUEEN; break;
            case 'K': type = Type.KING; break;
            default: throw new IllegalArgumentException("Invalid piece code: " + code);
        }
        return new ChessPiece(side, type);
    }

    /** @return the standard chess Unicode character for the piece */
    public String getSymbol() {
        final String white = "♙♖♘♗♕♔";
        final String black = "♟♜♞♝♛♚";
        int index;
        switch (type) {
            case PAWN: index = 0; break;
            case ROOK: index = 1; break;
            case KNIGHT: index = 2; break;
            case BISHOP: index = 3; break;
            case QUEEN: index = 4; break;
            default: index = 5;
        }
        return "" + (side == Side.WHITE ? white.charAt(index) : black.charAt(index));
    }

    /** @return a compact letter-label representation for an alternate piece style */
    public String getLetterLabel() {
        return getCode().substring(0, 1) + "-" + getCode().substring(1);
    }
}