import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.HashMap;

import java.util.Timer;
import java.util.TimerTask;

public class Board {
    private byte[] currentPosition;
    private List<Move> moveHistory = new ArrayList<Move>(0);
	private boolean isWhiteTurn;
	private double searchedNodes;
	private double evaluatedNodes;
	private byte gameState = 0; // 0 opening, 1 middlegame, 2 endgame
	private boolean searchCutoff;
	private int piecesOnBoard;

	public Board() {
		
		//parseFEN("8/8/7k/8/8/8/5q2/3B2RK b - - 0 1");
		//parseFEN("1rb2rk1/ppp5/1q2P1Q1/3p4/8/5P2/P1P4P/K2R3R b - - 0 1");
		parseFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1"); // Normal starting position for regular games

		// TEST CASES
		
		//parseFEN("rn3rk1/p5pp/2p5/3Ppb2/2q5/1Q6/PPPB2PP/R3K1NR b - - 0 1"); // mate in 3 (6)
		//parseFEN("7r/p3ppk1/3p4/2p1P1Kp/2Pb4/3P1QPq/PP5P/R6R b - - 0 1"); // This one is a mate in 2
		//parseFEN("3r2k1/p4ppp/b1pb1Q2/q7/8/1B3p2/PBPPNP1P/1R2K1R1 b - - 1 0"); // mate in 4 (8)

		//parseFEN("r2qk2r/1pp2ppp/p1np1n2/2b1p1B1/2B1P1b1/2NP1N1P/PPP2PP1/R2Q1RK1 b kq - 0 8"); // agree with stockfish

		
		//1r2k1r1/pbppnp1p/1b3P2/8/Q7/B1PB1q2/P4PPP/3R2K1
		//3r2k1/p4ppp/b1pb1Q2/q7/8/1B3p2/PBPPNP1P/1R2K1R1

    }

	public void parseFEN(String fen) {
	    byte[] board = new byte[64];
	    String[] fenParts = fen.split(" ");
	
	    if (fenParts.length < 1) {
	        throw new IllegalArgumentException("Invalid FEN string: " + fen);
	    }
	
	    String piecePlacement = fenParts[0];
	    int row = 0; // Start with the 8th rank (index 7) and move down to the 1st rank (index 0)
	    int col = 0; // Start with the a-file (index 0) and move right to the h-file (index 7)
	
	    for (char c : piecePlacement.toCharArray()) {
	        if (c == '/') {
	            // Move to the next rank and reset the column counter
	            row++;
	            col = 0;
	        } else if (Character.isDigit(c)) {
	            // Empty squares, skip the specified number of squares
	            int emptySquares = Character.getNumericValue(c);
	            col += emptySquares;
	        } else {
	            // Piece symbol
	            if (row < 0 || row >= 8 || col < 0 || col >= 8) {
	                throw new IllegalArgumentException("Invalid FEN string: " + fen);
	            }
	            board[row * 8 + col] = Piece.mapPieceSymbol(c);
	            col++;
	        }
	    }

		System.out.println(Arrays.toString(board));
		
		isWhiteTurn = (fenParts[1].equals("w"));
	    currentPosition = board;
		moveHistory = new ArrayList<Move>(0);
	}
	
	public void makeMove(Move move) {
		byte startSquare = move.startSquare;
		byte targetSquare = move.targetSquare;
		byte capturedPieceType = move.capturedPieceType;
		byte capturedPieceSquare = move.capturedPieceSquare;
		
		boolean promotion = move.promotion;
		boolean castleLeft = move.castleLeft;
		boolean castleRight = move.castleRight;

		currentPosition[capturedPieceSquare] = 0;
		currentPosition[targetSquare] = currentPosition[startSquare];
		currentPosition[startSquare] = 0;
		
		if (promotion) {
			byte colorMultiplier = (isWhiteTurn) ? (byte)(1) : (byte)(-1);
			currentPosition[targetSquare] = (byte)(Piece.QUEEN * colorMultiplier);
		}

		if (castleLeft) {
			byte rookTargetSquare = (isWhiteTurn) ? (byte)(59) : (byte)(3);
			byte rookLastSquare = (isWhiteTurn) ? (byte)(56) : (byte)(0);
			byte colorMultiplier = (isWhiteTurn) ? (byte)(1) : (byte)(-1);
			currentPosition[rookTargetSquare] = (byte)(Piece.ROOK * colorMultiplier);
			currentPosition[rookLastSquare] = (byte)(0);
		} 
		
		if (castleRight) {
			byte rookTargetSquare = (isWhiteTurn) ? (byte)(61) : (byte)(5);
			byte rookLastSquare = (isWhiteTurn) ? (byte)(63) : (byte)(7);
			byte colorMultiplier = (isWhiteTurn) ? (byte)(1) : (byte)(-1);
			currentPosition[rookTargetSquare] = (byte)(Piece.ROOK * colorMultiplier);
			currentPosition[rookLastSquare] = (byte)(0);
		}

		moveHistory.add(move);	
		
		isWhiteTurn = !isWhiteTurn;
	}

	public void undoMove(Move move) {
		isWhiteTurn = !isWhiteTurn;

	    byte startSquare = move.startSquare;
	    byte targetSquare = move.targetSquare;
	    byte capturedPieceType = move.capturedPieceType;
	    byte capturedPieceSquare = move.capturedPieceSquare;
		
	    boolean promotion = move.promotion;
		boolean castleLeft = move.castleLeft;
		boolean castleRight = move.castleRight;
	    
	    currentPosition[startSquare] = currentPosition[targetSquare];
	    currentPosition[targetSquare] = 0;
	    
	    if (promotion) {
	        byte pieceType = currentPosition[startSquare];
	        byte colorMultiplier = (isWhiteTurn) ? (byte)(1) : (byte)(-1);
	        currentPosition[startSquare] = (byte)(Piece.PAWN * colorMultiplier);
	    }
	    
	    if (capturedPieceType != 0) {
	        currentPosition[capturedPieceSquare] = capturedPieceType;
	    }

		if (castleLeft) {
			byte rookTargetSquare = (isWhiteTurn) ? (byte)(59) : (byte)(3);
			byte rookLastSquare = (isWhiteTurn) ? (byte)(56) : (byte)(0);
			byte colorMultiplier = (isWhiteTurn) ? (byte)(1) : (byte)(-1);
			currentPosition[rookLastSquare] = (byte)(Piece.ROOK * colorMultiplier);
			currentPosition[rookTargetSquare] = (byte)(0);
		}
		
		if (castleRight) {
			byte rookTargetSquare = (isWhiteTurn) ? (byte)(61) : (byte)(5);
			byte rookLastSquare = (isWhiteTurn) ? (byte)(63) : (byte)(7);
			byte colorMultiplier = (isWhiteTurn) ? (byte)(1) : (byte)(-1);
			currentPosition[rookLastSquare] = (byte)(Piece.ROOK * colorMultiplier);
			currentPosition[rookTargetSquare] = (byte)(0);
		}

		moveHistory.remove(move);
	}

	public boolean undoLastMove() {
	    if (moveHistory.isEmpty()) {
	        return false;
	    }

		Move lastMove = moveHistory.remove(moveHistory.size() - 1);

		undoMove(lastMove);
		
		return true;
	}
	
	public boolean kingInCheck(boolean isWhite, byte[] newPosition) {
		byte[][][] potentialMoves = PrecomputedData.kingCheckData;
		byte targetKing = (isWhite == true) ? Piece.KING : -Piece.KING;
		byte kingIndex = Utils.findIndex(newPosition, targetKing);


		byte[][] potentialMovesForIndex = potentialMoves[kingIndex];
		for (byte i = 0; i < potentialMovesForIndex.length; i++) {
			for (byte j = 0; j < potentialMovesForIndex[i].length; j++) {
				byte squareThatCouldKillMe = potentialMovesForIndex[i][j];
				byte squareThatCouldKillMeType = newPosition[squareThatCouldKillMe];

				if (squareThatCouldKillMeType != Piece.EMPTY && Piece.isWhite(squareThatCouldKillMeType) == Piece.isWhite(targetKing)) {
					break;
				}
				
				byte killMeSquareAbs = (byte)(Math.abs(squareThatCouldKillMeType));
				if (i < 4) {
				    // Rook or Queen
					if (squareThatCouldKillMeType != Piece.EMPTY && Piece.isWhite(squareThatCouldKillMeType) != Piece.isWhite(targetKing) && killMeSquareAbs != Piece.ROOK && killMeSquareAbs != Piece.QUEEN) {
						break;
					}
					
					if (killMeSquareAbs == Piece.ROOK || killMeSquareAbs == Piece.QUEEN) {
						return true;
					}
				} else if (i < 8) {
				    // Bishop or Queen
					if (squareThatCouldKillMeType != Piece.EMPTY && Piece.isWhite(squareThatCouldKillMeType) != Piece.isWhite(targetKing) && killMeSquareAbs != Piece.BISHOP && killMeSquareAbs != Piece.QUEEN) {
						break;
					}
		
					if (killMeSquareAbs == Piece.BISHOP || killMeSquareAbs == Piece.QUEEN) {
						return true;
					}
				} else if (i < 16) {
				    // Knight
					if (killMeSquareAbs == Piece.KNIGHT) {
						return true;
					}
				} else if (i < 24) {
				    // King
					if (killMeSquareAbs == Piece.KING) {
						return true;
					}
				} else if (i < 26) {
				    // Black Pawns
					if (Math.abs(squareThatCouldKillMeType) == Piece.PAWN && isWhite) {
						return true;
					}
				} else if (i < 28) {
				    // White Pawns
					if (Math.abs(squareThatCouldKillMeType) == Piece.PAWN && !isWhite) {
						return true;
					}
				}
			}
		}
	
		return false;
	}
	
	public List<Move> getAllLegalMoves(boolean ordered) {
		List<Move> allLegalMoves = new ArrayList<>();
		for (byte i = 0; i < 64; i++) {
			byte currentPiece = currentPosition[i];
			if (currentPiece == Piece.EMPTY || isWhiteTurn != Piece.isWhite(currentPiece)) continue;

			List<Move> movesForIndex = MoveGenerator.generateMovesForIndex(i, this);
    		allLegalMoves.addAll(movesForIndex);
		}

		if (ordered) {
			Collections.shuffle(allLegalMoves);
			Collections.sort(allLegalMoves, Move.promotionComparator);
			Collections.sort(allLegalMoves, Move.captureValueComparator);
			Collections.sort(allLegalMoves, Move.checkComparator);
		}
		
		return allLegalMoves;
	}

	public int evaluateCurrentPosition(int depth, int legalMoveCount) {
		int evaluation = 0;

		if (kingInCheck(isWhiteTurn, currentPosition) && legalMoveCount == 0) {
			evaluation = (isWhiteTurn) ? -100000000 - depth : 100000000 + depth;
			return evaluation;
		} else if (legalMoveCount == 0) {
			evaluation = 0;
			return evaluation;
		}

		piecesOnBoard = 0;
		for (byte i = 0; i < 64; i++) {
			byte currentPiece = currentPosition[i];
			if (currentPiece == Piece.EMPTY) continue;
			piecesOnBoard++;

			evaluation += PieceSquareTables.getValueOfAt(currentPiece, i, piecesOnBoard);
			evaluation += Piece.getPieceValue(currentPiece);		
		}
		
		return evaluation;
	}

    public EvaluationResult iterativeDeepening(long maxSearchTime) {
		double alpha = Double.NEGATIVE_INFINITY;
		double beta = Double.POSITIVE_INFINITY;
	    EvaluationResult bestResult = new EvaluationResult(null, 0);

		searchCutoff = false;
		
		boolean startingPlayer = isWhiteTurn;
		long startTime = System.currentTimeMillis();
        for (int depth = 1; depth <= 9; depth++) {
			searchedNodes = 0;
			evaluatedNodes = 0;
			
            EvaluationResult currentResult = minimax(depth, alpha, beta, startingPlayer, bestResult.move);
			bestResult = currentResult;
			
			long currentTime = System.currentTimeMillis();
			System.out.println("Got to depth: " + depth + " Searched Nodes: " + searchedNodes + " Evaluated Nodes: " + evaluatedNodes + " " + bestResult.move.startSquare + bestResult.move.targetSquare + " " + (currentTime - startTime));

			if ((currentTime - startTime) > maxSearchTime) {
				//searchCutoff = true;
				break;
			}
			
		}
		
        return bestResult;
    }

	public EvaluationResult minimax(int depth, double alpha, double beta, boolean isMaximizingPlayer, Move considerMove) {
		List<Move> legalMoves = getAllLegalMoves(true);
		int legalMoveCount = legalMoves.size();
		
		// Maximum depth exceeded or node is a terminal node
		if (depth == 0 || legalMoveCount == 0 || (depth > 2 && searchCutoff)) {
			evaluatedNodes++;
			return new EvaluationResult(considerMove, evaluateCurrentPosition(depth, legalMoveCount));
		}

		searchedNodes++;

		Move bestMove = considerMove;

		// Find maximum/minimum from list of possible moves
		for (Move move : legalMoves) {

			makeMove(move);

			EvaluationResult result = minimax(depth - 1, alpha, beta, !isMaximizingPlayer, bestMove);

			undoMove(move);

			double childValue = result.value;

			if (isMaximizingPlayer) {
				if (childValue > alpha) {
					alpha = childValue;
					bestMove = move;
				}
			} else {
				if (childValue < beta) {
					beta = childValue;
					bestMove = move;
				}
			}

			// Alpha-beta pruning
			if (alpha >= beta) {
				break;
			}
		}

		if (isMaximizingPlayer) {
			return new EvaluationResult(bestMove, alpha);
		} else {
			return new EvaluationResult(bestMove, beta);
		}
	}

	
	public byte[] getCurrentPosition() {
		return currentPosition;
	}

	public List<Move> getMoveHistory() {
		return moveHistory;
	}

	public boolean isWhiteTurn() {
		return isWhiteTurn;
	}

	public int getGameStage() {
		return gameState;
	}
}