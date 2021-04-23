package othello;

import java.util.Arrays;
import java.util.List;

/*

dark = X
white = O

    original
    coordinates                internal table representation

     a b c d e f g h          a  b  c  d  e  f  g  h
   1 . . . . . . . .      1   0  1  2  3  4  5  6  7
   2 . . . . . . . .      2   8  9 10 11 12 13 14 15
   3 . . . . . . . .      3  16 17 18 19 20 21 22 23
   4 . . . O X . . .      4  24 25 26 27 28 29 30 31
   5 . . . X O . . .      5  32 33 34 35 36 37 38 39
   6 . . . . . . . .      6  40 41 42 43 44 45 46 47
   7 . . . . . . . .      7  48 49 50 51 52 53 54 55
   8 . . . . . . . .      8  56 57 58 59 60 61 62 63
*/

public enum Field {
    A1, B1, C1, D1, E1, F1, G1, H1,
    A2, B2, C2, D2, E2, F2, G2, H2,
    A3, B3, C3, D3, E3, F3, G3, H3,
    A4, B4, C4, D4, E4, F4, G4, H4,
    A5, B5, C5, D5, E5, F5, G5, H5,
    A6, B6, C6, D6, E6, F6, G6, H6,
    A7, B7, C7, D7, E7, F7, G7, H7,
    A8, B8, C8, D8, E8, F8, G8, H8;

    public int toIndex() {
        return this.ordinal();
    }

    public static List<Field> allFields() {
        return Arrays.asList(Field.values());
    }

    public static Field fromRowCol(int row, int col) {
        return Field.allFields().get(8*row + col);
    }

    public int toRow() {
        return this.ordinal() / 8;
    }

    public int toCol() {
        return this.ordinal() % 8;
    }
}
