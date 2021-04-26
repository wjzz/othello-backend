package othello;

import java.util.*;

public class Status {
    final public List<Field> moves;
    final public String result;
    final public boolean is_pass;

    private Status(List<Field> moves, String result, boolean is_pass) {
        this.moves = moves;
        this.result = result;
        this.is_pass = is_pass;
    }

    public static Status MovesAvailable(List<Field> moves) {
        return new Status(moves, "", false);
    }

    public static Status GameFinished(String result) {
        return new Status(Collections.emptyList(), result, false);
    }

    public static Status OneSidedPass(List<Field> moves) {
        return new Status(moves, "", true);
    }
}
