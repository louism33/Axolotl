package javacode.chessengine;

import javacode.chessprogram.bitboards.BitBoards;
import javacode.chessprogram.chess.BitIndexing;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveMaking.StackMoveData;
import javacode.graphicsandui.Art;
import org.junit.Assert;

import java.util.Objects;
import java.util.Random;
import java.util.Stack;

import static javacode.chessprogram.bitboards.BitBoards.*;
import static javacode.chessprogram.chess.BitManipulations.newPieceOnSquare;
import static javacode.chessprogram.moveMaking.MoveOrganiser.whichPieceOnSquare;
import static javacode.chessprogram.moveMaking.MoveParser.*;
import static javacode.chessprogram.moveMaking.StackMoveData.SpecialMove.ENPASSANTVICTIM;

public class ZobristHash {

    // todo add separate hash for pawns (used in eval for structure), material

    private static final long initHashSeed = 100;
    final Stack<Long> zobristStack = new Stack<>();
    private static final long[][] zobristHashPieces = initPieceHash();
    private static final long[] zobristHashCastlingRights = initCastlingHash();
    private static final long[] zobristHashEPFiles = initEPHash();
    static final long zobristHashColourBlack = initColourHash();
    private long boardHash;

    public ZobristHash(Chessboard board) {
        this.boardHash = boardToHash(board);
    }

    long updateWithEPFlags(Chessboard board){
        Assert.assertTrue(board.moveStack.size() > 0);
        long hash = 0;
        StackMoveData peek = board.moveStack.peek();
        if (peek.typeOfSpecialMove == ENPASSANTVICTIM) {
            // file one = FILE_A
            hash ^= zobristHashEPFiles[peek.enPassantFile - 1];
        }
        
        return hash;
    }

    void updateHashPostMove(Chessboard board, Move move){
        /*
        invert colour
        */
        boardHash ^= zobristHashColourBlack;

        Assert.assertTrue(board.moveStack.size() > 0);
        
        /*
        if move we just made raised EP flag, update hash
        */
        boardHash ^= updateWithEPFlags(board);

        /*
        if castling rights changed, update hash
        */
        boardHash ^= postMoveCastlingRights(board);

    }

    private long postMoveCastlingRights(Chessboard board){
        long updatedHashValue = 0;
        StackMoveData peek = board.moveStack.peek();
        /*
        undo previous castling rights
        */
        int numTo15Undo = 0;
        if (peek.whiteCanCastleK){
            numTo15Undo += 1;
        }
        if (peek.whiteCanCastleQ){
            numTo15Undo += 2;
        }
        if (peek.blackCanCastleK){
            numTo15Undo += 4;
        }
        if (peek.blackCanCastleQ){
            numTo15Undo += 8;
        }
        Assert.assertTrue(numTo15Undo >= 0 && numTo15Undo <= 15);
        updatedHashValue ^= zobristHashCastlingRights[numTo15Undo];
 
        /*
        update with new castling rights
        */
        int numTo15Do = 0;
        if (board.whiteCanCastleK){
            numTo15Do  += 1;
        }

        if (board.whiteCanCastleQ){
            numTo15Do  += 2;
        }

        if (board.blackCanCastleK){
            numTo15Do  += 4;
        }

        if (board.blackCanCastleQ){
            numTo15Do  += 8;
        }

        updatedHashValue ^= zobristHashCastlingRights[numTo15Do];
        
        return updatedHashValue;
    }


    void updateHashPreMove(Chessboard board, Move move){
        int sourceSquare = move.getSourceAsPieceIndex();
        int destinationSquareIndex = move.destinationIndex;

        long sourcePiece = newPieceOnSquare(sourceSquare);
        int sourcePieceIdentifier = whichPieceOnSquare(board, sourcePiece) - 1;
        long sourceZH = zobristHashPieces[sourceSquare][sourcePieceIdentifier];

        long destinationSquare = newPieceOnSquare(destinationSquareIndex);
        long destinationZH = zobristHashPieces[destinationSquareIndex][sourcePieceIdentifier];

        boardHash ^= sourceZH;
        boardHash ^= destinationZH;
        
        /*
        captures
         */
        if ((destinationSquare & board.ALL_PIECES()) != 0){
            int destinationPieceIdentifier = whichPieceOnSquare(board, destinationSquare) - 1;
            /*
            remove taken piece from hash
            */
            long victimZH = zobristHashPieces[destinationSquareIndex][destinationPieceIdentifier];
            boardHash ^= victimZH;
        }

        /* 
        "positive" EP flag is set in updateHashPostMove, in updateHashPreMove we cancel a previous EP flag
        */
        Stack<StackMoveData> moveStack = board.moveStack;
        if (moveStack.size() > 0){
            boardHash ^= updateWithEPFlags(board);
        }

        long destinationPiece = newPieceOnSquare(move.destinationIndex);

        if (isSpecialMove(move)){
            if (isCastlingMove(move)) {
                int originalRookIndex = 0;
                int newRookIndex = 0;
                if ((sourcePiece & WHITE_KING) != 0){
                    if (move.destinationIndex == 1){
                        originalRookIndex = 0;
                        newRookIndex = move.destinationIndex + 1;
                    }
                    else if (move.destinationIndex == 5){
                        originalRookIndex = 7;
                        newRookIndex = move.destinationIndex - 1;
                    }
                }

                else if ((sourcePiece & BLACK_KING) != 0){
                    if (move.destinationIndex == 57){
                        originalRookIndex = 56;
                        newRookIndex = move.destinationIndex + 1;
                    }
                    else if (move.destinationIndex == 61){
                        originalRookIndex = 63;
                        newRookIndex = move.destinationIndex - 1;
                    }
                }
                else {
                    throw new RuntimeException("Mistake in Zobrist of castling");
                }

                int myRook = whichPieceOnSquare(board, newPieceOnSquare(originalRookIndex)) - 1;
                long originalRookZH = zobristHashPieces[originalRookIndex][myRook];
                long newRookZH = zobristHashPieces[newRookIndex][myRook];
                boardHash ^= originalRookZH;
                boardHash ^= newRookZH;
            }

            else if (isEnPassantMove(move)){
                if ((sourcePiece & board.WHITE_PAWNS) != 0){
                    long victimPawn = destinationPiece >>> 8;
                    int indexOfVictimPawn = BitIndexing.getIndexOfFirstPiece(victimPawn);
                    int pieceToKill = whichPieceOnSquare(board, victimPawn) - 1;
                    long victimPawnZH = zobristHashPieces[indexOfVictimPawn][pieceToKill];
                    boardHash ^= victimPawnZH;
                }

                else if  ((sourcePiece & board.BLACK_PAWNS) != 0){
                    long victimPawn = destinationPiece << 8;
                    int indexOfVictimPawn = BitIndexing.getIndexOfFirstPiece(victimPawn);
                    int pieceToKill = whichPieceOnSquare(board, victimPawn) - 1;
                    long victimPawnZH = zobristHashPieces[indexOfVictimPawn][pieceToKill];
                    boardHash ^= victimPawnZH;
                }
                else {
                    throw new RuntimeException("false EP move");
                }

            }

            else if (isPromotionMove(move)){
                int whichPromotingPiece = 0;
                if ((sourcePiece & board.WHITE_PAWNS) != 0){
                    if ((move.move & WHICH_PROMOTION) == KNIGHT_PROMOTION_MASK){
                        whichPromotingPiece = 2;
                    }
                    else if ((move.move & WHICH_PROMOTION) == BISHOP_PROMOTION_MASK){
                        whichPromotingPiece = 3;
                    }
                    else if ((move.move & WHICH_PROMOTION) == ROOK_PROMOTION_MASK){
                        whichPromotingPiece = 4;
                    }
                    else if ((move.move & WHICH_PROMOTION) == QUEEN_PROMOTION_MASK){
                        whichPromotingPiece = 5;
                    }
                }

                else if ((sourcePiece & board.BLACK_PAWNS) != 0){
                    if ((move.move & WHICH_PROMOTION) == KNIGHT_PROMOTION_MASK){
                        whichPromotingPiece = 8;
                    }
                    else if ((move.move & WHICH_PROMOTION) == BISHOP_PROMOTION_MASK){
                        whichPromotingPiece = 9;
                    }
                    else if ((move.move & WHICH_PROMOTION) == ROOK_PROMOTION_MASK){
                        whichPromotingPiece = 10;
                    }
                    else if ((move.move & WHICH_PROMOTION) == QUEEN_PROMOTION_MASK){
                        whichPromotingPiece = 11;
                    }
                }

                /*
                remove my pawn from zh
                 */
                boardHash ^= destinationZH;
                
                long promotionZH = zobristHashPieces[destinationSquareIndex][whichPromotingPiece - 1];
                boardHash ^= promotionZH;
            }
        }
    }

    /*
    create almost unique long to identify current board
     */
    private long boardToHash(Chessboard board){
        long hash = 0;
        for (int sq = 0; sq < 64; sq++) {
            long pieceOnSquare = newPieceOnSquare(sq);
            int pieceIndex = whichPieceOnSquare(board, pieceOnSquare) - 1;
            if (pieceIndex != -1) {
                hash ^= zobristHashPieces[sq][pieceIndex];
            }
        }

        hash ^= castlingRightsToHash(board);

        if (!board.isWhiteTurn()){
            hash ^= zobristHashColourBlack;
        }
        
        if (board.moveStack.size() > 0){
            hash ^= updateWithEPFlags(board);
        }

        return hash;
    }

    private long castlingRightsToHash(Chessboard board){
        int numTo15 = 0;
        if (board.whiteCanCastleK){
            numTo15 += 1;
        }
        if (board.whiteCanCastleQ){
            numTo15 += 2;
        }
        if (board.blackCanCastleK){
            numTo15 += 4;
        }
        if (board.blackCanCastleQ){
            numTo15 += 8;
        }
        Assert.assertTrue(numTo15 >= 0 && numTo15 <= 15);

        return zobristHashCastlingRights[numTo15];
    }

    /*
    create values for every possible piece on every possible square
     */
    private static long[][] initPieceHash(){
        Random r = new Random(initHashSeed);
        long[][] zobristHash = new long[64][12];
        for (int outer = 0; outer < 64; outer++){
            for (int inner = 0; inner < 12; inner++){
                zobristHash[outer][inner] = r.nextLong();
            }
        }
        return zobristHash;
    }

    /*
    create values for every possible combination of castling right
    */
    private static long[] initCastlingHash(){
        Random r = new Random(initHashSeed + 1);
        long[] zobristHash = new long[16];
        zobristHash[0] = 0;
        for (int cr = 1; cr < zobristHash.length; cr++){
            zobristHash[cr] = r.nextLong();
        }
        return zobristHash;
    }

    /*
    create values for every possible EP file
    */
    private static long[] initEPHash(){
        Random r = new Random(initHashSeed + 2);
        long[] zobristHash = new long[8];
        for (int cr = 0; cr < zobristHash.length; cr++){
            zobristHash[cr] = r.nextLong();
        }
        return zobristHash;
    }
    
    /*
    create value for the player being black
    */
    private static long initColourHash(){
        Random r = new Random(initHashSeed + 3);
        return r.nextLong();
    }

    public long getBoardHash() {
        return boardHash;
    }

    void setBoardHash(long boardHash) {
        this.boardHash = boardHash;
    }

    public Stack<Long> getZobristStack() {
        return zobristStack;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ZobristHash that = (ZobristHash) o;
        return boardHash == that.boardHash &&
                Objects.equals(zobristStack, that.zobristStack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zobristStack, boardHash);
    }

    @Override
    public String toString() {
        return "ZobristHash{" +
                "zobristStack=" + zobristStack +
                ", boardHash=" + boardHash +
                '}';
    }


}
