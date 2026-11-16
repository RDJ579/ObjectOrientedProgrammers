package Project.phase2.tests;

import Project.phase2.src.chessphase2.ChessPiece;
import Project.phase2.src.chessphase2.GameState;
import Project.phase2.src.chessphase2.MoveRecord;
import java.nio.file.Files;
import java.nio.file.Path;

/** No external test library is required; run with java -cp out GameStateTest. */
public final class GameStateTest {
    private static int checks = 0;

    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
        checks++;
    }

    private static void rejects(Runnable action, String message) {
        try {
            action.run();
            throw new AssertionError(message);
        } catch (IllegalArgumentException expected) {
            checks++;
        }
    }

    public static void main(String[] args) throws Exception {
        GameState game = new GameState();
        int total = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (game.getPiece(row, col) != null) total++;
            }
        }
        check(total == 32, "Expected 32 pieces");
        check(game.getTurn() == ChessPiece.Side.WHITE, "White starts");
        check(game.getPiece(7, 4).getType() == ChessPiece.Type.KING, "White king E1");
        check(game.getPiece(0, 4).getType() == ChessPiece.Type.KING, "Black king E8");
        rejects(() -> game.move(4, 4, 3, 4), "Empty origin must fail");
        rejects(() -> game.move(1, 4, 3, 4), "Wrong player must fail");
        rejects(() -> game.move(6, 4, 7, 4), "Cannot capture same side");
        rejects(() -> game.move(6, 4, 6, 4), "Must choose different destination");
        rejects(() -> game.move(-1, 4, 0, 4), "Invalid coordinate");

        MoveRecord first = game.move(6, 4, 4, 4); // E2 E4
        check("E2".equals(MoveRecord.squareName(first.getFromRow(), first.getFromCol())),
                "Move notation");
        check(game.getPiece(4, 4) != null && game.getPiece(6, 4) == null,
                "Move updated squares");
        check(game.getTurn() == ChessPiece.Side.BLACK, "Turn changes");

        game.move(1, 3, 3, 3); // D7 D5
        MoveRecord capture = game.move(4, 4, 3, 3); // E4 x D5
        check(capture.getCaptured() != null, "Capture should record enemy piece");
        check(game.getPiece(3, 3).getSide() == ChessPiece.Side.WHITE,
                "Capture replaces enemy");
        check(game.getHistory().size() == 3, "History length");
        check(game.undo(), "Undo should work");
        check(game.getPiece(3, 3).getSide() == ChessPiece.Side.BLACK,
                "Undo restores captured piece");
        check(game.getPiece(4, 4).getSide() == ChessPiece.Side.WHITE,
                "Undo restores attacker");
        check(game.getTurn() == ChessPiece.Side.WHITE, "Undo restores player turn");

        game.move(4, 4, 3, 3);
        Path save = Files.createTempFile("phase2-chess-", ".txt");
        try {
            game.save(save);
            GameState loaded = GameState.load(save);
            check(loaded.getHistory().size() == 3, "Save/load history");
            check(loaded.getPiece(3, 3).getSide() == ChessPiece.Side.WHITE,
                    "Save/load board");
            check(loaded.getTurn() == ChessPiece.Side.BLACK, "Save/load turn");
            check(loaded.undo(), "Undo after loading works");
            check(loaded.getPiece(3, 3).getSide() == ChessPiece.Side.BLACK,
                    "Captured piece restored after load");
        } finally {
            Files.deleteIfExists(save);
        }

        game.move(0, 1, 2, 1); // Black knight B8 -> B6
        MoveRecord kingCapture = game.move(7, 3, 0, 4);
        check(kingCapture.getCaptured().getType() == ChessPiece.Type.KING,
                "King captured");
        check(game.getWinner() == ChessPiece.Side.WHITE, "White winner declared");
        rejects(() -> game.move(0, 6, 2, 6), "Finished game rejects moves");

        game.reset();
        check(game.getHistory().isEmpty(), "Reset clears history");
        check(game.getWinner() == null, "Reset clears winner");
        check(!game.undo(), "Empty undo reports false");
        check(ChessPiece.fromCode("BN").getType() == ChessPiece.Type.KNIGHT,
                "Code round trip");

        System.out.println("All " + checks + " Phase 2 model checks passed.");
    }
}