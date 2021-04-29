package othello

import static othello.Field.*
import spock.lang.*

class PositionSpec extends Specification {

    //--------------------------------------------------------
    // INITIAL POSITION SPECS
    //--------------------------------------------------------

    def "by default, the initial position is created"() {
        given:
            def pos = new Position()
        expect:
            pos.getMoveCount() == 0
    }

    def "X starts the game"() {
        given:
            def pos = Position.initialPosition()
        expect:
            pos.getPlayerToMove() == Color.X
    }

    def "only 4 fields are occupied at start"() {
        given:
            def pos = Position.initialPosition()
        expect:
            pos.getOccupiedNum() == 4
    }

    def "at first, A4 is empty"() {
        given:
            def pos = Position.initialPosition()
        expect:
            pos.getSquareAtField(A4) == Square.EMPTY
    }

    def "at first, only four fields are occupied, v2"() {
        given:
            def pos = Position.initialPosition()
            def fields = Field.allFields()
            def count = 0
            fields.each { field ->
                if (pos.getSquareAtField(field) != Square.EMPTY) {
                    count += 1
                }
            }
        expect:
            count == 4
    }

    def "at first, there are 60 empty fields"() {
        given:
            def pos = Position.initialPosition()
            def empty_fields = pos.empty_fields()
        expect:
            empty_fields.size() == 60
    }

    def "initialPosition toString"() {
        given:
            def pos = Position.initialPosition()
            def str = pos.toString()

            def expected = """\
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
            . . . O X . . .
            . . . X O . . .
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
            """

        expect:
            str == expected.stripIndent()
    }

    //--------------------------------------------------------
    // Move generation
    //--------------------------------------------------------

    def "the generated legal moves are correct: starting position"() {
        given:
            def pos = Position.initialPosition()
            def moves = pos.legalMoves().sort()
        expect:
            moves == [D3, C4, F5, E6]
    }

    def "the generated legal moves are correct: corner case 1"() {
        given:
            def pos_str = """\
            . O X . . X O .
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
            . O X . . X O .
            """

            def pos = Position.fromString(pos_str, Color.X)
            def moves = pos.legalMoves().sort()
        expect:
            moves == [A1, H1, A8, H8]
    }

    def "the generated legal moves are correct: corner case 2"() {
        given:
            def pos_str = """\
            . . . . . . . .
            O . . . . . . O
            X . . . . . . X
            . . . . . . . .
            . . . . . . . .
            X . . . . . . X
            O . . . . . . O
            . . . . . . . .
            """

            def pos = Position.fromString(pos_str, Color.X)
            def moves = pos.legalMoves().sort()
        expect:
            moves == [A1, H1, A8, H8]
    }

    def "the generated legal moves are correct: corner case 3"() {
        given:
            def pos_str = """\
            . O O O X O O O
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
            O O O X O O O .
            """

            def pos = Position.fromString(pos_str, Color.X)
            def moves = pos.legalMoves().sort()
        expect:
            moves == [A1, H8]
    }

    def "the generated legal moves are correct: corner case 4"() {
        given:
            def pos_str = """\
            . . . . . . . .
            O . . . . . . O
            X . . . . . . X
            O . . . . . . O
            X . . . . . . X
            X . . . . . . X
            O . . . . . . O
            . . . . . . . .
            """

            def pos = Position.fromString(pos_str, Color.X)
            def moves = pos.legalMoves().sort()
        expect:
            moves == [A1, H1, A8, H8]
    }

    def "the generated legal moves are correct: center 1"() {
        given:
            def pos_str = """\
            . . . . . . . .
            . . . O . . . .
            . . . O . . . .
            . . . O . . . .
            . . . O . . . .
            . . . X . . . .
            . . . O . . . X
            . . . X . . X X
            """

            def pos = Position.fromString(pos_str, Color.X)
            def moves = pos.legalMoves().sort()
        expect:
            moves == [D1]
    }

    def "the generated legal moves are correct: no moves"() {
        given:
            def pos_str = """\
            . . . . . . . .
            X . . . . . . .
            X . . . . . . .
            X . . . O . . .
            X . . O . O . .
            . . . . O . . .
            . . . . . . . .
            . . . . . . . .
            """
            def pos = Position.fromString(pos_str, Color.X)

        when:
            def moves = pos.legalMoves().sort()

        then:
            moves == []
    }

    def "the generated legal moves are correct: no moves 2"() {
        given:
            def pos_str = """\
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
            . O . . X . . .
            . O . X O X . .
            . O . . X . . .
            . . . . . . . .
            . . . . . . . .
            """
            def pos = Position.fromString(pos_str, Color.X)

        when:
            def moves = pos.legalMoves().sort()
        then:
            moves == []
    }

    //--------------------------------------------------------
    // Make move
    //--------------------------------------------------------

    def "make move is correct: starting position"() {
        given:
            def pos_str = """\
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
            . . . O X . . .
            . . . X O . . .
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
            """
            def pos = Position.fromString(pos_str, Color.X)

        when:
            pos.makeMove(D3)

        then:
            def result = """\
            . . . . . . . .
            . . . . . . . .
            . . . X . . . .
            . . . X X . . .
            . . . X O . . .
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
            """
            pos.toString() == result.stripIndent()
    }

    //--------------------------------------------------------
    // Playing the game
    //--------------------------------------------------------

    def "generateStatus for game in progress"() {
        given:
            def pos_str = """\
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
            . . . O X . . .
            . . . X O . . .
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
            """
            def pos = Position.fromString(pos_str, Color.X)

        when:
            def status = pos.generateStatus()
        then:
            status.difference == 0
            status.moves.sort() == [D3, C4, F5, E6]
            status.is_pass == false
            status.winner == null
    }

    def "generateStatus for X pass"() {
        given:
            def pos_str = """\
            . . . . . . . .
            . . . . . . . .
            . . . . . . . .
            . O . . X . . .
            . O . X O X . .
            . O . . X . . .
            . . . . . . . .
            . . . . . . . .
            """
            def pos = Position.fromString(pos_str, Color.X)

        when:
            def status = pos.generateStatus()
        then:
            status.difference == 0
            status.is_pass == true
            // moves for next player after pass
            status.moves.sort() == [E3, C5, G5, E7]
            status.winner == null
    }

     def "generateStatus for finished game"() {
        given:
            def pos_str = """\
            . . . . . . . .
            . . . . . . . .
            . O . . . . . .
            . O . . X . . .
            . O . X X X X .
            . O . . X . . .
            . . . . . . . .
            . . . . . . . .
            """
            def pos = Position.fromString(pos_str, Color.X)

        when:
            def status = pos.generateStatus()
        then:
            status.difference == 2
            status.is_pass == false
            status.moves.sort() == []
            status.winner == Color.X
    }
}

class MoveGeneratorSpec extends Specification {
    def "constructor doesn't raise"() {
        given:
            def pos = Position.initialPosition()
            def move_gen = new MoveGenerator(pos.getPlayerToMove(), pos.board)
        expect:
            1 == 1
    }

    def "the number of sentinels is correct"() {
        given:
            def pos = Position.initialPosition()
            def move_gen = new MoveGenerator(pos.getPlayerToMove(), pos.board)
            def count = 0
            move_gen.board.each { row ->
                row.each { s->
                    if (s == null) {
                        count += 1
                    }
                }
            }
        expect:
            count == 4 * (Position.ROWS + 1)
    }

    def "the generated legal moves are correct"() {
        given:
            def pos = Position.initialPosition()
            def move_gen = new MoveGenerator(pos.getPlayerToMove(), pos.board)
            def moves = move_gen.legalMoves().sort()
        expect:
            moves == [D3, C4, F5, E6]
    }
}