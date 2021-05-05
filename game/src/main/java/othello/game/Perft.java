package othello.game;

import othello.*;
import java.util.List;

public class Perft {
    public static void Calculate() {
        final int MAX_DEPTH = 10;

        for (int depth = 0; depth < MAX_DEPTH; ++depth) {
            long node_count = NodesAtDepth(Position.initialPosition(), depth);

            System.out.println(String.format("%2d: %11d", depth, node_count));
        }
    }

    // compare with http://www.aartbik.com/strategy.php
    private static long NodesAtDepth(Position pos, int depth) {
        if (depth == 0) {
            return 1;
        }

        long count = 0;
        List<Field> moves = pos.legalMoves();
        for (Field move : moves) {
            count += NodesAtDepth(pos.applyMove(move), depth-1);
        }
        return count;
    }
}
