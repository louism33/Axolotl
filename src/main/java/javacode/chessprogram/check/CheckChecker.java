package javacode.chessprogram.check;

import javacode.chessengine.transpositiontable.ZobristHash;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.moveGeneration.PieceMoveKing;
import javacode.chessprogram.moveGeneration.PieceMoveKnight;
import javacode.chessprogram.moveGeneration.PieceMovePawns;
import javacode.chessprogram.moveGeneration.PieceMoveSliding;

import java.util.Stack;

import static javacode.chessprogram.chess.BitIndexing.populationCount;

public class CheckChecker {

    public static boolean boardInCheck(Chessboard board, boolean white){
        long myKing = (white) ? board.WHITE_KING : board.BLACK_KING;
        int numberOfCheckers = numberOfPiecesThatLegalThreatenSquare(board, white, myKing);
        return numberOfCheckers > 0;
    }

    public static int numberOfPiecesThatLegalThreatenSquare(Chessboard board, boolean myColour, long square){
        long pawns, knights, bishops, rooks, queens, king;
        if (!myColour){
            pawns = board.WHITE_PAWNS;
            knights = board.WHITE_KNIGHTS;
            bishops = board.WHITE_BISHOPS;
            rooks = board.WHITE_ROOKS;
            queens = board.WHITE_QUEEN;
            king = board.WHITE_KING;
        }
        else {
            pawns = board.BLACK_PAWNS;
            knights = board.BLACK_KNIGHTS;
            bishops = board.BLACK_BISHOPS;
            rooks = board.BLACK_ROOKS;
            queens = board.BLACK_QUEEN;
            king = board.BLACK_KING;
        }

        int numberOfThreats = 0;

        if (pawns != 0) {
            numberOfThreats += populationCount(PieceMovePawns.singlePawnCaptures(board, square, myColour, pawns));
        }
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        if (knights != 0) {
            numberOfThreats += populationCount(PieceMoveKnight.singleKnightTable(board, square, myColour, knights));
        }
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        if (bishops != 0) {
            numberOfThreats += populationCount(PieceMoveSliding.singleBishopCaptures(board, square, myColour, bishops));
        }
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        if (rooks != 0) {
            numberOfThreats += populationCount(PieceMoveSliding.singleRookCaptures(board, square, myColour, rooks));
        }
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        if (queens != 0) {
            numberOfThreats += populationCount(PieceMoveSliding.singleQueenCaptures(board, square, myColour, queens));
        }
        if (numberOfThreats > 1){
            return numberOfThreats;
        }
        if (king != 0) {
            numberOfThreats += populationCount(PieceMoveKing.singleKingCaptures(board, square, myColour, king));
        }

        return numberOfThreats;
    }

    public static boolean isDrawByRepetition(Chessboard board, ZobristHash zobristHash){
        Stack<Long> zobristStack = (Stack<Long>) zobristHash.getZobristStack().clone();
        long zobristHashToMatch = zobristHash.getBoardHash();
        int howManyMovesToSearchToMax = 50;
        int limit = Math.min(howManyMovesToSearchToMax, zobristStack.size());
        
        int counter = 0;
        for (int previousBoardHashIndex = limit - 1; previousBoardHashIndex >= 0; previousBoardHashIndex--){
            Long pop = zobristStack.pop();
            if (pop == zobristHashToMatch){
                counter++;
            }
            if (counter > 0){
                return true;
            }
        }
        return counter > 0;
    }

    public static boolean isDrawByInsufficientMaterial(Chessboard board){
        boolean drawByMaterial = false;
        int totalPieces = populationCount(board.ALL_PIECES());

        switch (totalPieces){
            case 2:
                drawByMaterial = true;
                break;
            case 3:
                if (populationCount(board.BLACK_BISHOPS)
                        + populationCount(board.WHITE_BISHOPS)
                        + populationCount(board.BLACK_KNIGHTS)
                        +populationCount(board.WHITE_KNIGHTS) != 0) {
                    
                    drawByMaterial = true;
                }
                break;
            case 4:
                break;
        }

        return drawByMaterial;
    }
}
