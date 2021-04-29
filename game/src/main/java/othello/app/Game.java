package othello.game;

import othello.*;

import java.util.List;

public class Game {
    public static void main(String[] args) {
        // Player player_1 = new RandomPlayer("1");
        // Player player_1 = new SquareValuationPlayer("1");
        MinimaxPlayer player_1 = new MinimaxPlayer("depth=3", 3);
        // player_1.debug = true;
        Player player_2 = new MinimaxPlayer("depth=5", 5);
        // Player player_2 = new MinimaxPlayer("depth=2", 2);

        int num_games = 1;
        if (args.length > 0)
            num_games = Integer.parseInt(args[0]);

        PlayManyGames(player_1, player_2, num_games);
    }

    private static int Percentage(int ok, int all) {
        return 100 * ok / all;
    }

    private static void PlayManyGames(Player player_1, Player player_2, int num_games) {
        Player[] players = new Player[]{ player_1, player_2 };

        int[] results = new int[] { 0, 0};
        int draws = 0;

        for (int i = 0; i < num_games; ++i) {
            // flip players every other game
            final boolean flip_players = (i % 2 == 1);
            final Color winner = PlayGame(players, flip_players);

            if (winner == null) {
                ++draws;
            } else if (winner == Color.X) {
                results[i%2]++;
            } else {
                results[1 - i%2]++;
            }
        }
        System.out.println(String.format("After %d games:", num_games));
        System.out.println(String.format("Draws: %d", draws));
        System.out.println(String.format("%s wins: %d [%d%%]",
            player_1.getName(), results[0], Percentage(results[0], num_games)));
        System.out.println(String.format("%s wins: %d [%d%%]",
            player_2.getName(), results[1], Percentage(results[1], num_games)));
    }


    /** @return color of the winner, or null if draw */
    public static Color PlayGame(Player[] players, boolean flip_players) {
        Position pos = Position.initialPosition();
        int i = 0;

        while (true) {
            System.out.println(pos.toString());

            final Status status = pos.generateStatus();

            if (status.isGameFinished()) {
                return status.winner;
            }

            if (status.isPassForced()) {
                i++;
                pos.makePass();
            }

            Color curr_color = i%2 == 0 ? Color.X : Color.O;
            assert pos.getPlayerToMove() == curr_color;

            final int curr_player = flip_players ? (1-i%2) : i%2;
            final Player player = players[curr_player];
            final Field move = player.bestMove(pos, status.moves);
            pos.makeMove(move);

            System.out.println("Move made by player" + player.getName());

            i++;
        }
    }
}
