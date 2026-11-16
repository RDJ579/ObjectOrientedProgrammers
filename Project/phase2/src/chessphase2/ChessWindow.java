package chessphase2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.io.File;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

/**
 * Swing user interface for Phase 2: clickable chessboard, menu, move history,
 * captures, Undo, and appearance settings.
 */
public final class ChessWindow extends JFrame {
    private GameState game = new GameState();
    private final ChessBoardPanel boardPanel;
    private final JLabel playerLabel = new JLabel();
    private final JLabel statusLabel =
            new JLabel("Click one of your pieces, then a destination.");
    private final JTextArea historyArea = new JTextArea();
    private final JLabel whiteCaptures = new JLabel();
    private final JLabel blackCaptures = new JLabel();
    private final JButton undoButton = new JButton("Undo last move");

    private int selectedRow = -1;
    private int selectedCol = -1;
    private String boardTheme = "Classic Brown";
    private String pieceStyle = "Chess symbols";
    private int squareSize = 64;

    /** Builds the playable chess GUI with an initial board. */
    public ChessWindow() {
        super("Chess Game - Phase 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(new Color(242, 245, 249));
        setJMenuBar(buildMenuBar());

        boardPanel = new ChessBoardPanel(this::onSquareClick);

        JPanel boardHolder = new JPanel(new BorderLayout());
        boardHolder.setBorder(new EmptyBorder(15, 18, 15, 15));
        boardHolder.setOpaque(false);
        boardHolder.add(boardPanel, BorderLayout.CENTER);

        add(boardHolder, BorderLayout.CENTER);
        add(buildSidebar(), BorderLayout.EAST);

        statusLabel.setBorder(new EmptyBorder(8, 18, 12, 15));
        statusLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        add(statusLabel, BorderLayout.SOUTH);

        refresh();
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(750, 540));
    }

    private JMenuBar buildMenuBar() {
        JMenuBar bar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");
        JMenuItem newGame = new JMenuItem("New Game");
        JMenuItem saveGame = new JMenuItem("Save Game...");
        JMenuItem loadGame = new JMenuItem("Load Game...");
        JMenuItem exit = new JMenuItem("Exit");

        newGame.addActionListener(event -> newGame());
        saveGame.addActionListener(event -> saveGame());
        loadGame.addActionListener(event -> loadGame());
        exit.addActionListener(event -> dispose());

        gameMenu.add(newGame);
        gameMenu.add(saveGame);
        gameMenu.add(loadGame);
        gameMenu.addSeparator();
        gameMenu.add(exit);
        bar.add(gameMenu);

        JMenu settingsMenu = new JMenu("Settings");
        JMenuItem customize =
                new JMenuItem("Customize board and pieces...");
        customize.addActionListener(event -> showSettingsWindow());
        settingsMenu.add(customize);
        bar.add(settingsMenu);

        return bar;
    }

    private JPanel buildSidebar() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 14, 16, 16));
        panel.setPreferredSize(new Dimension(270, 520));

        JLabel title = new JLabel("CHESS / PHASE 2");
        title.setFont(new Font("SansSerif", Font.BOLD, 17));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(title);
        panel.add(spacer(13));

        playerLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        playerLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(playerLabel);
        panel.add(spacer(18));

        JLabel historyHeading = new JLabel("Move history");
        historyHeading.setFont(new Font("SansSerif", Font.BOLD, 14));
        historyHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(historyHeading);
        panel.add(spacer(5));

        historyArea.setEditable(false);
        historyArea.setFont(new Font("SansSerif", Font.PLAIN, 12));
        historyArea.setLineWrap(true);
        historyArea.setWrapStyleWord(true);
        historyArea.setBackground(new Color(250, 250, 250));

        JScrollPane scroll = new JScrollPane(historyArea);
        scroll.setPreferredSize(new Dimension(246, 245));
        scroll.setMaximumSize(new Dimension(260, 275));
        scroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(scroll);
        panel.add(spacer(15));

        JLabel capturesHeading = new JLabel("Captured pieces");
        capturesHeading.setFont(new Font("SansSerif", Font.BOLD, 14));
        capturesHeading.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(capturesHeading);
        panel.add(spacer(6));

        whiteCaptures.setFont(new Font("SansSerif", Font.PLAIN, 12));
        whiteCaptures.setAlignmentX(Component.LEFT_ALIGNMENT);
        blackCaptures.setFont(new Font("SansSerif", Font.PLAIN, 12));
        blackCaptures.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(whiteCaptures);
        panel.add(spacer(5));
        panel.add(blackCaptures);
        panel.add(spacer(18));

        undoButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        undoButton.setMaximumSize(new Dimension(255, 36));
        undoButton.addActionListener(event -> {
            if (game.undo()) {
                clearSelection();
                statusLabel.setText(
                        "Last move undone. Any captured piece was restored.");
                refresh();
            }
        });

        panel.add(undoButton);
        panel.add(spacer(16));

        JLabel hint = new JLabel("Click a piece, then its destination.");
        hint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        hint.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(hint);

        return panel;
    }

    private static JPanel spacer(int size) {
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);
        spacer.setMaximumSize(new Dimension(1, size));
        spacer.setPreferredSize(new Dimension(1, size));
        return spacer;
    }

    private void onSquareClick(Point square) {
        int row = square.y;
        int col = square.x;
        ChessPiece piece = game.getPiece(row, col);

        if (selectedRow < 0) {
            if (piece == null) {
                statusLabel.setText("That square is empty. Select a piece.");
            } else if (piece.getSide() != game.getTurn()) {
                statusLabel.setText("It is " + game.getTurn() + "'s turn.");
            } else {
                selectedRow = row;
                selectedCol = col;
                statusLabel.setText(
                        "Selected " + piece.getName() + " on " +
                        MoveRecord.squareName(row, col) +
                        ". Choose a destination.");
            }
            refresh();
            return;
        }

        if (selectedRow == row && selectedCol == col) {
            clearSelection();
            statusLabel.setText("Selection canceled.");
            refresh();
            return;
        }

        if (piece != null && piece.getSide() == game.getTurn()) {
            selectedRow = row;
            selectedCol = col;
            statusLabel.setText(
                    "Selected " + piece.getName() + ". Choose a destination.");
            refresh();
            return;
        }

        try {
            MoveRecord move = game.move(selectedRow, selectedCol, row, col);
            clearSelection();
            statusLabel.setText(move.describe());
            refresh();

            if (game.getWinner() != null) {
                String winningPlayer =
                        game.getWinner() == ChessPiece.Side.WHITE
                        ? "White" : "Black";

                JOptionPane.showMessageDialog(this,
                        winningPlayer +
                        " wins! The opponent's King was captured.",
                        "Game Over",
                        JOptionPane.INFORMATION_MESSAGE);

                System.exit(0);
            }
        } catch (IllegalArgumentException problem) {
            clearSelection();
            statusLabel.setText(problem.getMessage());
            refresh();
        }
    }

    private void clearSelection() {
        selectedRow = -1;
        selectedCol = -1;
    }

    private void refresh() {
        boardPanel.refresh(game, selectedRow, selectedCol);

        playerLabel.setText(
                "Turn: " +
                (game.getTurn() == ChessPiece.Side.WHITE ? "White" : "Black"));

        StringBuilder moves = new StringBuilder();
        StringBuilder capturedByWhite = new StringBuilder();
        StringBuilder capturedByBlack = new StringBuilder();

        for (int i = 0; i < game.getHistory().size(); i++) {
            MoveRecord move = game.getHistory().get(i);
            moves.append(i + 1)
                    .append(". ")
                    .append(move.describe())
                    .append('\n');

            if (move.getCaptured() != null) {
                StringBuilder target =
                        move.getMoved().getSide() == ChessPiece.Side.WHITE
                        ? capturedByWhite : capturedByBlack;

                if (target.length() > 0) target.append(", ");
                target.append(
                        move.getCaptured().getType().name().toLowerCase());
            }
        }

        historyArea.setText(moves.toString());
        historyArea.setCaretPosition(
                historyArea.getDocument().getLength());

        whiteCaptures.setText(
                "By White: " +
                (capturedByWhite.length() == 0 ? "None" : capturedByWhite));

        blackCaptures.setText(
                "By Black: " +
                (capturedByBlack.length() == 0 ? "None" : capturedByBlack));

        undoButton.setEnabled(!game.getHistory().isEmpty());
    }

    private void newGame() {
        game.reset();
        clearSelection();
        statusLabel.setText("New game started. White moves first.");
        refresh();
    }

    private void saveGame() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Save current chess game");
        chooser.setSelectedFile(new File("chess-game.txt"));

        if (chooser.showSaveDialog(this)
                != JFileChooser.APPROVE_OPTION) return;

        try {
            game.save(chooser.getSelectedFile().toPath());
            statusLabel.setText(
                    "Game saved to " + chooser.getSelectedFile().getName());
        } catch (Exception problem) {
            JOptionPane.showMessageDialog(this,
                    "Could not save: " + problem.getMessage(),
                    "Save error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadGame() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Load a saved chess game");

        if (chooser.showOpenDialog(this)
                != JFileChooser.APPROVE_OPTION) return;

        try {
            GameState loaded =
                    GameState.load(chooser.getSelectedFile().toPath());

            if (loaded.getWinner() != null) {
                throw new IllegalArgumentException(
                        "Finished games cannot be resumed.");
            }

            game = loaded;
            clearSelection();
            statusLabel.setText(
                    "Game restored from " +
                    chooser.getSelectedFile().getName());
            refresh();
        } catch (Exception problem) {
            JOptionPane.showMessageDialog(this,
                    "Could not load: " + problem.getMessage(),
                    "Load error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showSettingsWindow() {
        JDialog dialog =
                new JDialog(this, "Customize Chess", true);

        JPanel content = new JPanel(new BorderLayout(10, 14));
        content.setBorder(new EmptyBorder(16, 18, 16, 18));

        JPanel options = new JPanel(new GridLayout(3, 2, 12, 12));

        JComboBox<String> themes = new JComboBox<>(
                new String[] {
                        "Classic Brown",
                        "Slate Gray",
                        "Forest Green"
                });

        JComboBox<String> pieces = new JComboBox<>(
                new String[] {
                        "Chess symbols",
                        "Letter labels"
                });

        JComboBox<String> sizes = new JComboBox<>(
                new String[] {
                        "Small",
                        "Medium",
                        "Large"
                });

        themes.setSelectedItem(boardTheme);
        pieces.setSelectedItem(pieceStyle);
        sizes.setSelectedItem(
                squareSize == 52 ? "Small" :
                squareSize == 78 ? "Large" : "Medium");

        options.add(new JLabel("Board color/style:"));
        options.add(themes);
        options.add(new JLabel("Piece appearance:"));
        options.add(pieces);
        options.add(new JLabel("Board size:"));
        options.add(sizes);

        content.add(options, BorderLayout.CENTER);

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton apply = new JButton("Apply");
        JButton close = new JButton("Close");

        apply.addActionListener(event -> {
            boardTheme = (String) themes.getSelectedItem();
            pieceStyle = (String) pieces.getSelectedItem();

            String selectedSize = (String) sizes.getSelectedItem();
            squareSize =
                    "Small".equals(selectedSize) ? 52 :
                    "Large".equals(selectedSize) ? 78 : 64;

            boardPanel.setPreferences(
                    boardTheme, pieceStyle, squareSize);

            refresh();
            pack();
            statusLabel.setText("Appearance settings applied.");
            dialog.dispose();
        });

        close.addActionListener(event -> dialog.dispose());

        actions.add(apply);
        actions.add(close);
        content.add(actions, BorderLayout.SOUTH);

        dialog.setContentPane(content);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
