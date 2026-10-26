# ObjectOrientedProgrammers

# Console Chess - CS 3354 Phase 1

A two-player chess game written in Java. Moves are entered as `board positions`.

## Requirements

- Java JDK 21 (or another recent version of Java)
- VS Code with the Extension Pack for Java, or any terminal
- No external libraries or build tools

## Compile and run (Windows PowerShell)

Open the project folder in VS Code and open **Terminal > New Terminal**. Run:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac -d out (Get-ChildItem -Recurse -Filter *.java -Path src).FullName
java -cp out game.Main
```

Example moves:

```text
White to move: E2 E4
Black to move: e7 e5
White to move: G1 F3
Black to move: quit
```

Only valid moves change turns. Enter `quit` to stop.

## Run the tests

```powershell
javac -cp out -d out tests/ChessSmokeTest.java
java -cp out ChessSmokeTest
```

Expected output: `All 16 tests passed.`


## Implemented

- 8x8 board with all 32 pieces in standard starting squares
- Text display with files A-H and ranks 1-8
- Move format `E2 E4` (case-insensitive, extra spaces allowed)
- White and black turns, rejecting wrong-color moves
- Rejection of empty starts, friendly captures, and invalid moves
- Basic movement for pawn, rook, knight, bishop, queen, and king
- Capture, including pawn diagonal capture
- Clear error messages; the game loop survives invalid input


## Team workflow

The repository owner maintains `main` and `dev`. Other member makes changes on `feature/sandy-glitch-ai`, submits a pull request to `dev`, and reviews the other member's PR. The final tested `dev` branch is merged to `main` through a separate PR.

