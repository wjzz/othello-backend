package othello;

import java.util.*;

public class Status {
    final public List<Field> moves;
    final public int difference;
    final public boolean is_pass;
    final public Color winner; /* null if draw */

    private Status(List<Field> moves, int difference, boolean is_pass, Color winner) {
        this.moves = moves;
        this.difference = difference;
        this.is_pass = is_pass;
        this.winner = winner;
    }

    public static Status MovesAvailable(List<Field> moves) {
        return new Status(moves, 0, false, null);
    }

    public static Status GameFinished(int difference, Color winner) {
        return new Status(Collections.emptyList(), difference, false, winner);
    }

    public static Status OneSidedPass(List<Field> moves) {
        return new Status(moves, 0, true, null);
    }

    public boolean isGameFinished() {
        return moves.size() == 0;
    }

    public boolean isPassForced() {
        return is_pass;
    }
}
