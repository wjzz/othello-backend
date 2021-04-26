package othello.game;

import othello.*;

import java.util.List;
import java.util.Random;

public class Game {
    static int X_wins = 0;
    static int O_wins = 0;
    static int draws = 0;

    public static void main(String[] args) {
        int num_games = 1;
        if (args.length > 0)
            num_games = Integer.parseInt(args[0]);

        for (int i = 0; i < num_games; ++i)
            PlayGame();

        int perc_X = (100 * X_wins) / num_games;
        int perc_O = (100 * O_wins) / num_games;
        int perc_draws = (100 * draws) / num_games;

        System.out.println(String.format("Results:\nX    %3d (%2d%%)\nO    %3d (%2d%%)\ndraw %3d (%2d%%)",
            X_wins, perc_X, O_wins, perc_O, draws, perc_draws));
    }

    private static Field getRandomElement(List<Field> list) {
        Random rand = new Random();
        return list.get(rand.nextInt(list.size()));
    }

    public static void PlayGame() {
        Position pos = Position.initialPosition();
        int i = 0;

        while (true) {
            // System.out.println("------------------------------------");
            // System.out.println("Current position");
            // System.out.println(pos);

            Color curr_player = pos.getPlayerToMove();
            // System.out.println(i + " to move: " + curr_player);

            Status status = pos.generateStatus();
            String result = status.result;

            if (result != "") {
                if (result == "draw")
                    draws++;
                else if (result.startsWith("X"))
                    X_wins++;
                else
                    O_wins++;

                // System.out.println("Game finished!");
                // System.out.println(result);
                break;
            }

            List<Field> moves = status.moves;
            if (status.is_pass) {
                // System.out.println("no legal moves, PASS!");
                curr_player = curr_player.opposite();
                i++;
            }

            // Pick one move at random
            Field move = getRandomElement(moves);
            // System.out.println("Chosen move: " + move);
            pos.makeMove(move);
        }
    }
}
