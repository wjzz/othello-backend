package othello;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

enum Square {
    EMPTY, X, O;

    @Override
    public String toString() {
        switch (this) {
            case EMPTY: return ".";
            case X: return "X";
            case O: return "O";
            default: return "IMPOSSIBLE CASE";
        }
    }

    public static Square fromChar(Character ascii) {
        assert ascii == '.' || ascii == 'X' || ascii == 'O';

        if (ascii == 'X')
            return Square.X;
        if (ascii == 'O')
            return Square.O;
        return Square.EMPTY;
    }

    public Square flip() {
        if (this == Square.EMPTY) {
            return Square.EMPTY;
        } else if (this == Square.X) {
            return Square.O;
        } else {
            return Square.X;
        }
    }

    public Color toColor() {
        assert this != EMPTY;

        switch(this) {
            case X:
                return Color.X;
            case O:
                return Color.O;
            case EMPTY:
            default:
                return null;
        }
    }
}

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

    private Position(Square[] board) {
        assert board.length == FIELDS;

        this.board = board;

        int played_moves = 0;
        for (Square square : board) {
            assert square != null;
            if (square != Square.EMPTY)
                played_moves++;
        }
        this.to_move = (played_moves % 2 == 0) ? Color.X : Color.O;
    }

    // Factory methods

    public static Position initialPosition() {
        return new Position();
    }

    public static Position fromString(String ascii) {
        Square[] board = ascii
            .chars()
            .mapToObj(c -> (char) c)
            .filter(c -> ".XO".indexOf(c) != -1)
            .map(Square::fromChar)
            .collect(Collectors.toList())
            .toArray(new Square[0]);

        assert board.length == FIELDS;

        return new Position(board);
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

    public List<Field> legalMoves() {
        return new MoveGenerator(to_move, board).legalMoves();
    }

    public void makeMove(Field move) {
        List<Integer> to_flip = new MoveGenerator(to_move, board).makeMove(move);

        for (int i : to_flip) {
            board[i] = board[i].flip();
        }

        board[move.toIndex()] = to_move.toSquare();
        to_move = to_move.opposite();
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