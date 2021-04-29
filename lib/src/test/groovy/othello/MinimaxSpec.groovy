package othello

import static othello.Field.*
import othello.game.MinimaxPlayer

import spock.lang.*

class MinimaxSpec extends Specification {

    //--------------------------------------------------------
    // Depth 0
    //--------------------------------------------------------

    def "minimax 0: final position 1 - draw"() {
        given:
            def pos_str = """\
            O O O O X X X X
            O O O O X X X X
            O O O O X X X X
            O O O O X X X X
            O O O O X X X X
            O O O O X X X X
            O O O O X X X X
            O O O O X X X X
            """
            def pos = Position.fromString(pos_str, Color.X)
            def depth = 0
            def minimax = new MinimaxPlayer("test", depth)
        when:
            def evaluation = minimax.eval(pos, depth)
        then:
            pos.generateStatus().isGameFinished()
            evaluation == 0
   }

   def "minimax 0: final position 2 - (current player) X wins"() {
        given:
            def pos_str = """\
            O O O O X X X X
            O O O O X X X X
            O O O O X X X X
            O O O O X X X X
            O O O O X X X X
            O O O O X X X X
            O O O O X X X X
            O O O X X X X X
            """
            def pos = Position.fromString(pos_str, Color.X)
            def depth = 0
            def minimax = new MinimaxPlayer("test", depth)
        when:
            def evaluation = minimax.eval(pos, depth)
        then:
            pos.generateStatus().isGameFinished()
            evaluation == MinimaxPlayer.WIN_EVAL
   }

   def "minimax 0: final position 3 - (current player) X loses"() {
        given:
            def pos_str = """\
            O O O O X X X X
            O O O O X X X X
            O O O O X X X X
            O O O O X X X X
            O O O O X X X X
            O O O O X X X X
            O O O O X X X X
            O O O O O X X X
            """
            def pos = Position.fromString(pos_str, Color.X)
            def depth = 0
            def minimax = new MinimaxPlayer("test", depth)
        when:
            def evaluation = minimax.eval(pos, depth)
        then:
            pos.generateStatus().isGameFinished()
            evaluation == MinimaxPlayer.LOSS_EVAL
   }

   def "minimax 0: final position 4 - (current player) X wins - no more legal moves"() {
        given:
            def pos_str = """\
            O O O O O O X O
            X O O O X X X O
            O X O O X X X O
            O O X O X X X O
            O O O X X X X O
            O O O O X X X O
            X O O O X X X X
            X X X X X X . X
            """
            def pos = Position.fromString(pos_str, Color.X)
            def depth = 0
            def minimax = new MinimaxPlayer("test", depth)
        when:
            def evaluation = minimax.eval(pos, depth)
        then:
            pos.generateStatus().isGameFinished()
            evaluation == MinimaxPlayer.WIN_EVAL
   }

     def "minimax 0: final position 5 - (current player) O loses - no more legal moves"() {
        given:
            def pos_str = """\
            O O O O O O X O
            X O O O X X X O
            O X O O X X X O
            O O X O X X X O
            O O O X X X X O
            O O O O X X X O
            X O O O X X X X
            X X X X X X . X
            """
            def pos = Position.fromString(pos_str, Color.O)
            def depth = 0
            def minimax = new MinimaxPlayer("test", depth)
        when:
            def evaluation = minimax.eval(pos, depth)
        then:
            pos.generateStatus().isGameFinished()
            evaluation == MinimaxPlayer.LOSS_EVAL
   }
}