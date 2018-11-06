package javacode.chessprogram.moveMaking;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

@SuppressWarnings("CanBeFinal")
public class StackMoveData {
    
    public Move move;
    public int takenPiece = 0;
    private int fiftyMoveCounter;
    public boolean whiteTurn;
    
    public enum SpecialMove {
        BASICQUIETPUSH, BASICLOUDPUSH, BASICCAPTURE, ENPASSANTVICTIM, ENPASSANTCAPTURE, CASTLING, PROMOTION,
    }
    public SpecialMove typeOfSpecialMove;
    
    // file one : FILE_A 
    public int enPassantFile = -1;
    public boolean whiteCanCastleK, whiteCanCastleQ, blackCanCastleK, blackCanCastleQ;


    public StackMoveData(Move move, Chessboard board, int fiftyMoveCounter, SpecialMove typeOfSpecialMove) {
        this.move = move;
        this.fiftyMoveCounter = fiftyMoveCounter;
        this.typeOfSpecialMove = typeOfSpecialMove;

        this.whiteTurn = board.isWhiteTurn();
        
        this.whiteCanCastleK = board.whiteCanCastleK;
        this.whiteCanCastleQ = board.whiteCanCastleQ;
        this.blackCanCastleK = board.blackCanCastleK;
        this.blackCanCastleQ = board.blackCanCastleQ;
    }

    
    public StackMoveData(Move move, Chessboard board, int fiftyMoveCounter, SpecialMove typeOfSpecialMove, int takenPiece) {
        this.move = move;
        this.fiftyMoveCounter = fiftyMoveCounter;
        this.typeOfSpecialMove = typeOfSpecialMove;
        this.takenPiece = takenPiece;

        this.whiteTurn = board.isWhiteTurn();

        this.whiteCanCastleK = board.whiteCanCastleK;
        this.whiteCanCastleQ = board.whiteCanCastleQ;
        this.blackCanCastleK = board.blackCanCastleK;
        this.blackCanCastleQ = board.blackCanCastleQ;
    }


    public StackMoveData(Move move, Chessboard board, int fiftyMoveCounter, int enPassantFile, SpecialMove typeOfSpecialMove) {
        this.move = move;
        this.fiftyMoveCounter = fiftyMoveCounter;
        this.typeOfSpecialMove = typeOfSpecialMove;
        
        if (this.typeOfSpecialMove == SpecialMove.ENPASSANTVICTIM){
            this.enPassantFile = enPassantFile;
        }

        this.whiteTurn = board.isWhiteTurn();

        this.whiteCanCastleK = board.whiteCanCastleK;
        this.whiteCanCastleQ = board.whiteCanCastleQ;
        this.blackCanCastleK = board.blackCanCastleK;
        this.blackCanCastleQ = board.blackCanCastleQ;
    }


    @Override
    public String toString() {
        return "StackMoveData{" +
                "move=" + move +
                ", takenPiece=" + takenPiece +
                ", fiftyMoveCounter=" + fiftyMoveCounter +
                ", whiteTurn=" + whiteTurn +
                ", typeOfSpecialMove=" + typeOfSpecialMove +
                ", enPassantFile=" + enPassantFile +
                ", whiteCanCastleK=" + whiteCanCastleK +
                ", whiteCanCastleQ=" + whiteCanCastleQ +
                ", blackCanCastleK=" + blackCanCastleK +
                ", blackCanCastleQ=" + blackCanCastleQ +
                '}';
    }
}