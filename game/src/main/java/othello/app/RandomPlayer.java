package othello.game;

import othello.*;

import java.util.List;
import java.util.Random;

public class RandomPlayer implements Player {
    private String id;

    public RandomPlayer(String id) {
        this.id = id;
    }

    public String getName() {
        return "RandomPlayer_" + id;
    }

    public Field bestMove(Position pos, List<Field> moves) {
        return getRandomElement(moves);
    }

    private static Field getRandomElement(List<Field> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }
}
