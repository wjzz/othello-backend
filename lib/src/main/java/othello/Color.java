package othello;

public enum Color {
    X, O;

    public Square toSquare() {
        if (this == X)
            return Square.X;
        else
            return Square.O;
    }

    public Color valueOfString(String ascii) {
        assert ascii == "X" || ascii == "O";

        return ascii == "X" ? Color.X : Color.O;
    }

    public Color opposite() {
        if (this == X)
            return Color.O;
        else
            return Color.X;
    }
}
