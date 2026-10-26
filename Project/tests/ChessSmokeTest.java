import Board.Board;
import Board.Position;
import Pieces.Color;
import utils.InputParser;

/** Small tests that do not need any extra testing libraries. */
public class ChessSmokeTest {
    /** Runs tests for initial setup, parsing, turns, and movement. */
    public static void main(String[] args) {
        Board board = new Board();
        int count = 0;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                if (board.getPiece(new Position(r, c)) != null) count++;
            }
        }
        check(count == 32, "Starting board should have 32 pieces");
        check(board.getPiece(Position.fromChess("E1")).getSymbol().equals("wK"), "White king at E1");
        check(board.getPiece(Position.fromChess("D8")).getSymbol().equals("bQ"), "Black queen at D8");
        check(InputParser.parseMove("   e2     e4   ") != null, "Whitespace and case input");
        check(InputParser.parseMove("E9 E4") == null, "Reject invalid rank");
        check(InputParser.parseMove("E2\tE4") != null, "Accept tab-separated input");
        check(InputParser.parseMove("E2") == null, "Reject incomplete move");
        check(board.movePiece(Position.fromChess("E2"), Position.fromChess("E2"), Color.WHITE) != null,
                "Reject a same-square move");
        check(board.movePiece(Position.fromChess("A1"), Position.fromChess("A3"), Color.WHITE) != null,
                "Reject a blocked rook move");
        check(board.movePiece(Position.fromChess("E7"), Position.fromChess("E5"), Color.WHITE) != null,
                "Reject the wrong color");
        check(board.movePiece(Position.fromChess("E2"), Position.fromChess("E4"), Color.WHITE) == null,
                "Accept white pawn opening");
        check(board.getPiece(Position.fromChess("E4")).getSymbol().equals("wp"), "Pawn moved to E4");
        check(board.movePiece(Position.fromChess("A1"), Position.fromChess("A4"), Color.BLACK) != null,
                "Reject wrong color and blocked rook");
        check(board.movePiece(Position.fromChess("D7"), Position.fromChess("D5"), Color.BLACK) == null,
                "Accept black pawn opening");
        check(board.movePiece(Position.fromChess("E4"), Position.fromChess("D5"), Color.WHITE) == null,
                "Accept diagonal pawn capture");
        check(board.getPiece(Position.fromChess("D5")).getSymbol().equals("wp"), "Captured pawn replaced");
        System.out.println("All 16 tests passed.");
    }

    /** Stops with an error if the test condition is false. */
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
}