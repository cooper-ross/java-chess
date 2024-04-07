import java.util.Arrays;

public class PieceSquareTables {
	/* 
	 * Piece Square Tables, adapted (with significant variation) from Sunfish.py:
	 * https://github.com/thomasahle/sunfish/blob/master/sunfish.py
	 */
    public static byte[] pawnEvalWhite = {
        120,120,110,110,110,110,120,120,
        60, 52, 50, 50, 50, 50, 52, 60,
        15, 12, 20, 30, 30, 20, 12, 15,
         5,  5, 10, 20, 20, 10,  5,  5,
         0,  0,  0, 25, 25,  0,  0,  0,
         5, 5,-10,  15, 15,-10,  5,  5,
         5, 10, 10,-15,-15, 10, 10,  5,
         0,  0,  0,  0,  0,  0,  0,  0
    };
    public static byte[] pawnEvalBlack = Utils.reverseByteArray(Arrays.copyOf(pawnEvalWhite, pawnEvalWhite.length), (byte)(pawnEvalWhite.length));
    
	public static byte[] knightEvalWhite = {
        -50,-40,-30,-30,-30,-30,-40,-50,
        -40,-20,  0,  0,  0,  0,-20,-40,
        -30,  0, 10, 15, 15, 10,  0,-30,
        -30,  5, 15, 20, 20, 15,  5,-30,
        -30,  0, 15, 20, 20, 15,  0,-30,
        -30,  5, 10, 15, 15, 10,  5,-30,
        -40,-20,  0,  5,  5,  0,-20,-40,
        -50,-40,-30,-30,-30,-30,-40,-50
    };
    public static byte[] knightEvalBlack = Utils.reverseByteArray(Arrays.copyOf(knightEvalWhite, knightEvalWhite.length), (byte)(knightEvalWhite.length));
    
	public static byte[] bishopEvalWhite = {
        -20,-10,-10,-10,-10,-10,-10,-20,
        -10,  0,  0,  0,  0,  0,  0,-10,
        -10,  0,  5, 10, 10,  5,  0,-10,
        -10,  5,  5, 10, 10,  5,  5,-10,
        -10,  0, 10, 10, 10, 10,  0,-10,
        -10, 10, 10, 10, 10, 10, 10,-10,
        -10,  5,  0,  0,  0,  0,  5,-10,
        -20,-10,-10,-10,-10,-10,-10,-20
    };
	public static byte[] bishopEvalBlack = Utils.reverseByteArray(Arrays.copyOf(bishopEvalWhite, bishopEvalWhite.length), (byte)(bishopEvalWhite.length));

    public static byte[] rookEvalWhite = {
      0,  0,  0,  0,  0,  0,  0,  0,
      5, 10, 10, 10, 10, 10, 10,  5,
     -5,  0,  0,  0,  0,  0,  0, -5,
     -5,  0,  0,  0,  0,  0,  0, -5,
     -5,  0,  0,  0,  0,  0,  0, -5,
     -5,  0,  0,  0,  0,  0,  0, -5,
     -5,  0,  0,  0,  0,  0,  0, -5,
     -5, 0,  2,  5,  5,  2,  0, -5
    };
    public static byte[] rookEvalBlack = Utils.reverseByteArray(Arrays.copyOf(rookEvalWhite, rookEvalWhite.length), (byte)(rookEvalWhite.length));
    
	public static byte[] queenEvalWhite = {
        -20,-10,-10,-10,-10,-10,-10,-20,
        -10,  0,  0,  0,  0,  0,  0,-10,
        -10,  0,  5,  5,  5,  5,  0,-10,
         -5,  0,  5,  5,  5,  5,  0, -5,
         -5,  0,  5,  5,  5,  5,  0, -5,
        -10,  5,  5,  5,  5,  5,  5,-10,
        -10,  0,  5,  0,  0,  0,  0,-10,
        -20,-10,-10,  1,  1,-10,-10,-20
    };
    public static byte[] queenEvalBlack = Utils.reverseByteArray(Arrays.copyOf(queenEvalWhite, queenEvalWhite.length), (byte)(queenEvalWhite.length));
    
	public static byte[] kingEvalWhite = {
        -30,-40,-40,-50,-50,-40,-40,-30,
        -30,-40,-40,-50,-50,-40,-40,-30,
        -30,-40,-40,-50,-50,-40,-40,-30,
        -30,-40,-40,-50,-50,-40,-40,-30,
        -20,-30,-30,-40,-40,-30,-30,-20,
        -10,-20,-20,-20,-20,-20,-20,-10,
         20, 20,  0,  0,  0,  0, 20, 20,
         20, 21, 10,  0,  0, 10, 21, 20
    };
    public static byte[] kingEvalBlack = Utils.reverseByteArray(Arrays.copyOf(kingEvalWhite, kingEvalWhite.length), (byte)(kingEvalWhite.length));

	public static byte[] kingEvalWhiteEndgame = {
		-50,-40,-30,-20,-20,-30,-40,-50,
		-30,-20,-10,  0,  0,-10,-20,-30,
		-30,-10, 20, 30, 30, 20,-10,-30,
		-30,-10, 30, 40, 40, 30,-10,-30,
		-30,-10, 30, 40, 40, 30,-10,-30,
		-30,-10, 20, 30, 30, 20,-10,-30,
		-30,-30,  0,  0,  0,  0,-30,-30,
		-50,-30,-30,-30,-30,-30,-30,-50
	};
	public static byte[] kingEvalBlackEndgame = Utils.reverseByteArray(Arrays.copyOf(kingEvalWhiteEndgame, kingEvalWhiteEndgame.length), (byte)(kingEvalWhiteEndgame.length));


	public static int getValueOfAt(byte piece, byte position, int piecesOnBoard) {
        switch (piece) {
            case 1:
                return pawnEvalWhite[position];
            case 2:
                return knightEvalWhite[position];
            case 3:
                return bishopEvalWhite[position];
            case 4:
                return rookEvalWhite[position];
            case 5:
                return queenEvalWhite[position];
            case 6:
				if (piecesOnBoard < 12) {
					return kingEvalWhiteEndgame[position];
				}
                return kingEvalWhite[position];
            case -1:
                return -pawnEvalBlack[position];
            case -2:
                return -knightEvalBlack[position];
            case -3:
                return -bishopEvalBlack[position];
			case -4:
                return -rookEvalBlack[position];
            case -5:
                return -queenEvalBlack[position];
            case -6:
				if (piecesOnBoard < 12) {
					return -kingEvalBlackEndgame[position];
				}
                return -kingEvalBlack[position];
			default:
                return 0;
        }
    }
}