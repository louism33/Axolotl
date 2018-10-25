package javacode.chessengine;

import javacode.chessprogram.bitboards.BitBoards;
import javacode.chessprogram.chess.BitManipulations;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveMaking.MoveOrganiser;
import javacode.chessprogram.moveMaking.MoveParser;
import javacode.chessprogram.moveMaking.StackMoveData;
import org.junit.Assert;

import java.util.Objects;
import java.util.Random;
import java.util.Stack;

import static javacode.chessprogram.chess.BitManipulations.*;
import static javacode.chessprogram.moveMaking.MoveOrganiser.*;
import static javacode.chessprogram.moveMaking.MoveOrganiser.whichPieceOnSquare;
import static javacode.chessprogram.moveMaking.MoveParser.*;

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

    void updateWithEPFlags(Chessboard board){
        StackMoveData peek = board.moveStack.peek();
        if (peek.typeOfSpecialMove == StackMoveData.SpecialMove.ENPASSANTVICTIM) {
            // file one = FILE_A
            boardHash ^= zobristHashEPFiles[peek.enPassantFile - 1];
        }
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
        updateWithEPFlags(board);

        /*
        if castling rights changed, update hash
        */
        StackMoveData peek = board.moveStack.peek();
        if (peek.whiteCanCastleK != board.whiteCanCastleK){
            boardHash ^= zobristHashCastlingRights[1];
        }

        if (peek.whiteCanCastleQ != board.whiteCanCastleQ){
            boardHash ^= zobristHashCastlingRights[2];
        }

        if (peek.blackCanCastleK != board.blackCanCastleK){
            boardHash ^= zobristHashCastlingRights[4];
        }

        if (peek.blackCanCastleQ != board.blackCanCastleQ){
            boardHash ^= zobristHashCastlingRights[8];
        }


    }

    void updateHashPreMove(Chessboard board, Move move){
        int sourceSquare = move.getSourceAsPiece();
        int destinationSquare = move.destination;

        long sourcePiece = newPieceOnSquare(sourceSquare);
        int sourcePieceIdentifier = whichPieceOnSquare(board, sourcePiece) - 1;
        long sourceZH = zobristHashPieces[sourceSquare][sourcePieceIdentifier];

        long destination = newPieceOnSquare(destinationSquare);
        long destinationZH = zobristHashPieces[destinationSquare][sourcePieceIdentifier];

        boardHash ^= sourceZH;
        boardHash ^= destinationZH;
        
        /*
        captures
         */
        if ((destination & board.ALL_PIECES()) != 0){
            int destinationPieceIdentifier = whichPieceOnSquare(board, destination) - 1;
            long victimZH = zobristHashPieces[sourceSquare][destinationPieceIdentifier];
            boardHash ^= victimZH;
        }

        /* 
        "positive" EP flag is set in updateHashPostMove, in updateHashPreMove we cancel a previous EP flag
        */
        Stack<StackMoveData> moveStack = board.moveStack;
        if (moveStack.size() > 0){
            updateWithEPFlags(board);
        }



        long destinationPiece = newPieceOnSquare(move.destination);

        if (isSpecialMove(move)){
            if (isCastlingMove(move)) {
                long originalRook = 0;
                long newRook = 0;
                if ((sourcePiece & BitBoards.WHITE_KING) != 0){
                    if (move.destination == 1){
                        originalRook = newPieceOnSquare(0);
                        newRook = newPieceOnSquare(move.destination + 1);
                    }
                    else if (move.destination == 5){
                        originalRook = newPieceOnSquare(7);
                        newRook = newPieceOnSquare(move.destination - 1);
                    }
                }

                else if ((sourcePiece & BitBoards.BLACK_KING) != 0){
                    if (move.destination == 57){
                        originalRook = newPieceOnSquare(56);
                        newRook = newPieceOnSquare(move.destination + 1);
                    }
                    else if (move.destination == 61){
                        originalRook = newPieceOnSquare(63);
                        newRook = newPieceOnSquare(move.destination - 1);
                    }
                }
                else {
                    throw new RuntimeException("Mistake in Zobrist of castling");
                }
                boardHash ^= originalRook;
                boardHash ^= newRook;
            }

            else if (isEnPassantMove(move)){
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

                long promotionZH = zobristHashPieces[destinationSquare][whichPromotingPiece - 1];
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
        for (int cr = 0; cr < zobristHash.length; cr++){
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
    create values for the player being black
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
