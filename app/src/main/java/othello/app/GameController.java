package othello.app;

import othello.Color;
import othello.Field;
import othello.Position;

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

    @GetMapping("/candidates")
    public List<Field> getCandidates(
        @RequestParam(value = "pos", required = true) String ascii,
        @RequestParam(value = "to_move", required = true) String to_move
    ) {
        log.warn(String.format("Got: pos=%s", ascii));
        log.warn(String.format("Got: to_move=[%s]", to_move));

        assert(ascii.length() == Position.FIELDS);
        assert(to_move.equals("X") || to_move.equals("O"));

        Position pos = Position.fromString(ascii, Color.valueOf(to_move));
        List<Field> moves = pos.legalMoves();

        return moves;
    }

    @GetMapping("/make_move")
    public Map<String, Object> getPositionAfterMove(
        @RequestParam(value = "pos", required = true) String ascii,
        @RequestParam(value = "to_move", required = true) String to_move,
        @RequestParam(value = "move", required = true) String move
    ) {
        log.warn(String.format("Got: pos=%s", ascii));
        log.warn(String.format("Got: to_move=[%s]", to_move));
        log.warn(String.format("Got: move=[%s]", move));

        assert(ascii.length() == Position.FIELDS);
        assert(to_move.equals("X") || to_move.equals("O"));
        assert(move.length() == 2);

        Position pos = Position
            .fromString(ascii, Color.valueOf(to_move))
            .applyMove(Field.valueOf(move));

        Map<String, Object> ret_values = new HashMap<>();
        ret_values.put("pos", pos.toAscii());
        ret_values.put("moves", pos.legalMoves());

        return ret_values;
    }
}
