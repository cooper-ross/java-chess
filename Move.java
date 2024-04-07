import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Move {
    // Main Data
    public byte startSquare;
    public byte targetSquare;
    public byte thisPieceType;

    // Captured logic
    public byte capturedPieceSquare;
    public byte capturedPieceType;

    // Flags
    public boolean enPassant;
    public boolean castleLeft;
    public boolean castleRight;
    public boolean promotion;
    public boolean firstMove;
    public boolean check;

    // Sort
    public double captureValue;

    public Move(byte startSquare, byte targetSquare, byte thisPieceType, byte capturedPieceSquare,
            byte capturedPieceType, boolean enPassant, boolean castleLeft, boolean castleRight, boolean promotion,
            boolean firstMove, boolean check, double captureValue) {
        this.startSquare = startSquare;
        this.targetSquare = targetSquare;
        this.thisPieceType = thisPieceType;

        this.capturedPieceSquare = capturedPieceSquare;
        this.capturedPieceType = capturedPieceType;

        this.enPassant = enPassant;
        this.castleLeft = castleLeft;
        this.castleRight = castleRight;
        this.promotion = promotion;
        this.firstMove = firstMove;
        this.check = check;
        this.captureValue = captureValue;
    }

    public static Comparator<Move> captureValueComparator = new Comparator<Move>() {
        public int compare(Move move1, Move move2) {
            return Double.compare(move2.captureValue, move1.captureValue);
        }
    };

    public static Comparator<Move> checkComparator = new Comparator<Move>() {
        public int compare(Move move1, Move move2) {
            return Boolean.compare(move2.check, move1.check);
        }
    };

    public static Comparator<Move> promotionComparator = new Comparator<Move>() {
        public int compare(Move move1, Move move2) {
            return Boolean.compare(move2.promotion, move1.promotion);
            // Use reverse order to sort with 'promotion' moves first
        }
    };

    @Override
    public String toString() {
        return "From: " + startSquare + "\nTo: " + targetSquare + "\nCheck: " + check + "\nCapture Value: "
                + captureValue;
    }
}