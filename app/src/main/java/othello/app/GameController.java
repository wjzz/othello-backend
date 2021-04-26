package othello.app;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GameController {
    @GetMapping("/hello")
    public String index() {
        return "hello";
    }

    // @GetMapping("/make_move")
    // public MakeMoveResponse make_move() {

    // }
}
