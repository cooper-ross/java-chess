import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class PrecomputedData {
	public static byte[][][] orthogonalMoveData = precomputeOrthogonalMoves();
	public static byte[][][] diagonalMoveData = precomputeDiagonalMoves();
	public static byte[][][] queenMoveData = precomputeQueenMoveData();

	public static byte[][][] knightMoveData = precomputeKnightMoveData();
	public static byte[][][] kingMoveData = precomputeKingMoveData();

	public static byte[][][] whitePawnMoveData = precomputeWhitePawnMoveData();
	public static byte[][][] blackPawnMoveData = precomputeBlackPawnMoveData();

	public static byte[][][] kingCheckData = precomputeKingCheckData();

	public static byte[][][] precomputeOrthogonalMoves() {
		byte[][][] orthogonalMoveData = new byte[64][4][];
		for (byte i = 0; i < 64; i++) {
			orthogonalMoveData[i] = calculateOrthogonalFromIndex(i);
		}
		return orthogonalMoveData;
	}

	public static byte[][][] precomputeDiagonalMoves() {
		byte[][][] diagonalMoveData = new byte[64][4][];
		for (byte i = 0; i < 64; i++) {
			diagonalMoveData[i] = calculateDiagonalFromIndex(i);
		}
		return diagonalMoveData;
	}

	public static byte[][][] precomputeQueenMoveData() {
		byte[][][] queenMoveData = new byte[64][8][];
		for (byte i = 0; i < 64; i++) {
			queenMoveData[i] = calculateQueenMovesFromIndex(i);
		}
		return queenMoveData;
	}

	public static byte[][][] precomputeKnightMoveData() {
		byte[][][] knightMoveData = new byte[64][8][];
		for (byte i = 0; i < 64; i++) {
			knightMoveData[i] = calculateKnightMovesFromIndex(i);
		}
		return knightMoveData;
	}

	public static byte[][][] precomputeKingMoveData() {
		byte[][][] kingMoveData = new byte[64][8][];
		for (byte i = 0; i < 64; i++) {
			kingMoveData[i] = calculateKingMovesFromIndex(i);
		}
		return kingMoveData;
	}

	public static byte[][][] precomputeWhitePawnMoveData() {
		byte[][][] whitePawnMoveData = new byte[64][8][];
		for (byte i = 0; i < 64; i++) {
			whitePawnMoveData[i] = calculateWhitePawnMovesFromIndex(i);
		}
		return whitePawnMoveData;
	}

	public static byte[][][] precomputeBlackPawnMoveData() {
		byte[][][] blackPawnMoveData = new byte[64][8][];
		for (byte i = 0; i < 64; i++) {
			blackPawnMoveData[i] = calculateBlackPawnMovesFromIndex(i);
		}
		return blackPawnMoveData;
	}

	public static byte[][][] precomputeKingCheckData() {
		byte[][][] kingCheckData = new byte[64][16][];
		for (byte i = 0; i < 64; i++) {
			kingCheckData[i] = calculateKingCheckDataFromIndex(i);
		}
		return kingCheckData;
	}

	public static byte[][] calculateOrthogonalFromIndex(byte index) {
		byte column = (byte) (Math.floor(index / 8));
		byte row = (byte) (index % 8);

		byte[] right = new byte[7 - row];
		for (byte i = 1; i < 8 - row; i++) {
			right[i - 1] = (byte) (i + row + (column * 8));
		}

		byte[] left = new byte[row];
		for (byte i = 1; i - 1 < row; i++) {
			left[i - 1] = (byte) (-i + row + (column * 8));
		}

		byte[] up = new byte[column];
		for (byte i = 1; i - 1 < column; i++) {
			up[i - 1] = (byte) (row + ((column + -i) * 8));
		}

		byte[] down = new byte[7 - column];
		for (byte i = 1; i < 8 - column; i++) {
			down[i - 1] = (byte) (row + ((column + i) * 8));
		}

		return new byte[][] { up, down, left, right };
	}

	public static byte[][] calculateDiagonalFromIndex(byte index) {
		byte column = (byte) (Math.floor(index / 8));
		byte row = (byte) (index % 8);

		byte[] upRight = new byte[Math.min(7 - column, 7 - row)];
		for (byte i = 1; i <= Math.min(7 - column, 7 - row); i++) {
			upRight[i - 1] = (byte) (index + i * 8 + i);
		}

		byte[] upLeft = new byte[Math.min(7 - column, row)];
		for (byte i = 1; i <= Math.min(7 - column, row); i++) {
			upLeft[i - 1] = (byte) (index + i * 8 - i);
		}

		byte[] downRight = new byte[Math.min(column, 7 - row)];
		for (byte i = 1; i <= Math.min(column, 7 - row); i++) {
			downRight[i - 1] = (byte) (index - i * 8 + i);
		}

		byte[] downLeft = new byte[Math.min(column, row)];
		for (byte i = 1; i <= Math.min(column, row); i++) {
			downLeft[i - 1] = (byte) (index - i * 8 - i);
		}

		return new byte[][] { upRight, upLeft, downRight, downLeft };
	}

	public static byte[][] calculateQueenMovesFromIndex(byte index) {
		byte[][] queenMoves = new byte[8][];

		byte[][] orthogonal = calculateOrthogonalFromIndex(index);
		queenMoves[0] = orthogonal[0];
		queenMoves[1] = orthogonal[1];
		queenMoves[2] = orthogonal[2];
		queenMoves[3] = orthogonal[3];

		byte[][] diagonal = calculateDiagonalFromIndex(index);
		queenMoves[4] = diagonal[0];
		queenMoves[5] = diagonal[1];
		queenMoves[6] = diagonal[2];
		queenMoves[7] = diagonal[3];

		return queenMoves;
	}

	public static byte[][] calculateKnightMovesFromIndex(byte index) {
		byte[][] knightMoves = new byte[8][];

		byte[] xOffset = { 2, 1, -1, -2, -2, -1, 1, 2 };
		byte[] yOffset = { 1, 2, 2, 1, -1, -2, -2, -1 };

		byte row = (byte) (Math.floor(index / 8)); // Assuming a 1-dimensional board is mapped to 8x8 grid.
		byte col = (byte) (index % 8);

		for (byte i = 0; i < 8; i++) {
			byte newRow = (byte) (row + yOffset[i]);
			byte newCol = (byte) (col + xOffset[i]);

			// Check if the new position is within the board boundaries
			if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
				byte newIndex = (byte) (newRow * 8 + newCol);
				byte[] moveArray = { newIndex };
				knightMoves[i] = moveArray;
			} else {
				knightMoves[i] = new byte[0];
			}
		}

		return knightMoves;
	}

	public static byte[][] calculateKingMovesFromIndex(byte index) {
		byte[][] kingMoves = new byte[8][];

		byte[] xOffset = { 1, 1, 1, -1, -1, -1, 0, 0 };
		byte[] yOffset = { 1, 0, -1, 1, 0, -1, 1, -1 };

		byte row = (byte) (Math.floor(index / 8)); // Assuming a 1-dimensional board is mapped to 8x8 grid.
		byte col = (byte) (index % 8);

		for (byte i = 0; i < 8; i++) {
			byte newRow = (byte) (row + yOffset[i]);
			byte newCol = (byte) (col + xOffset[i]);

			// Check if the new position is within the board boundaries
			if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
				byte newIndex = (byte) (newRow * 8 + newCol);
				byte[] moveArray = { newIndex };
				kingMoves[i] = moveArray;
			} else {
				kingMoves[i] = new byte[0];
			}
		}

		return kingMoves;
	}

	public static byte[][] calculateWhitePawnMovesFromIndex(byte index) {
		byte[][] whitePawnMoves = new byte[5][];

		byte[] xOffset = { 0, 0 };
		byte[] yOffset = { -1, -2 };

		byte row = (byte) (index / 8);
		byte col = (byte) (index % 8);

		byte moveForwardCount = (row == 6) ? (byte) (2) : (byte) (1);
		byte[] pawnMovesForward = new byte[moveForwardCount];

		// Forward Moves
		for (byte i = 0; i < moveForwardCount; i++) {
			byte newRow = (byte) (row + yOffset[i]);
			byte newCol = (byte) (col + xOffset[i]);

			// Check if the new position is within the board boundaries
			if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
				byte newIndex = (byte) (newRow * 8 + newCol);
				pawnMovesForward[i] = newIndex;
			} else {
				pawnMovesForward = new byte[0];
				break;
			}
		}
		whitePawnMoves[0] = pawnMovesForward;

		// Check for enPassant
		byte checkRightCol = (byte) (col + 1);
		if (checkRightCol >= 0 && checkRightCol < 8) {
			byte newIndex = (byte) (row * 8 + checkRightCol);
			byte[] moveArray = { newIndex };
			whitePawnMoves[1] = moveArray;
		} else {
			whitePawnMoves[1] = new byte[0];
		}

		byte checkLeftCol = (byte) (col - 1);
		if (checkLeftCol >= 0 && checkLeftCol < 8) {
			byte newIndex = (byte) (row * 8 + checkLeftCol);
			byte[] moveArray = { newIndex };
			whitePawnMoves[2] = moveArray;
		} else {
			whitePawnMoves[2] = new byte[0];
		}

		byte diaAtkLeftRow = (byte) (row - 1);
		byte diaAtkLeftCol = (byte) (col - 1);
		if (diaAtkLeftRow >= 0 && diaAtkLeftRow < 8 && diaAtkLeftCol >= 0 && diaAtkLeftCol < 8) {
			byte newIndex = (byte) (diaAtkLeftRow * 8 + diaAtkLeftCol);
			byte[] moveArray = { newIndex };
			whitePawnMoves[3] = moveArray;
		} else {
			whitePawnMoves[3] = new byte[0];
		}

		byte diaAtkRightRow = (byte) (row - 1);
		byte diaAtkRightCol = (byte) (col + 1);
		if (diaAtkRightRow >= 0 && diaAtkRightRow < 8 && diaAtkRightCol >= 0 && diaAtkRightCol < 8) {
			byte newIndex = (byte) (diaAtkRightRow * 8 + diaAtkRightCol);
			byte[] moveArray = { newIndex };
			whitePawnMoves[4] = moveArray;
		} else {
			whitePawnMoves[4] = new byte[0];
		}

		return whitePawnMoves;
	}

	public static byte[][] calculateBlackPawnMovesFromIndex(byte index) {
		byte[][] blackPawnMoves = new byte[5][];

		byte[] xOffset = { 0, 0 };
		byte[] yOffset = { 1, 2 };

		byte row = (byte) (index / 8);
		byte col = (byte) (index % 8);

		byte moveForwardCount = (row == 1) ? (byte) (2) : (byte) (1);
		byte[] pawnMovesForward = new byte[moveForwardCount];

		// Forward Moves
		for (byte i = 0; i < moveForwardCount; i++) {
			byte newRow = (byte) (row + yOffset[i]);
			byte newCol = (byte) (col + xOffset[i]);

			// Check if the new position is within the board boundaries
			if (newRow >= 0 && newRow < 8 && newCol >= 0 && newCol < 8) {
				byte newIndex = (byte) (newRow * 8 + newCol);
				pawnMovesForward[i] = newIndex;
			} else {
				pawnMovesForward = new byte[0];
				break;
			}
		}
		blackPawnMoves[0] = pawnMovesForward;

		// Check for enPassant
		byte checkRightCol = (byte) (col + 1);
		if (checkRightCol >= 0 && checkRightCol < 8) {
			byte newIndex = (byte) (row * 8 + checkRightCol);
			byte[] moveArray = { newIndex };
			blackPawnMoves[1] = moveArray;
		} else {
			blackPawnMoves[1] = new byte[0];
		}

		byte checkLeftCol = (byte) (col - 1);
		if (checkLeftCol >= 0 && checkLeftCol < 8) {
			byte newIndex = (byte) (row * 8 + checkLeftCol);
			byte[] moveArray = { newIndex };
			blackPawnMoves[2] = moveArray;
		} else {
			blackPawnMoves[2] = new byte[0];
		}

		// a
		byte diaAtkLeftRow = (byte) (row + 1);
		byte diaAtkLeftCol = (byte) (col - 1);
		if (diaAtkLeftRow >= 0 && diaAtkLeftRow < 8 && diaAtkLeftCol >= 0 && diaAtkLeftCol < 8) {
			byte newIndex = (byte) (diaAtkLeftRow * 8 + diaAtkLeftCol);
			byte[] moveArray = { newIndex };
			blackPawnMoves[3] = moveArray;
		} else {
			blackPawnMoves[3] = new byte[0];
		}

		byte diaAtkRightRow = (byte) (row + 1);
		byte diaAtkRightCol = (byte) (col + 1);
		if (diaAtkRightRow >= 0 && diaAtkRightRow < 8 && diaAtkRightCol >= 0 && diaAtkRightCol < 8) {
			byte newIndex = (byte) (diaAtkRightRow * 8 + diaAtkRightCol);
			byte[] moveArray = { newIndex };
			blackPawnMoves[4] = moveArray;
		} else {
			blackPawnMoves[4] = new byte[0];
		}

		return blackPawnMoves;
	}

	public static byte[][] calculateKingCheckDataFromIndex(byte index) {
		byte[][] kingCheckData = new byte[28][];

		byte[][] orthogonal = calculateOrthogonalFromIndex(index);
		kingCheckData[0] = orthogonal[0];
		kingCheckData[1] = orthogonal[1];
		kingCheckData[2] = orthogonal[2];
		kingCheckData[3] = orthogonal[3];

		byte[][] diagonal = calculateDiagonalFromIndex(index);
		kingCheckData[4] = diagonal[0];
		kingCheckData[5] = diagonal[1];
		kingCheckData[6] = diagonal[2];
		kingCheckData[7] = diagonal[3];

		byte[][] knight = calculateKnightMovesFromIndex(index);
		kingCheckData[8] = knight[0];
		kingCheckData[9] = knight[1];
		kingCheckData[10] = knight[2];
		kingCheckData[11] = knight[3];
		kingCheckData[12] = knight[4];
		kingCheckData[13] = knight[5];
		kingCheckData[14] = knight[6];
		kingCheckData[15] = knight[7];

		byte[][] king = calculateKingMovesFromIndex(index);
		kingCheckData[16] = king[0];
		kingCheckData[17] = king[1];
		kingCheckData[18] = king[2];
		kingCheckData[19] = king[3];
		kingCheckData[20] = king[4];
		kingCheckData[21] = king[5];
		kingCheckData[22] = king[6];
		kingCheckData[23] = king[7];

		// Pawn Attacks are really just the diagonal king attacks :}
		kingCheckData[24] = king[5];
		kingCheckData[25] = king[2];

		// Above are black, below are white
		kingCheckData[26] = king[3];
		kingCheckData[27] = king[0];

		return kingCheckData;
	}
}