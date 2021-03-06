package main.java;

import java.awt.Point;
import java.util.ArrayList;

public class FiveInARowGame {

    private final int boardSize = 9;
    private ArrayList<CellScore> availablePositions = new ArrayList<>();
    private final ArrayList<Point> gameHistory = new ArrayList<>();
    private final int[][] board = new int[boardSize][boardSize];
    private int turn = 1; // 1 = player 1, 2 = player 2
    private int over = 0; // 0 = still playing, 1 = P1 wins, 2 = P2 wins.
    private int numMoves = 0; // Counts moves in game.

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[41m";
    private static final String ANSI_GREEN = "\u001B[42m";


    /**
     * Creates new instance with empty Board
     */
    FiveInARowGame() {
        for (int y = 0; y < boardSize; y++) {
            for (int x = 0; x < boardSize; x++) {
                board[y][x] = 0;
                availablePositions.add(new CellScore(new Point(x, y), 0));
            }
        }
    }

    /**
     * Progresses the game by one move
     * @param position the point the next player will play
     */
    void nextTurn(CellScore position) {
        boolean pointIsInBoard = (position.getX() >= 0) && (position.getX() < boardSize) && (position.getY() >= 0)
                && (position.getY() < boardSize);

        if (pointIsInBoard && board[position.getY()][position.getX()] == 0) {
            board[position.getY()][position.getX()] = turn;
            if (position.getEvaluation() == -1) {
                for (int idx = 0; idx < availablePositions.size(); idx++) {
                    if (availablePositions.get(idx).getPoint().equals(position.getPoint())) {
                        availablePositions.remove(idx);
                        break;
                    }
                }
            } else {
                availablePositions.remove(position);
            }
            gameHistory.add(position.getPoint());
            numMoves++;
            turn = (turn == 1) ? 2 : 1;
            over = checkGameOver();
        }
    }

    FiveInARowGame setPosition(Point position, int player) {
        boolean positionIsValid = position.x >= 0 && position.x < boardSize && position.y >= 0 && position.y < boardSize;
        if (positionIsValid) {
            board[position.y][position.x] = player;
            if (player != 0) {
                gameHistory.add(position);
                numMoves++;
            } else {
                gameHistory.remove(position);
                numMoves--;
            }
            turn = (turn == 1) ? 2 : 1;
        }
        return this;
    }

    /**
     * Counts the number of consecutive points each player has in a
     * specified direction.
     * @param point The point look if there are any consecutive positions
     * @param xDirection the direction to move in the x direction (-1 left, 0 none, 1 right)
     * @param yDirection the direction to move in the y direction (-1 up, 0 none, 1 down)
     * @param board the game board
     * @return number found in a direction. 0 if none found, minimum of 2.
     */
    static int countDirection(Point point, int xDirection, int yDirection, int[][] board) {
        int searchNum = board[point.y][point.x];
        int numFound = 0;
        int x = point.x;
        int y = point.y;
        boolean cont = true;
        while (cont) {
            x += xDirection;
            y += yDirection;
            numFound++;
            if (x < 0 || x >= board.length
                    || y < 0 || y >= board.length
                    || board[y][x] != searchNum) {
                cont = false;
            }
        }
        return numFound;
    }

    /**
     * Checks if 5 in a row has been found
     * @return 0 if no player won. 1 for player 1, 2 for player 2.
     * //TODO: Make -1 = tie
     */
    int checkGameOver() {
        for (Point point : gameHistory) {
            if (countDirection(point, 1, 0, board) == 5) {
                return board[point.y][point.x];
            } else if (countDirection(point, -1, 1, board) == 5) {
                return board[point.y][point.x];
            } else if (countDirection(point, 0, 1, board) == 5) {
                return board[point.y][point.x];
            } else if (countDirection(point, 1, 1, board) == 5) {
                return board[point.y][point.x];
            }
        }
        if (availablePositions.size() == 0) {
            return -1;
        }
        return 0;
    }

    /**
     * Prints the board to the console. Red = Player 1, Green = Player 2.
     */
    void printBoard() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n");
        System.out.print("   |");
        for (int i = 0; i < boardSize; i++) {
            System.out.printf("%2d |", i + 1);
        }
        System.out.println();
        for (int row = 0; row < boardSize; row++) {
            System.out.println("-".repeat(boardSize * 4) + "----");
            System.out.printf("%2d |", row + 1);
            StringBuilder printRow = new StringBuilder();
            for (int space : board[row]) {
                if (space == 1) {
                    printRow.append(ANSI_RED + "   " + ANSI_RESET + "|");
                } else if (space == 2) {
                    printRow.append(ANSI_GREEN + "   " + ANSI_RESET + "|");
                } else {
                    printRow.append("   |");
                }
            }
            System.out.println(printRow);
        }
        System.out.println("-".repeat(boardSize * 4) + "----");
    }

    void consecutiveTurns(Point[] positions) {
        for (Point position : positions) {
            nextTurn(new CellScore(position, 0));
        }
    }

    public void setAvailablePositions(ArrayList<CellScore> availablePositions) {
        this.availablePositions = availablePositions;
    }

    ArrayList<CellScore> getAvailablePositions() {
        return availablePositions;
    }

    public int[][] getBoard() {
        return board;
    }

    public int isOver() {
        return over;
    }

    public ArrayList<Point> getGameHistory() {
        return gameHistory;
    }

    public int getMove() {
        return numMoves;
    }
}
