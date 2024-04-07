public class MoveBuilder {
    private byte _startSquare;
    private byte _targetSquare;
    private byte _thisPieceType;

    private byte _capturedPieceType;
    private byte _capturedPieceSquare;

    private boolean _enPassant = false;
    private boolean _castleLeft = false;
    private boolean _castleRight = false;
    private boolean _promotion = false;
    private boolean _firstMove = false;
    private boolean _check = false;

    private double _captureValue = 0;

    public MoveBuilder() {
    }

    public Move buildMove() {
        return new Move(_startSquare, _targetSquare, _thisPieceType, _capturedPieceSquare, _capturedPieceType,
                _enPassant, _castleLeft, _castleRight, _promotion, _firstMove, _check, _captureValue);
    }

    public MoveBuilder startSquare(byte _startSquare) {
        this._startSquare = _startSquare;
        return this;
    }

    public MoveBuilder targetSquare(byte _targetSquare) {
        this._targetSquare = _targetSquare;
        return this;
    }

    public MoveBuilder thisPieceType(byte _thisPieceType) {
        this._thisPieceType = _thisPieceType;
        return this;
    }

    public MoveBuilder capturedPieceSquare(byte _capturedPieceSquare) {
        this._capturedPieceSquare = _capturedPieceSquare;
        return this;
    }

    public MoveBuilder capturedPieceType(byte _capturedPieceType) {
        this._capturedPieceType = _capturedPieceType;
        return this;
    }

    public MoveBuilder enPassant(boolean _enPassant) {
        this._enPassant = _enPassant;
        return this;
    }

    public MoveBuilder castleLeft(boolean _castleLeft) {
        this._castleLeft = _castleLeft;
        return this;
    }

    public MoveBuilder castleRight(boolean _castleRight) {
        this._castleRight = _castleRight;
        return this;
    }

    public MoveBuilder promotion(boolean _promotion) {
        this._promotion = _promotion;
        return this;
    }

    public MoveBuilder firstMove(boolean _firstMove) {
        this._firstMove = _firstMove;
        return this;
    }

    public MoveBuilder check(boolean _check) {
        this._check = _check;
        return this;
    }

    public MoveBuilder captureValue(double _captureValue) {
        this._captureValue = _captureValue;
        return this;
    }
}