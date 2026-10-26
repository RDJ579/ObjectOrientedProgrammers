package game;

import java.util.Scanner;
import Board.Board;
import Board.Position;
import Pieces.Color;
import utils.InputParser;

/** Runs the chess game in the terminal. */
public class Game {
    /** The current chessboard. */
    private final Board board = new Board();
    /** The white player. */
    private final Player whitePlayer = new Player(Color.WHITE);
    /** The black player. */
    private final Player blackPlayer = new Player(Color.BLACK);
    /** Color of the player who moves next. */
    private Color turn = Color.WHITE;

    /** Starts the console loop and processes moves until the user quits. */
    public void play() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Console Chess - Phase 1");
        System.out.println("Type moves like E2 E4, or type quit to exit.");
        while (true) {
            board.display();
            Player current = turn == Color.WHITE ? whitePlayer : blackPlayer;
            System.out.print(current.getColor().displayName() + " to move: ");
            if (!scanner.hasNextLine()) break;
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("quit")) break;
            Position[] move = InputParser.parseMove(input);
            if (move == null) {
                System.out.println("Invalid input. Use a move such as E2 E4.");
                continue;
            }
            String error = board.movePiece(move[0], move[1], turn);
            if (error != null) {
                System.out.println("Invalid move: " + error);
            } else {
                System.out.println("Moved " + move[0] + " to " + move[1] + ".");
                turn = turn.opposite();
            }
        }
        System.out.println("Game ended.");
    }
}