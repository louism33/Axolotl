package javacode.chessengine;

import javacode.chessprogram.bitboards.BitBoards;
import javacode.chessprogram.chess.BitManipulations;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveMaking.MoveParser;

import java.util.Random;
import java.util.Stack;

import static javacode.chessprogram.moveMaking.MoveOrganiser.whichPieceOnSquare;

class ZobristHash {

    public final Stack<Long> zobristStack = new Stack<>();
    private final long[][] zobristHash = initHash();
    private long boardHash;

    public ZobristHash(Chessboard board) {
        this.boardHash = boardToHash(board);
    }



    void updateHash2(Chessboard board, Move move, boolean unmake){
        int sourceSquare = !unmake ? move.getSourceAsPiece() : move.destination;
        int destinationSquare = !unmake ? move.destination : move.getSourceAsPiece();

        long source = BitManipulations.newPieceOnSquare(sourceSquare);
        int sourcePieceIdentifier = whichPieceOnSquare(board, source) - 1;
        long sourceZH = zobristHash[sourceSquare][sourcePieceIdentifier];

        long destination = BitManipulations.newPieceOnSquare(destinationSquare);
        long destinationZH = zobristHash[destinationSquare][sourcePieceIdentifier];

        boardHash ^= sourceZH;
        boardHash ^= destinationZH;

    }

    void updateHash(Chessboard board, Move move, boolean unmake){
        int sourceSquare = move.getSourceAsPiece();
        int destinationSquare = move.destination;

        long sourcePiece = BitManipulations.newPieceOnSquare(sourceSquare);
        int sourcePieceIdentifier = whichPieceOnSquare(board, sourcePiece) - 1;
        long sourceZH = zobristHash[sourceSquare][sourcePieceIdentifier];

        long destination = BitManipulations.newPieceOnSquare(destinationSquare);
        long destinationZH = zobristHash[destinationSquare][sourcePieceIdentifier];

        boardHash ^= sourceZH;
        boardHash ^= destinationZH;

        // capture
        if ((destination & board.ALL_PIECES()) != 0){
            int destinationPieceIdentifier = whichPieceOnSquare(board, destination) - 1;
            long victimZH = zobristHash[sourceSquare][destinationPieceIdentifier];
            boardHash ^= victimZH;
        }


        long destinationPiece = BitManipulations.newPieceOnSquare(move.destination);


        if (MoveParser.isSpecialMove(move)){
            if (MoveParser.isCastlingMove(move)) {
                long originalRook = 0;
                long newRook = 0;
                if ((sourcePiece & BitBoards.WHITE_KING) != 0){
                    if (move.destination == 1){
                        originalRook = BitManipulations.newPieceOnSquare(0);
                        newRook = BitManipulations.newPieceOnSquare(move.destination + 1);
                    }
                    else if (move.destination == 5){
                        originalRook = BitManipulations.newPieceOnSquare(7);
                        newRook = BitManipulations.newPieceOnSquare(move.destination - 1);
                    }
                }

                else if ((sourcePiece & BitBoards.BLACK_KING) != 0){
                    if (move.destination == 57){
                        originalRook = BitManipulations.newPieceOnSquare(56);
                        newRook = BitManipulations.newPieceOnSquare(move.destination + 1);
                    }
                    else if (move.destination == 61){
                        originalRook = BitManipulations.newPieceOnSquare(63);
                        newRook = BitManipulations.newPieceOnSquare(move.destination - 1);
                    }
                }
                else {
                    throw new RuntimeException("Mistake in Zobrist of castling");
                }
                boardHash ^= originalRook;
                boardHash ^= newRook;
            }

            else if (MoveParser.isEnPassantMove(move)){
                if ((sourcePiece & board.WHITE_PAWNS) != 0){
                    long victimPawn = destinationPiece >>> 8;
                    boardHash ^= victimPawn;
                }

                else if  ((sourcePiece & board.BLACK_PAWNS) != 0){
                    long victimPawn = destinationPiece << 8;
                    boardHash ^= victimPawn;
                }
                else {
                    throw new RuntimeException("false EP move");
                }

            }

            else if (MoveParser.isPromotionMove(move)){
                int whichPromotingPiece = 0;
                if ((sourcePiece & board.WHITE_PAWNS) != 0){
                    if ((move.move & MoveParser.WHICH_PROMOTION) == MoveParser.KNIGHT_PROMOTION_MASK){
                        whichPromotingPiece = 2;
                    }
                    else if ((move.move & MoveParser.WHICH_PROMOTION) == MoveParser.BISHOP_PROMOTION_MASK){
                        whichPromotingPiece = 3;
                    }
                    else if ((move.move & MoveParser.WHICH_PROMOTION) == MoveParser.ROOK_PROMOTION_MASK){
                        whichPromotingPiece = 4;
                    }
                    else if ((move.move & MoveParser.WHICH_PROMOTION) == MoveParser.QUEEN_PROMOTION_MASK){
                        whichPromotingPiece = 5;
                    }
                }

                else if ((sourcePiece & board.BLACK_PAWNS) != 0){
                    if ((move.move & MoveParser.WHICH_PROMOTION) == MoveParser.KNIGHT_PROMOTION_MASK){
                        whichPromotingPiece = 8;
                    }
                    else if ((move.move & MoveParser.WHICH_PROMOTION) == MoveParser.BISHOP_PROMOTION_MASK){
                        whichPromotingPiece = 9;
                    }
                    else if ((move.move & MoveParser.WHICH_PROMOTION) == MoveParser.ROOK_PROMOTION_MASK){
                        whichPromotingPiece = 10;
                    }
                    else if ((move.move & MoveParser.WHICH_PROMOTION) == MoveParser.QUEEN_PROMOTION_MASK){
                        whichPromotingPiece = 11;
                    }
                }

                long promotionZH = zobristHash[destinationSquare][whichPromotingPiece - 1];
                boardHash ^= promotionZH;
            }
        }
    }

    private long boardToHash(Chessboard board){
        long hash = 0;
        for (int sq = 0; sq < 64; sq++) {
            long pieceOnSquare = BitManipulations.newPieceOnSquare(sq);
            int pieceIndex = whichPieceOnSquare(board, pieceOnSquare) - 1;
            if (pieceIndex != -1) {
                hash ^= zobristHash[sq][pieceIndex];
            }
        }
        return hash;
    }

    private static long[][] initHash(){
        Random r = new Random(100);
        long[][] zobristHash = new long[64][12];
        for (int outer = 0; outer < 64; outer++){
            for (int inner = 0; inner < 12; inner++){
                zobristHash[outer][inner] = r.nextLong();
            }
        }
        return zobristHash;
    }

    public long getBoardHash() {
        return boardHash;
    }

    public void setBoardHash(long boardHash) {
        this.boardHash = boardHash;
    }

    public Stack<Long> getZobristStack() {
        return zobristStack;
    }
}
