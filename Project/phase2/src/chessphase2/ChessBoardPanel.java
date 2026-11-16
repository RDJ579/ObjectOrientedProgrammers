package Project.phase2.src.chessphase2;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.function.Consumer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

/** A Swing grid of 64 clickable squares displaying the current chess pieces. */
public final class ChessBoardPanel extends JPanel {
    private final JButton[][] squares = new JButton[8][8];
    private int squareSize = 64;
    private String boardTheme = "Classic Brown";
    private String pieceStyle = "Chess symbols";

    /**
     * Constructs the GUI chessboard.
     * @param squareClick callback invoked with selected column (x) and row (y)
     */
    public ChessBoardPanel(Consumer<Point> squareClick) {
        setLayout(new GridLayout(8, 8, 0, 0));
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                final int r = row, c = col;
                JButton button = new JButton();
                button.setFocusable(false);
                button.setFocusPainted(false);
                button.setOpaque(true);
                button.setContentAreaFilled(true);
                button.setMargin(new java.awt.Insets(0, 0, 0, 0));
                button.setToolTipText(MoveRecord.squareName(row, col));
                button.addActionListener(event -> squareClick.accept(new Point(c, r)));
                squares[row][col] = button;
                add(button);
            }
        }
        setPreferences(boardTheme, pieceStyle, squareSize);
    }

    /**
     * Applies settings without restarting the game.
     * @param boardTheme board color scheme
     * @param pieceStyle Chess symbols or Letter labels
     * @param squareSize pixel size of one square
     */
    public void setPreferences(String boardTheme, String pieceStyle, int squareSize) {
        this.boardTheme = boardTheme;
        this.pieceStyle = pieceStyle;
        this.squareSize = squareSize;
        Dimension dimension = new Dimension(squareSize * 8, squareSize * 8);
        setPreferredSize(dimension);
        for (JButton[] row : squares) {
            for (JButton button : row) {
                button.setPreferredSize(new Dimension(squareSize, squareSize));
            }
        }
        revalidate();
    }

    /**
     * Draws pieces, square colors, and the selected square.
     * @param game current game state
     * @param selectedRow selected square row, or -1
     * @param selectedCol selected square column, or -1
     */
    public void refresh(GameState game, int selectedRow, int selectedCol) {
        Color light;
        Color dark;
        switch (boardTheme) {
            case "Slate Gray":
                light = new Color(224, 229, 235);
                dark = new Color(113, 129, 147);
                break;
            case "Forest Green":
                light = new Color(236, 239, 205);
                dark = new Color(109, 142, 104);
                break;
            default:
                light = new Color(240, 217, 181);
                dark = new Color(181, 136, 99);
        }

        boolean letters = "Letter labels".equals(pieceStyle);
        int fontSize = letters
                ? Math.max(17, squareSize / 3)
                : Math.max(29, squareSize * 2 / 3);
        Font piecesFont = new Font(
                letters ? "SansSerif" : "DejaVu Sans",
                Font.BOLD, fontSize);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                JButton button = squares[row][col];
                Color background = (row + col) % 2 == 0 ? light : dark;
                button.setBackground(background);
                button.setFont(piecesFont);

                ChessPiece piece = game.getPiece(row, col);
                button.setText(piece == null ? "" :
                        (letters ? piece.getLetterLabel() : piece.getSymbol()));

                button.setForeground(
                        piece != null && piece.getSide() == ChessPiece.Side.WHITE
                        ? new Color(39, 73, 148)
                        : new Color(28, 32, 41));

                button.setBorder(BorderFactory.createLineBorder(
                        row == selectedRow && col == selectedCol
                                ? new Color(242, 204, 67)
                                : new Color(110, 94, 78, 100),
                        row == selectedRow && col == selectedCol ? 4 : 1));
            }
        }
        repaint();
    }
}

