import java.util.ArrayList;
import java.util.List;

public class MoveGenerator {

	public static List<Move> generateMovesForIndex(byte index, Board board) {
		List<Move> legalMoves = new ArrayList<Move>();
		byte[] currentPosition = board.getCurrentPosition();

		byte startSquare = index;
		byte thisPieceType = currentPosition[startSquare];
		byte thisPieceTypeAbs = (byte) (Math.abs(thisPieceType));
		boolean castle = false;
		boolean promote = false;
		boolean enPassant = false;
		boolean pieceIsWhite = Piece.isWhite(thisPieceType);

		byte[][][] potentialMoves;
		switch (thisPieceTypeAbs) {
			case Piece.ROOK:
				potentialMoves = PrecomputedData.orthogonalMoveData;
				break;
			case Piece.BISHOP:
				potentialMoves = PrecomputedData.diagonalMoveData;
				break;
			case Piece.KNIGHT:
				potentialMoves = PrecomputedData.knightMoveData;
				break;
			case Piece.QUEEN:
				potentialMoves = PrecomputedData.queenMoveData;
				break;
			case Piece.KING:
				potentialMoves = PrecomputedData.kingMoveData;
				break;
			case Piece.PAWN:
				return handlePawnMoves(board, startSquare, legalMoves, currentPosition, thisPieceType);
			default:
				// This is just so Java doesn't flip out at me for not having a value for
				// potentialMoves
				potentialMoves = PrecomputedData.orthogonalMoveData;
				break;
		}

		byte[][] potentialMovesForIndex = potentialMoves[index];
		for (byte i = 0; i < potentialMovesForIndex.length; i++) {
			for (byte j = 0; j < potentialMovesForIndex[i].length; j++) {
				byte targetSquare = potentialMovesForIndex[i][j];
				byte capturedPieceType = currentPosition[targetSquare];
				boolean capturedPieceIsWhite = Piece.isWhite(capturedPieceType);

				if (capturedPieceType != Piece.EMPTY && capturedPieceIsWhite == pieceIsWhite) {
					break;
				}

				byte[] newPosition = currentPosition.clone();
				newPosition[targetSquare] = newPosition[startSquare];
				newPosition[startSquare] = 0;

				boolean kingWillBeInCheck = board.kingInCheck(pieceIsWhite, newPosition);
				if (kingWillBeInCheck && capturedPieceType != Piece.EMPTY && capturedPieceIsWhite != pieceIsWhite) {
					break;
				} else if (kingWillBeInCheck) {
					continue;
				}

				double capturedPieceValue = Piece.getPieceValue(capturedPieceType);

				Move move = new MoveBuilder()
						.startSquare(startSquare)
						.targetSquare(targetSquare)
						.thisPieceType(thisPieceType)
						.capturedPieceSquare(targetSquare)
						.capturedPieceType(capturedPieceType)
						.check(board.kingInCheck(!pieceIsWhite, newPosition))
						.captureValue(capturedPieceValue)
						.buildMove();

				legalMoves.add(move);

				if (capturedPieceType != Piece.EMPTY && capturedPieceIsWhite != pieceIsWhite) {
					break;
				}
			}
		}

		if (thisPieceTypeAbs == Piece.KING && (index == 4 || index == 60)) {
			legalMoves = checkCastleRights(board, startSquare, legalMoves, currentPosition, thisPieceType,
					pieceIsWhite);
		}

		return legalMoves;
	}

	private static List<Move> checkCastleRights(Board board, byte startSquare, List<Move> legalMoves,
			byte[] currentPosition, byte thisPieceType, boolean isWhite) {
		boolean isWhiteTurn = isWhite;

		byte lookForRookType = (isWhiteTurn) ? Piece.ROOK : -Piece.ROOK;
		byte rookL = (isWhiteTurn) ? (byte) (56) : (byte) (0);
		byte rookR = (isWhiteTurn) ? (byte) (63) : (byte) (7);

		boolean canCastleLeft = false;
		boolean canCastleRight = false;

		// If the king can move left or right (not going to be in check there)
		// Then we can castle, we will deal with checks for the destination squares
		// later
		for (byte i = (byte) (legalMoves.size() - 1); i >= 0; --i) {
			Move currentMove = legalMoves.get(i);
			byte targetSquare = currentMove.targetSquare;
			if (targetSquare == (byte) (startSquare - 1)) {
				canCastleLeft = true;
			} else if (targetSquare == (byte) (startSquare + 1)) {
				canCastleRight = true;
			}

			// Break early if we've confirmed castling both ways works!
			if (canCastleLeft && canCastleRight) {
				break;
			}
		}

		if (!canCastleLeft && !canCastleRight) {
			return legalMoves;
		}

		byte[] emptyLeft = (isWhiteTurn) ? new byte[] { 57, 58, 59 } : new byte[] { 1, 2, 3 };
		byte[] emptyRight = (isWhiteTurn) ? new byte[] { 61, 62 } : new byte[] { 5, 6 };

		for (byte i = 0; i < 3; i++) {
			byte currentIndex = emptyLeft[i];
			if (currentPosition[currentIndex] != Piece.EMPTY) {
				canCastleLeft = false;
				break;
			}
		}

		for (byte i = 0; i < 2; i++) {
			byte currentIndex = emptyRight[i];
			if (currentPosition[currentIndex] != Piece.EMPTY) {
				canCastleRight = false;
				break;
			}
		}

		if (currentPosition[rookL] != lookForRookType) {
			canCastleLeft = false;
		}

		if (currentPosition[rookR] != lookForRookType) {
			canCastleRight = false;
		}

		if (!canCastleLeft && !canCastleRight) {
			return legalMoves;
		}

		List<Move> moveHistory = board.getMoveHistory();

		for (Move playedMove : moveHistory) {
			if (playedMove.thisPieceType == thisPieceType) {
				return legalMoves;
			}
		}

		for (Move playedMove : moveHistory) {
			if (canCastleLeft && playedMove.thisPieceType == lookForRookType && playedMove.startSquare == rookL) {
				canCastleLeft = false;
			}

			if (canCastleRight && playedMove.thisPieceType == lookForRookType && playedMove.startSquare == rookR) {
				canCastleRight = false;
			}

			if (!canCastleLeft && !canCastleRight) {
				return legalMoves;
			}
		}

		if (board.kingInCheck(isWhiteTurn, currentPosition)) {
			return legalMoves;
		}

		if (canCastleRight) {
			byte kingTargetSquare = (isWhiteTurn) ? (byte) (62) : (byte) (6);

			byte[] newPosition = currentPosition.clone();
			newPosition[kingTargetSquare] = newPosition[startSquare];
			newPosition[startSquare] = 0;

			if (board.kingInCheck(isWhiteTurn, newPosition)) {
				return legalMoves;
			}

			Move move = new MoveBuilder()
					.startSquare(startSquare)
					.targetSquare(kingTargetSquare)
					.thisPieceType(thisPieceType)
					.capturedPieceSquare(kingTargetSquare)
					.capturedPieceType((byte) (0))
					.castleRight(true)
					.buildMove();

			legalMoves.add(move);
		}

		if (canCastleLeft) {
			byte kingTargetSquare = (isWhiteTurn) ? (byte) (58) : (byte) (2);

			byte[] newPosition = currentPosition.clone();
			newPosition[kingTargetSquare] = newPosition[startSquare];
			newPosition[startSquare] = 0;

			if (board.kingInCheck(isWhiteTurn, newPosition)) {
				return legalMoves;
			}

			Move move = new MoveBuilder()
					.startSquare(startSquare)
					.targetSquare(kingTargetSquare)
					.thisPieceType(thisPieceType)
					.capturedPieceSquare(kingTargetSquare)
					.capturedPieceType((byte) (0))
					.castleLeft(true)
					.buildMove();

			legalMoves.add(move);
		}

		return legalMoves;
	}

	private static List<Move> handlePawnMoves(Board board, byte startSquare, List<Move> legalMoves,
			byte[] currentPosition, byte thisPieceType) {
		boolean isWhitePiece = Piece.isWhite(thisPieceType);
		byte startingRow = (isWhitePiece) ? (byte) (6) : (byte) (1);
		byte enPassantRow = (isWhitePiece) ? (byte) (3) : (byte) (4);
		byte promotionRow = (isWhitePiece) ? (byte) (1) : (byte) (6);
		byte[][][] potentialMoves = (isWhitePiece) ? PrecomputedData.whitePawnMoveData
				: PrecomputedData.blackPawnMoveData;

		byte[][] potentialMovesForIndex = potentialMoves[startSquare];
		byte row = (byte) (startSquare / 8);

		for (byte i = 0; i < potentialMoves[startSquare].length; i++) {
			for (byte j = 0; j < potentialMoves[startSquare][i].length; j++) {
				byte targetSquare = potentialMoves[startSquare][i][j];
				byte capturedPieceType = currentPosition[targetSquare];

				if (capturedPieceType != Piece.EMPTY && i == 0) {
					break;
				}

				if (i == 1 || i == 2) {
					break;
				}

				if ((capturedPieceType == Piece.EMPTY
						|| Piece.isWhite(capturedPieceType) == Piece.isWhite(thisPieceType)) && i > 2) {
					break;
				}

				byte[] newPosition = currentPosition.clone();
				newPosition[targetSquare] = newPosition[startSquare];
				newPosition[startSquare] = 0;

				if (board.kingInCheck(isWhitePiece, newPosition)) {
					break;
				}

				Move move = new MoveBuilder()
						.startSquare(startSquare)
						.targetSquare(targetSquare)
						.capturedPieceSquare(targetSquare)
						.capturedPieceType(capturedPieceType)
						.firstMove(row == startingRow)
						.promotion(row == promotionRow)
						.buildMove();

				legalMoves.add(move);

				if (capturedPieceType != Piece.EMPTY
						&& Piece.isWhite(capturedPieceType) != Piece.isWhite(thisPieceType)) {
					break;
				}
			}
		}

		List<Move> moveHistory = board.getMoveHistory();
		if (!moveHistory.isEmpty()) {
			Move lastMove = moveHistory.get(moveHistory.size() - 1);

			if (potentialMovesForIndex[1].length > 0) {
				byte enPassantRight = currentPosition[potentialMovesForIndex[1][0]];
				if (row == enPassantRow &&
						lastMove.firstMove &&
						potentialMovesForIndex[1][0] == lastMove.targetSquare &&
						enPassantRight != Piece.EMPTY &&
						Piece.isWhite(enPassantRight) != Piece.isWhite(thisPieceType)) {
					byte[] newPosition = currentPosition.clone();
					newPosition[potentialMovesForIndex[4][0]] = newPosition[startSquare];
					newPosition[startSquare] = 0;

					if (board.kingInCheck(Piece.isWhite(thisPieceType), newPosition)) {
						return legalMoves;
					}

					Move move = new MoveBuilder()
							.startSquare(startSquare)
							.targetSquare(potentialMovesForIndex[4][0])
							.capturedPieceSquare(potentialMovesForIndex[1][0])
							.capturedPieceType(enPassantRight)
							.enPassant(true)
							.buildMove();
					legalMoves.add(move);
				}
			}

			if (potentialMovesForIndex[2].length > 0) {
				byte enPassantLeft = currentPosition[potentialMovesForIndex[2][0]];
				if (row == enPassantRow &&
						lastMove.firstMove &&
						potentialMovesForIndex[2][0] == lastMove.targetSquare &&
						enPassantLeft != Piece.EMPTY &&
						Piece.isWhite(enPassantLeft) != Piece.isWhite(thisPieceType)) {
					byte[] newPosition = currentPosition.clone();
					newPosition[potentialMovesForIndex[3][0]] = newPosition[startSquare];
					newPosition[startSquare] = 0;

					if (board.kingInCheck(Piece.isWhite(thisPieceType), newPosition)) {
						return legalMoves;
					}

					Move move = new MoveBuilder()
							.startSquare(startSquare)
							.targetSquare(potentialMovesForIndex[3][0])
							.capturedPieceSquare(potentialMovesForIndex[2][0])
							.capturedPieceType(enPassantLeft)
							.enPassant(true)
							.buildMove();
					legalMoves.add(move);
				}
			}
		}

		return legalMoves;
	}
}