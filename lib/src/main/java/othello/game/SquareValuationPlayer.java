package othello.game;

import othello.*;

import java.util.List;

public class SquareValuationPlayer implements Player {
    final public static int[] WEIGHTS = new int[] {
        100, -20, 10, 30, 30, 10, -20, 100,
        -20, -50,  0,  0,  0,  0, -50, -20,
         10,   0,  0,  0,  0,  0,   0,  10,
         30,   0,  0,  0,  0,  0,   0,  30,
         30,   0,  0,  0,  0,  0,   0,  30,
         10,   0,  0,  0,  0,  0,   0,  10,
        -20, -50,  0,  0,  0,  0, -50, -20,
        100, -20, 10, 30, 30, 10, -20, 100
    };

    private String id;

    public SquareValuationPlayer(String id) {
        this.id = id;
    }

    public String getName() {
        return "SquareValuationPlayer_" + id;
    }

    public Field bestMove(Position pos, List<Field> moves) {
        int best_evaluation = Integer.MIN_VALUE;
        Field best_move = null;

        for (Field move : moves) {
            int evaluation = WEIGHTS[move.toIndex()];
            if (evaluation > best_evaluation) {
                best_evaluation = evaluation;
                best_move = move;
            }
        }

        assert best_move != null;
        return best_move;
    }
}
