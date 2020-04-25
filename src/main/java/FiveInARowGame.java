package main.java;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

public class FiveInARowGame {

    //private final TreeSet<CellScore> treeSet = new TreeSet<>(Comparator.comparingInt(CellScore::getEvaluation));
    private final ArrayList<Point> gameHistory = new ArrayList<>();
    private final int[][] board = new int[9][9];
    private int turn = 1;
    private int over = 0;
    private int move = 0;

    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[41m";
    private static final String ANSI_GREEN = "\u001B[42m";


    FiveInARowGame() {
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                board[x][y] = 0;
            }
        }
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                if (board[y][x] == 0) {
                    //treeSet.add(new CellScore(new Point(x, y), 0));
                }
            }
        }
    }

    void turn(int xPos, int yPos) {
        gameHistory.add(new Point(xPos, yPos));
        if (board[yPos][xPos] == 0) {
            board[yPos][xPos] = turn;
            move++;
            over = checkGameOver(this);

            turn = (turn == 1) ? 2 : 1;
        }
    }

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

    static int checkGameOver(FiveInARowGame game) {
        for (Point point : game.getGameHistory()) {
            if (countDirection(point, -1, -1, game.getBoard()) == 5) {
                return game.getBoard()[point.y][point.x];
            } else if (countDirection(point, 0, -1, game.getBoard()) == 5) {
                return game.getBoard()[point.y][point.x];
            } else if (countDirection(point, 1, -1, game.getBoard()) == 5) {
                return game.getBoard()[point.y][point.x];
            } else if (countDirection(point, 1, 0, game.getBoard()) == 5) {
                return game.getBoard()[point.y][point.x];
            } else if (countDirection(point, 1, 1, game.getBoard()) == 5) {
                return game.getBoard()[point.y][point.x];
            } else if (countDirection(point, 0, 1, game.getBoard()) == 5) {
                return game.getBoard()[point.y][point.x];
            } else if (countDirection(point, -1, 1, game.getBoard()) >= 5) {
                return game.getBoard()[point.y][point.x];
            } else if (countDirection(point, -1, 0, game.getBoard()) == 5) {
                return game.getBoard()[point.y][point.x];
            }
        }
        return 0;
    }

    void printBoard() {
        System.out.println("\n\n\n\n\n\n\n\n\n\n");
        System.out.println("   | 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8 | 9 |");
        for (int row = 0; row < board.length; row++) {
            System.out.println("---------------------------------------");
            System.out.print((row + 1) + "  ");
            for (int space : board[row]) {
                if (space == 1) {
                    System.out.print("|" + ANSI_RED + "   " + ANSI_RESET);
                } else if (space == 2) {
                    System.out.print("|" + ANSI_GREEN + "   " + ANSI_RESET);
                } else {
                    System.out.print("|   ");
                }
            }
            System.out.println();
        }
        System.out.println("---------------------------------------");
    }

    ArrayList<Point> getPositions() {
        ArrayList<Point> availablePoints = new ArrayList<>();
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board.length; x++) {
                if (board[y][x] == 0) {
                    availablePoints.add(new Point(x, y));
                }
            }
        }
        return availablePoints;
    }

    FiveInARowGame setPosition(Point position, int player) {
        if (position.x >= 0 && position.x < board.length && position.y >= 0 && position.y < board.length) {
            board[position.y][position.x] = player;
            if (board[position.y][position.x] != 0) {
                gameHistory.add(position);
                over = checkGameOver(this);
                gameHistory.remove(gameHistory.size() - 1);
            }
        }
        return this;
    }

    public int getTurn() {
        return turn;
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
        return move;
    }
}
