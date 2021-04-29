package othello.game;

import othello.*;

import java.util.List;

public interface Player {
    String getName();

    Field bestMove(Position pos, List<Field> moves);
}
