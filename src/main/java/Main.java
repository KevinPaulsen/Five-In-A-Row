package main.java;

import java.awt.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static final int depth1 = 3;
    private static final int depth2 = 3;
    private static int score = 0;
    private static final boolean MANUAL_MODE = false;
    private static final boolean HUMAN_FIRST_MOVE = true;
    private static boolean isTied = false;

    public static void main(String[] args) {
        FiveInARowGame game = new FiveInARowGame();

        while (game.isOver() == 0 && !isTied) {

            if (MANUAL_MODE) {
                Scanner scanner = new Scanner(System.in);
                printStuff(game);
                if (HUMAN_FIRST_MOVE) {
                    humanMove(game, scanner);
                    printStuff(game);
                    computerMove(game, false, depth1);
                } else {
                    computerMove(game, true, depth1);
                    printStuff(game);
                    if (isTied) {
                        break;
                    }
                    humanMove(game, scanner);
                }
            } else {
                computerMove(game, true, depth1);
                printStuff(game);
                if (isTied) {
                    break;
                }
                computerMove(game, false, depth2);
                printStuff(game);
            }
        }
        printStuff(game);
    }

    private static boolean checkTie(Point bestMove) {
        return bestMove.x == -1;
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
        game.turn(x - 1, y - 1);//*/
    }

    private static void computerMove(FiveInARowGame game, Boolean isFirst, int depth) {
        Point bestMove = getBestMove(game, isFirst, depth);
        isTied = checkTie(bestMove);
        if (!isTied) {
            game.turn(bestMove.x, bestMove.y);
        }
    }

    private static void printStuff(FiveInARowGame game) {
        game.printBoard();
        System.out.println("Player " + 1 + " has: " + FiveInARowSolver.countOpenChainLength(game, 1) + " in a row");
        System.out.println("Player " + 2 + " has: " + FiveInARowSolver.countOpenChainLength(game, 2) + " in a row");
        System.out.println("Score: " + score);
        System.out.println("Move:  " + game.getMove());
        if (game.isOver() != 0) {
            System.out.printf("Player %d Wins!", game.isOver());
        } else if (isTied) {
            System.out.println("Tie Game!");
        }
    }

    private static Point getBestMove(FiveInARowGame game, boolean isFirst, int depth) {
        ArrayList<Point> positions = game.getPositions();
        Point targetPoint;
        if (positions.isEmpty()) {
            return new Point(-1, -1);
        } else {
            targetPoint = positions.get(0);
        }

        System.out.println(".".repeat(positions.size()));
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        if (isFirst) {
            int maxEval = Integer.MIN_VALUE;
            for (Point point : positions) {
                int eval = FiveInARowSolver.miniMax(game.setPosition(point, 2), depth, alpha, beta, true).getEvaluation();
                game.setPosition(point, 0);
                if (maxEval < eval) {
                    maxEval = eval;
                    targetPoint = point;
                    score = eval;
                }
                alpha = Math.max(eval, alpha);
                if (beta <= alpha) {
                    break;
                }
                System.out.print(".");
            }
            System.out.println("!");
            return targetPoint;
        } else {
            int minEval = Integer.MAX_VALUE;
            for (Point point : positions) {
                int eval = FiveInARowSolver.miniMax(game.setPosition(point, 1), depth, alpha, beta, false).getEvaluation();
                game.setPosition(point, 0);
                if (minEval > eval) {
                    minEval = eval;
                    targetPoint = point;
                    score = eval;
                }
                beta = Math.min(eval, beta);
                if (beta <= alpha) {
                    break;
                }
                System.out.print(".");
            }
            System.out.println("!");
            return targetPoint;
        }//*/
    }
}
