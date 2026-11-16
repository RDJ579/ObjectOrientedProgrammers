package Project.phase2.src.chessphase2;

import javax.swing.SwingUtilities;

/** Launches the Phase 2 Java Swing chess application. */
public final class ChessApp {
    private ChessApp() { }

    /**
     * Starts the graphical chess game on Swing's event dispatch thread.
     * @param args unused command-line arguments
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(
                () -> new ChessWindow().setVisible(true));
    }
}

