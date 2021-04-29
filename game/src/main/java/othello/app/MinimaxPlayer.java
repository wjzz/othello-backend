package othello.game;

import othello.*;

import java.util.List;

public class MinimaxPlayer implements Player {
    final static int WIN_EVAL = 10000;
    final static int LOSS_EVAL = -10000;

    private String id;
    private int depth;
    public boolean debug = false;

    public MinimaxPlayer(String id, int depth) {
        this.id = id;
        this.depth = depth;
    }

    public String getName() {
        return "MinimaxPlayer_" + id;
    }

    private int eval_terminal(final Position pos) {
        int evaluation = 0;
        Square[] board = pos.getBoard();

        for (int i = 0; i < Position.FIELDS; ++i) {
            if (board[i] == Square.EMPTY) continue;

            int value = SquareValuationPlayer.WEIGHTS[i];
            int sign = (board[i] == pos.getPlayerToMove().toSquare()) ? 1 : -1;
            evaluation += value * sign;
        }
        return evaluation;
    }

    /** evaluate the current position
     * @return      0 if DRAW is the best move
     * @return  10000 if the current player wins
     * @return -10000 if the current player losses
     */
    private int eval(final Position pos, final int depth) {
        Color to_move = pos.getPlayerToMove();
        final Status status = pos.generateStatus();

        if (status.isGameFinished()) {
            if (status.winner == null)
                return 0;
            else if (status.winner == to_move)
                return WIN_EVAL;
            else
                return LOSS_EVAL;
        }

        if (depth == 0) {
            return eval_terminal(pos);
        }

        if (status.isPassForced()) {
            pos.makePass();
            return -eval(pos, depth-1);
        }

        int best_evaluation = Integer.MIN_VALUE;

        for (Field move : status.moves) {
            int evaluation = -eval(pos.applyMove(move), depth-1);
            if (evaluation > best_evaluation) {
                best_evaluation = evaluation;
            }
        }

        return best_evaluation;
    }

    public Field bestMove(final Position pos, final List<Field> moves) {
        int best_evaluation = Integer.MIN_VALUE;
        Field best_move = null;

        for (Field move : moves) {
            int evaluation = -eval(pos.applyMove(move), depth-1);

            if (this.debug) {
                System.out.println("evaluation = " + evaluation);
            }

            if (evaluation > best_evaluation) {
                best_evaluation = evaluation;
                best_move = move;
            }
        }
        if (this.debug) {
            System.out.println("best = " + best_evaluation);
        }

        return best_move;
    }
}
