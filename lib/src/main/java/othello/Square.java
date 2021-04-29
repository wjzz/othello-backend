package othello;

public enum Square {
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
