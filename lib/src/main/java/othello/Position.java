package othello;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Position {
    public final static int ROWS = 8;
    public final static int COLS = 8;
    public final static int FIELDS = ROWS * COLS;

    private Color to_move = Color.X;
    private Square[] board = new Square[FIELDS];

    private Position() {
        for (int i = 0; i < FIELDS; ++i){
            board[i] = Square.EMPTY;
        }

        board[3*8+3] = board[4*8+4] = Square.O;
        board[4*8+3] = board[3*8+4] = Square.X;
    }

    private Position(Square[] board, Color to_move) {
        assert board.length == FIELDS;
        assert to_move != null;

        this.board = board;
        this.to_move = to_move;
    }

    // Factory methods

    public static Position initialPosition() {
        return new Position();
    }

    public Square[] getBoard() {
        return board;
    }

    public static Position fromString(String ascii, Color to_move) {
        Square[] board = ascii
            .chars()
            .mapToObj(c -> (char) c)
            .filter(c -> ".XO".indexOf(c) != -1)
            .map(Square::fromChar)
            .collect(Collectors.toList())
            .toArray(new Square[0]);

        assert board.length == FIELDS;

        return new Position(board, to_move);
    }

    public Position copy() {
        Position pos = Position.fromString(this.toString(), this.to_move);
        pos.to_move = this.to_move;
        return pos;
    }

    // Normal methods

    public int getMoveCount() {
        return 0;
    }

    public Color getPlayerToMove() {
        return to_move;
    }

    public int getOccupiedNum() {
        return 4;
    }

    public List<Field> empty_fields() {
        return Field.allFields()
            .stream()
            .filter(field -> getSquareAtField(field) == Square.EMPTY)
            .collect(Collectors.toList());
    }

    public Square getSquareAtField(Field field) {
        int index = field.toIndex();
        return board[index];
    }

    private int calculateDifference() {
        int X = 0;
        int O = 0;

        for (Square square : board) {
            if (square == Square.X)
                X++;
            else if (square == Square.O)
                O++;
        }
        return X - O;
    }

    public String formatResult(int difference) {
        if (difference == 0)
            return "draw";

        // X + O == 64
        // X - O == difference
        int X = (FIELDS + difference) / 2;
        int O = FIELDS - X;

        String winner = difference > 0 ? "X" : "O";
        return String.format("%s wins | X=%d vs O=%d",
            winner, X, O);
    }

    private Color getWinner(int difference) {
        if (difference == 0)
            return null;
        if (difference > 0)
            return Color.X;
        else
            return Color.O;
    }

    public Status generateStatus() {
        final List<Field> moves = legalMoves();
        if (moves.size() > 0) {
            return Status.MovesAvailable(moves);
        }

        // current player has to pass, try to change color
        final List<Field> moves_after_pass = legalMovesAfterPass();
        if (moves_after_pass.size() > 0) {
            return Status.OneSidedPass(moves_after_pass);
        }

        int difference = calculateDifference();
        final Color winner = getWinner(difference);
        return Status.GameFinished(difference, winner);
    }

    private List<Field> generateMoves(Color to_move) {
        return new MoveGenerator(to_move, this.board).legalMoves();
    }

    private List<Field> legalMovesAfterPass() {
        return generateMoves(this.to_move.opposite());
    }

    public List<Field> legalMoves() {
        return generateMoves(this.to_move);
    }

    public void makePass() {
        this.to_move = this.to_move.opposite();
    }

    public Position applyMove(Field move) {
        Position result = this.copy();
        result.makeMove(move);

        assert result.to_move == this.to_move.opposite();

        return result;
    }

    public void makeMove(Field move) {
        List<Integer> to_flip = new MoveGenerator(to_move, board).makeMove(move);

        for (int i : to_flip) {
            board[i] = board[i].flip();
        }

        board[move.toIndex()] = to_move.toSquare();
        to_move = to_move.opposite();
    }

    public String toAscii() {
        String result = "";
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; ++col) {
                Square square = board[row*8 + col];
                result += square.toString();
            }
        }
        return result;
    }

    @Override
    public String toString() {
        String result = "";
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; ++col) {
                Square square = board[row*8 + col];
                result += square.toString();
                if (col < COLS-1) {
                    result += " ";
                }
            }
            result += "\n";
        }
        return result;
    }
}

class MoveGenerator {
    public static final int ROWS = Position.ROWS + 2;
    public static final int COLS = Position.COLS + 2;
    public final static int FIELDS = ROWS * COLS;

    // board with a sentinel frame around (consisting of nulls)
    Square[][] board = new Square[ROWS][];
    Color to_move;

    public MoveGenerator(Color to_move, Square[] board) {
        this.to_move = to_move;

        for (int row = 0; row < ROWS; row++)
            this.board[row] = new Square[COLS];

        for (int row = 0; row < Position.ROWS; row++)
            for (int col = 0; col < Position.COLS; col++)
                this.board[row+1][col+1] = board[row*8 +col];
    }

    public List<Integer> makeMove(Field move) {
        List<Integer> indexes_to_flip = new ArrayList<Integer>();

        int row = 1 + move.toRow();
        int col = 1 + move.toCol();

        for (int delta_row: List.of(-1, 0, 1))
            for (int delta_col: List.of(-1, 0, 1))
                if (delta_row != 0 || delta_col != 0) {
                    List<Integer> indexes = tryCaptureOnLine(row, col, delta_row, delta_col);
                    if (indexes != null)
                        indexes_to_flip.addAll(indexes);
                }

        return indexes_to_flip;
    }

    private List<Integer> tryCaptureOnLine(int row, int col, int delta_row, int delta_col) {
        List<Integer> indexes_to_capture = new ArrayList<Integer>();
        int x = row + delta_row;
        int y = col + delta_col;

        final Square opp_square = to_move.opposite().toSquare();
        final Square own_square = to_move.toSquare();

        while (board[x][y] == opp_square) {
            indexes_to_capture.add((x-1)*8 + (y-1));

            x += delta_row;
            y += delta_col;
        }

        // we need to have our stone at the other side of the line with no gaps
        if (board[x][y] == own_square)
            return indexes_to_capture;
        else
            return null;
    }

    public List<Field> legalMoves() {
        List<Field> legal_moves = new ArrayList<Field>();

        for (int row = 0; row < MoveGenerator.ROWS; row++)
            for (int col = 0; col < MoveGenerator.COLS; col++)
                if (board[row][col] == Square.EMPTY)
                    if (isCapturePossible(row, col))
                        legal_moves.add(Field.fromRowCol(row-1, col-1));

        return legal_moves;
    }

    private boolean isCapturePossible(int row, int col) {
        for (int delta_row: List.of(-1, 0, 1))
            for (int delta_col: List.of(-1, 0, 1))
                if (delta_row != 0 || delta_col != 0)
                    if (isCapturePossibleOnLine(row, col, delta_row, delta_col))
                        return true;
        return false;
    }

    // Checks if we can flip any stones one the given line
    private boolean isCapturePossibleOnLine(int row, int col, int delta_row, int delta_col) {
        assert board[row][col] == Square.EMPTY;

        List<Color> colors_on_diagonal = traverseLine(row, col, delta_row, delta_col);
        if (colors_on_diagonal.isEmpty())
            return false;

        Color opponent = to_move.opposite();
        return colors_on_diagonal.get(0) == opponent && colors_on_diagonal.contains(to_move);
    }

    private List<Color> traverseLine(int row, int col, int delta_row, int delta_col) {
        List<Color> colors_on_diagonal = new ArrayList<Color>();
        int x = row + delta_row;
        int y = col + delta_col;

        while (board[x][y] != null && board[x][y] != Square.EMPTY) {
            colors_on_diagonal.add(board[x][y].toColor());

            x += delta_row;
            y += delta_col;
        }

        return colors_on_diagonal;
    }
}