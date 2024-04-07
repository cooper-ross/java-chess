import javax.swing.ImageIcon;

public class Piece {
    public static final byte EMPTY = 0;
    public static final byte PAWN = 1;
    public static final byte KNIGHT = 2;
    public static final byte BISHOP = 3;
    public static final byte ROOK = 4;
    public static final byte QUEEN = 5;
    public static final byte KING = 6;

    private static final ImageIcon WHITE_PAWN = new ImageIcon(
            ClassLoader.getSystemResource("textures/pieces/white_pawn.png"));
    private static final ImageIcon WHITE_KNIGHT = new ImageIcon(
            ClassLoader.getSystemResource("textures/pieces/white_knight.png"));
    private static final ImageIcon WHITE_BISHOP = new ImageIcon(
            ClassLoader.getSystemResource("textures/pieces/white_bishop.png"));
    private static final ImageIcon WHITE_ROOK = new ImageIcon(
            ClassLoader.getSystemResource("textures/pieces/white_rook.png"));
    private static final ImageIcon WHITE_QUEEN = new ImageIcon(
            ClassLoader.getSystemResource("textures/pieces/white_queen.png"));
    private static final ImageIcon WHITE_KING = new ImageIcon(
            ClassLoader.getSystemResource("textures/pieces/white_king.png"));

    private static final ImageIcon BLACK_PAWN = new ImageIcon(
            ClassLoader.getSystemResource("textures/pieces/black_pawn.png"));
    private static final ImageIcon BLACK_KNIGHT = new ImageIcon(
            ClassLoader.getSystemResource("textures/pieces/black_knight.png"));
    private static final ImageIcon BLACK_BISHOP = new ImageIcon(
            ClassLoader.getSystemResource("textures/pieces/black_bishop.png"));
    private static final ImageIcon BLACK_ROOK = new ImageIcon(
            ClassLoader.getSystemResource("textures/pieces/black_rook.png"));
    private static final ImageIcon BLACK_QUEEN = new ImageIcon(
            ClassLoader.getSystemResource("textures/pieces/black_queen.png"));
    private static final ImageIcon BLACK_KING = new ImageIcon(
            ClassLoader.getSystemResource("textures/pieces/black_king.png"));

    public static boolean isWhite(byte piece) {
        return (Math.abs(piece) == piece);
    }

    public static boolean orthogonalMovesAllowed(byte piece) {
        return (Math.abs(piece) == ROOK || Math.abs(piece) == QUEEN);
    }

    public static boolean diagonalMovesAllowed(byte piece) {
        return (Math.abs(piece) == BISHOP || Math.abs(piece) == QUEEN);
    }

    public static boolean kingMovesAllowed(byte piece) {
        return (Math.abs(piece) == KING);
    }

    public static boolean pawnMovesAllowed(byte piece) {
        return (Math.abs(piece) == PAWN);
    }

    public static byte mapPieceSymbol(char symbol) {
        switch (symbol) {
            case 'p':
                return -PAWN;
            case 'n':
                return -KNIGHT;
            case 'b':
                return -BISHOP;
            case 'r':
                return -ROOK;
            case 'q':
                return -QUEEN;
            case 'k':
                return -KING;
            case 'P':
                return PAWN;
            case 'N':
                return KNIGHT;
            case 'B':
                return BISHOP;
            case 'R':
                return ROOK;
            case 'Q':
                return QUEEN;
            default:
                return KING;
        }
    }

    public static ImageIcon mapPieceTexture(byte id) {
        switch (id) {
            case 1:
                return WHITE_PAWN;
            case 2:
                return WHITE_KNIGHT;
            case 3:
                return WHITE_BISHOP;
            case 4:
                return WHITE_ROOK;
            case 5:
                return WHITE_QUEEN;
            case 6:
                return WHITE_KING;
            case -1:
                return BLACK_PAWN;
            case -2:
                return BLACK_KNIGHT;
            case -3:
                return BLACK_BISHOP;
            case -4:
                return BLACK_ROOK;
            case -5:
                return BLACK_QUEEN;
            default:
                return BLACK_KING;
        }
    }

    public static double getPieceValue(byte piece) {
        /*
         * Piece Square Tables, adapted from Sunfish.py:
         * https://github.com/thomasahle/sunfish/blob/master/sunfish.py
         */
        switch (piece) {
            case 1:
                return 100;
            case 2:
                return 320;
            case 3:
                return 320;
            case 4:
                return 479;
            case 5:
                return 929;
            case 6:
                return 60000;
            case -1:
                return -100;
            case -2:
                return -320;
            case -3:
                return -320;
            case -4:
                return -479;
            case -5:
                return -929;
            case -6:
                return -60000;
            default:
                return 0;
        }
    }

}