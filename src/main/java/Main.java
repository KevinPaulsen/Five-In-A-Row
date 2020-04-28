package main.java;

import java.awt.*;
import java.util.Scanner;

public class Main {

    private static final int depth1 = 5;
    private static final int depth2 = 5;
    private static int score = 0;
    private static final boolean MANUAL_MODE = false;
    private static final boolean HUMAN_FIRST_MOVE = true;

    public static void main(String[] args) {
        FiveInARowGame game = new FiveInARowGame();

        /*Point[] initialMoves = {
                new Point(4, 0),
                new Point(3, 1),
                new Point(1, 1),
                new Point(4, 1),
                new Point(2, 1),
                new Point(7, 1),
                new Point(1, 2),
                new Point(3, 2),
                new Point(2, 2),
                new Point(4, 2),
                new Point(2, 3),
                new Point(5, 2),
                new Point(3, 3),
                new Point(4, 3),
                new Point(5, 4),
                new Point(5, 3),
                new Point(6, 4),
                new Point(2, 4),
                new Point(2, 5),
                new Point(3, 4),
                new Point(4, 5),
                new Point(4, 4),
        };
        game.consecutiveTurns(initialMoves);//*/

        while (game.isOver() == 0) {
            if (MANUAL_MODE) {
                Scanner scanner = new Scanner(System.in);
                printScreen(game);
                if (HUMAN_FIRST_MOVE) {
                    humanMove(game, scanner);
                    printScreen(game);
                    computerMove(game, false, depth1);
                } else {
                    computerMove(game, true, depth1);
                    printScreen(game);
                    if (game.isOver() == -1) {
                        break;
                    }
                    humanMove(game, scanner);
                }
            } else {
                computerMove(game, true, depth1);
                printScreen(game);
                if (game.isOver() == -1) {
                    break;
                }
                computerMove(game, false, depth2);
                printScreen(game);
            }
        }
        printScreen(game);//*/
    }

    private static void humanMove(FiveInARowGame game, Scanner scanner) {
        int x = -1;
        int y = -1;
        while (x <= -1 || y <= -1 || x > game.getBoard().length || y > game.getBoard().length ||
                game.getBoard()[y - 1][x - 1] != 0) {
            System.out.println("Next move:");
            System.out.print("X: ");
            x = scanner.nextInt();
            System.out.print("Y: ");
            y = scanner.nextInt();
        }
        game.nextTurn(new CellScore(new Point(x - 1, y - 1), (y - 1) * game.getBoard().length + (x - 1)));//*/
    }

    private static void computerMove(FiveInARowGame game, Boolean isFirst, int depth) {
        Point bestMove = getBestMove(game, isFirst, depth);
        if (game.isOver() == 0) {
            game.nextTurn(new CellScore(new Point(bestMove.x, bestMove.y),
                    bestMove.y * game.getBoard().length + bestMove.x));
        }
    }

    private static void printScreen(FiveInARowGame game) {
        game.printBoard();
        System.out.println("Player " + 1 + " has: " + FiveInARowSolver.countOpenChainLength(game, 1) + " in a row");
        System.out.println("Player " + 2 + " has: " + FiveInARowSolver.countOpenChainLength(game, 2) + " in a row");
        System.out.println("Score: " + score);
        System.out.println("Move:  " + game.getMove());
        if (game.isOver() > 0) {
            System.out.printf("Player %d Wins!", game.isOver());
        } else if (game.isOver() == -1) {
            System.out.println("Tie Game!");
        }
    }

    private static Point getBestMove(FiveInARowGame game, boolean isFirst, int depth) {
        CellScore bestMove = FiveInARowSolver.miniMax(game, depth, Integer.MIN_VALUE, Integer.MAX_VALUE, isFirst);
        score = bestMove.getEvaluation();
        return bestMove.getPoint();
    }
}
