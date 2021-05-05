package othello.app;

import othello.Color;
import othello.Field;
import othello.Position;
import othello.Status;

import othello.game.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class GameController {
    @GetMapping("/hello")
    public String index() {
        return "hello";
    }

    // Test: http ':9000/status?pos=...........................OX......XO...........................&to_move=X'
    @GetMapping("/status")
    public Status getStatusOfPosition(
        @RequestParam(value = "pos", required = true) String ascii,
        @RequestParam(value = "to_move", required = true) String to_move
    ) {
        assert(ascii.length() == Position.FIELDS);
        assert(to_move.equals("X") || to_move.equals("O"));

        Status status = Position
          .fromString(ascii, Color.valueOf(to_move))
          .generateStatus();

        return status;
    }

    @GetMapping("/make_move")
    public String getPositionAfterMove(
        @RequestParam(value = "pos", required = true) String ascii,
        @RequestParam(value = "to_move", required = true) String to_move,
        @RequestParam(value = "move", required = true) String move
    ) {
        log.warn(String.format("Got: pos=%s", ascii));
        log.warn(String.format("Got: to_move=[%s]", to_move));
        log.warn(String.format("Got: move=[%s]", move));

        assert(ascii.length() == Position.FIELDS);
        assert(to_move.equals("X") || to_move.equals("O"));
        assert(move.equals("pass") || move.length() == 2);

        if (move.equals("pass")) {
            return ascii;
        }

        Position pos = Position
            .fromString(ascii, Color.valueOf(to_move))
            .applyMove(Field.valueOf(move));

        return pos.toAscii();
    }

    @GetMapping("/bot")
    public String getBotMove(
        @RequestParam(value = "pos", required = true) String ascii,
        @RequestParam(value = "to_move", required = true) String to_move
    ) {
        log.warn(String.format("Got: pos=%s", ascii));
        log.warn(String.format("Got: to_move=[%s]", to_move));

        assert(ascii.length() == Position.FIELDS);
        assert(to_move.equals("X") || to_move.equals("O"));

        Position pos = Position.fromString(ascii, Color.valueOf(to_move));
        Player player = new RandomPlayer("1");

        List<Field> moves = pos.legalMoves();
        if (moves.size() == 0) {
            return "pass";
        }
        Field move = player.bestMove(pos, moves);
        return move.toString();
    }
}
