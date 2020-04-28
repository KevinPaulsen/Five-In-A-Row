package main.java;

import java.awt.Point;
import java.util.Comparator;
import java.util.TreeSet;

public class FiveInARowSolver {

    private static final TreeSet<CellScore> usedPositions = new TreeSet<>(Comparator.comparingInt(CellScore::getEvaluation));

    static CellScore miniMax(FiveInARowGame game, int depth, int alpha, int beta, Boolean maximizingPlayer) {
        int overScore = game.checkGameOver();
        if (depth == 0 || overScore != 0) {
            return new CellScore(new Point(), evaluation(game, overScore));
        }
        Point bestPoint = game.getAvailablePositions().first().getPoint();

        if (maximizingPlayer) {
            int maxEval = Integer.MIN_VALUE;
            for (CellScore cellScore : game.getAvailablePositions()) {
                if (usedPositions.contains(cellScore)) {
                    continue;
                }
                usedPositions.add(cellScore);
                int eval = miniMax(game.setPosition(cellScore.getPoint(), 1), depth - 1, alpha, beta, false).getEvaluation();
                usedPositions.remove(cellScore);
                game.setPosition(cellScore.getPoint(), 0);

                if (maxEval < eval) {
                    maxEval = eval;
                    bestPoint = cellScore.getPoint();
                }
                alpha = Math.max(eval, alpha);
                if (beta <= alpha) {
                    break;
                }
            }
            return new CellScore(bestPoint, maxEval);
        } else {
            int minEval = Integer.MAX_VALUE;
            for (CellScore cellScore : game.getAvailablePositions()) {
                if (usedPositions.contains(cellScore)) {
                    continue;
                }
                usedPositions.add(cellScore);
                int eval = miniMax(game.setPosition(cellScore.getPoint(), 2), depth - 1, alpha, beta, true).getEvaluation();
                usedPositions.remove(cellScore);
                game.setPosition(cellScore.getPoint(), 0);

                if (minEval > eval) {
                    minEval = eval;
                    bestPoint = cellScore.getPoint();
                }
                beta = Math.min(eval, beta);
                if (beta <= alpha) {
                    break;
                }
            }
            return new CellScore(bestPoint, minEval);
        }
    }

    static int evaluation(FiveInARowGame game, int overScore) {

        if (overScore == 1) {
            return Integer.MAX_VALUE - game.getMove();
        } else if (overScore == 2) {
            return Integer.MIN_VALUE + game.getMove();
        } else if (overScore == -1) {
            return 0;
        }

        // Count my chain length
        int AIChains = countOpenChainLength(game, 1);

        // Count their chain Length
        int pChains = countOpenChainLength(game, 2);

        return AIChains - pChains;
    }

    static int countOpenChainLength(FiveInARowGame game, int searchNum) {

        int chainLength = 0;
        int[][] board = game.getBoard();

        for (Point point : game.getGameHistory()) {
            if (board[point.y][point.x] != searchNum) {
                continue;
            }
            boolean isTopRow = point.y == 0;
            boolean isFistColumn = point.x == 0;
            boolean isLastColumn = point.x == board.length - 1;

            // Down Left
            if (isTopRow || isLastColumn || board[point.y - 1][point.x + 1] != searchNum) {
                int possibleChainLength = FiveInARowGame.countDirection(point, -1, 1, board);

                boolean isOpenTopRight = !isTopRow && !isLastColumn && board[point.y - 1][point.x + 1] == 0;
                boolean isOpenBottomLeft = (point.x - possibleChainLength) >= 0 &&
                        (point.y + possibleChainLength) < board.length &&
                        board[point.y + possibleChainLength][point.x - possibleChainLength] == 0;

                chainLength += getChainLength(possibleChainLength, isOpenTopRight, isOpenBottomLeft);
            }
            // Down
            if (isTopRow || board[point.y - 1][point.x] != searchNum) {
                int possibleChainLength = FiveInARowGame.countDirection(point, 0, 1, board);

                boolean isOpenTop = !isTopRow && board[point.y - 1][point.x] == 0;
                boolean isOpenBottom = (point.y + possibleChainLength) < board.length &&
                        board[point.y + possibleChainLength][point.x] == 0;

                chainLength += getChainLength(possibleChainLength, isOpenTop, isOpenBottom);
            }
            // Down Right
            if (isTopRow || isFistColumn || board[point.y - 1][point.x - 1] != searchNum) {
                int possibleChainLength = FiveInARowGame.countDirection(point, 1, 1, board);

                boolean isOpenTopLeft = !isFistColumn && !isTopRow && board[point.y - 1][point.x - 1] == 0;
                boolean isOpenBottomRight = (point.x + possibleChainLength) < board.length &&
                        (point.y + possibleChainLength) < board.length &&
                        board[point.y + possibleChainLength][point.x + possibleChainLength] == 0;
                chainLength += getChainLength(possibleChainLength, isOpenBottomRight, isOpenTopLeft);
            }
            // Right
            if (isFistColumn || board[point.y][point.x - 1] != searchNum) {
                int possibleChainLength = FiveInARowGame.countDirection(point, 1, 0, board);

                boolean isOpenLeft = !isFistColumn && board[point.y][point.x - 1] == 0;
                boolean isOpenRight = (point.x + possibleChainLength) < board.length &&
                        board[point.y][point.x + possibleChainLength] == 0;
                chainLength += getChainLength(possibleChainLength, isOpenLeft, isOpenRight);
            }
        }
        return chainLength;
    }

    private static int getChainLength(int possibleChainLength, boolean condition1, boolean condition2) {
        if (condition2 && condition1 && possibleChainLength != 1) {
            return (int) Math.pow(possibleChainLength, 2);
        } else if ((condition2 || condition1) && possibleChainLength != 1) {
            return possibleChainLength;
        }
        return 0;
    }
}
