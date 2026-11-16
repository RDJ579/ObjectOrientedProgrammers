# Chess Project - Phase 2

## Overview

This is our Phase 2 chess project for CS 3354.

We created a graphical chess game using Java Swing.
The game displays an 8x8 chessboard with White and Black pieces.

Players can click a piece and then click a destination
square to move it.


## Features

- 8x8 chessboard with alternating colors
- White and Black pieces in their starting positions
- Mouse-click movement
- Capturing opponent pieces
- Winner popup when a King is captured
- New Game
- Save Game and Load Game
- Move history
- Captured-pieces display
- Undo last move
- Appearance settings for board color, piece style, and size

## Java Classes

ChessPiece.java:
Stores each piece's side and type.

MoveRecord.java:
Stores completed moves and captured pieces.

GameState.java:
Manages the board, player turns, captures, Undo,
Save, and Load.

ChessBoardPanel.java:
Displays the graphical chessboard using Swing buttons.

ChessWindow.java:
Handles the GUI, mouse input, menus, history panel,
settings, and winner popup.

ChessApp.java:
Starts the graphical chess game.

## Compile and Run

Open PowerShell in the Git repository root.

Compile:

javac -encoding UTF-8 -d phase2/out (Get-ChildItem phase2/src -Recurse -File -Filter *.java).FullName

Run:

java -cp phase2/out chessphase2.ChessApp

## Team Contributions

Member 1:
Chess model, game-state handling, and automated testing.

Member 2:
Swing interface, mouse controls, menus, and GUI testing.