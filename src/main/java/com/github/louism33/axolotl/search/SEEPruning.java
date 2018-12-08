package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.chesscore.Piece;
import com.github.louism33.chesscore.PieceMove;

import java.util.ArrayList;
import java.util.List;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.BitOperations.populationCount;
import static com.github.louism33.chesscore.Piece.*;

public class SEEPruning {
    
    static int seeScore(Chessboard board, int move){
        long destinationSquare = newPieceOnSquare(MoveParser.getDestinationIndex(move));
        boolean iAmWhite = board.isWhiteTurn();
        List<Piece> myPieces = myPiecesThatThreatenSquare(board, iAmWhite, destinationSquare);
        List<Piece> enemyPieces = myPiecesThatThreatenSquare(board, !iAmWhite, destinationSquare);

        if (enemyPieces.size() > 0) {
            return seeHelper(board, move, myPieces, enemyPieces, destinationSquare);
        }
        
        return Evaluator.getScoreOfDestinationPiece(board, move);
    }
    
    private static int seeHelper(Chessboard board, int move,
                                 List<Piece> myPieces, List<Piece> enemyPieces, long destinationSquare){
 
        int seeScore = Evaluator.getScoreOfDestinationPiece(board, move);
        int totalFriends = myPieces.size();
        int myPiece = 0;
        int totalEnemies = enemyPieces.size();
        int enemyPiece = 0;
        
        while (true){
            seeScore -= scoreByPiece(myPieces.get(myPiece));
            if (seeScore < 0){
                return seeScore;
            } 
            myPiece++;
            if (totalFriends - myPiece == 0){
                return seeScore;
            }
            seeScore += scoreByPiece(enemyPieces.get(enemyPiece));
            enemyPiece++;
            if (totalEnemies - enemyPiece == 0){
                return seeScore;
            }
        }
    }

    private static int scoreByPiece(Piece piece){
        if (piece.equals(Piece.BLACK_PAWN) || piece.equals(Piece.WHITE_PAWN)){
            return PAWN_SCORE;
        }
        if (piece.equals(Piece.BLACK_KNIGHT) || piece.equals(Piece.WHITE_KNIGHT)){
            return KNIGHT_SCORE;
        }
        if (piece.equals(Piece.BLACK_BISHOP) || piece.equals(Piece.WHITE_BISHOP)){
            return BISHOP_SCORE;
        }
        if (piece.equals(Piece.BLACK_ROOK) || piece.equals(Piece.WHITE_ROOK)){
            return ROOK_SCORE;
        }
        if (piece.equals(BLACK_QUEEN) || piece.equals(WHITE_QUEEN)){
            return QUEEN_SCORE;
        }
        if (piece.equals(BLACK_KING) || piece.equals(WHITE_KING)){
            return KING_SCORE;
        }
        throw new RuntimeException("piece problem");
    }

    // todo
    private static List<Piece> myPiecesThatThreatenSquare(Chessboard board, boolean myColour, long square){
        List<Piece> pieces = new ArrayList<>();

        long pawns, knights, bishops, rooks, queens, king;
        Piece p, n, b, r, q, k;
        if (myColour){
            pawns = board.getWhitePawns();
            knights = board.getWhiteKnights();
            bishops = board.getWhiteBishops();
            rooks = board.getWhiteRooks();
            queens = board.getWhiteQueen();
            king = board.getWhiteKing();
            
            p = Piece.WHITE_PAWN;
            n = Piece.WHITE_KNIGHT;
            b = Piece.WHITE_BISHOP;
            r = Piece.WHITE_ROOK;
            q = Piece.WHITE_QUEEN;
            k = Piece.WHITE_KING;
        }
        else {
            pawns = board.getBlackPawns();
            knights = board.getBlackKnights();
            bishops = board.getBlackBishops();
            rooks = board.getBlackRooks();
            queens = board.getBlackQueen();
            king = board.getBlackKing();

            p = Piece.BLACK_PAWN;
            n = Piece.BLACK_KNIGHT;
            b = Piece.BLACK_BISHOP;
            r = Piece.BLACK_ROOK;
            q = Piece.BLACK_QUEEN;
            k = Piece.BLACK_KING;
        }

        if (pawns != 0) {
            int numberOfThreats = populationCount(PieceMove.singlePawnCaptures(square, !myColour, pawns));
            for (int num = 0; num < numberOfThreats; num++){
                pieces.add(p);
            }
        }
        if (knights != 0) {
            int numberOfThreats = populationCount(PieceMove.singleKnightTable(square, knights));
            for (int num = 0; num < numberOfThreats; num++){
                pieces.add(n);
            }
        }
        if (bishops != 0) {
            int numberOfThreats = populationCount(PieceMove.singleBishopTable(board.allPieces(), myColour, square, bishops));
            for (int num = 0; num < numberOfThreats; num++){
                pieces.add(b);
            }
        }
        if (rooks != 0) {
            int numberOfThreats = populationCount(PieceMove.singleRookTable(board.allPieces(), myColour, square, rooks));
            for (int num = 0; num < numberOfThreats; num++){
                pieces.add(r);
            }
        }
        if (queens != 0) {
            int numberOfThreats = populationCount(PieceMove.singleQueenTable(board.allPieces(), myColour, square, queens));
            for (int num = 0; num < numberOfThreats; num++){
                pieces.add(q);
            }
        }
        if (king != 0) {
            int numberOfThreats = populationCount(PieceMove.singleKingTable(square, king));
            for (int num = 0; num < numberOfThreats; num++){
                pieces.add(k);
            }
        }

        return pieces;
    }
}
