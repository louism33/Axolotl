package javacode.chessengine.search;

import javacode.chessengine.evaluation.Evaluator;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.PieceMoveKing;
import javacode.chessprogram.moveGeneration.PieceMoveKnight;
import javacode.chessprogram.moveGeneration.PieceMovePawns;
import javacode.chessprogram.moveGeneration.PieceMoveSliding;

import java.util.ArrayList;
import java.util.List;

import static javacode.chessengine.evaluation.EvaluationConstants.*;
import static javacode.chessprogram.chess.BitIndexing.populationCount;
import static javacode.chessprogram.chess.BitManipulations.newPieceOnSquare;

public class SEEPruning {

    enum Piece {
        WHITE_PAWN, WHITE_KNIGHT, WHITE_BISHOP, WHITE_ROOK, WHITE_QUEEN, WHITE_KING,
        BLACK_PAWN, BLACK_KNIGHT, BLACK_BISHOP, BLACK_ROOK, BLACK_QUEEN, BLACK_KING
    }

    static int seeScore(Chessboard board, Move move, Evaluator evaluator){
        long destinationSquare = newPieceOnSquare(move.destinationIndex);
        boolean iAmWhite = board.isWhiteTurn();
        List<Piece> myPieces = myPiecesThatThreatenSquare(board, iAmWhite, destinationSquare);
        List<Piece> enemyPieces = myPiecesThatThreatenSquare(board, !iAmWhite, destinationSquare);

        if (enemyPieces.size() > 0) {
            return seeHelper(board, evaluator, move, myPieces, enemyPieces, destinationSquare);
        }
        
        return evaluator.getScoreOfDestinationPiece(board, move);
    }
    
    private static int seeHelper(Chessboard board, Evaluator evaluator, Move move,
                                 List<Piece> myPieces, List<Piece> enemyPieces, long destinationSquare){
 
        int seeScore = evaluator.getScoreOfDestinationPiece(board, move);
        int totalFriends = myPieces.size();
        int myPiece = 0;
        int totalEnemies = enemyPieces.size();
        int enemyPiece = 0;
        
        while (true){
            seeScore -= scoreByPiece(evaluator, myPieces.get(myPiece));
            if (seeScore < 0){
                return seeScore;
            } 
            myPiece++;
            if (totalFriends - myPiece == 0){
                return seeScore;
            }
            seeScore += scoreByPiece(evaluator, enemyPieces.get(enemyPiece));
            enemyPiece++;
            if (totalEnemies - enemyPiece == 0){
                return seeScore;
            }
        }
    }

    private static int scoreByPiece(Evaluator evaluator, Piece piece){
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
        if (piece.equals(Piece.BLACK_QUEEN) || piece.equals(Piece.WHITE_QUEEN)){
            return QUEEN_SCORE;
        }
        if (piece.equals(Piece.BLACK_KING) || piece.equals(Piece.WHITE_KING)){
            return KING_SCORE;
        }
        throw new RuntimeException("piece problem");
    }

    private static List<Piece> myPiecesThatThreatenSquare(Chessboard board, boolean myColour, long square){
        List<Piece> pieces = new ArrayList<>();

        long pawns, knights, bishops, rooks, queens, king;
        Piece p, n, b, r, q, k;
        if (myColour){
            pawns = board.WHITE_PAWNS;
            knights = board.WHITE_KNIGHTS;
            bishops = board.WHITE_BISHOPS;
            rooks = board.WHITE_ROOKS;
            queens = board.WHITE_QUEEN;
            king = board.WHITE_KING;
            
            p = Piece.WHITE_PAWN;
            n = Piece.WHITE_KNIGHT;
            b = Piece.WHITE_BISHOP;
            r = Piece.WHITE_ROOK;
            q = Piece.WHITE_QUEEN;
            k = Piece.WHITE_KING;
        }
        else {
            pawns = board.BLACK_PAWNS;
            knights = board.BLACK_KNIGHTS;
            bishops = board.BLACK_BISHOPS;
            rooks = board.BLACK_ROOKS;
            queens = board.BLACK_QUEEN;
            king = board.BLACK_KING;

            p = Piece.BLACK_PAWN;
            n = Piece.BLACK_KNIGHT;
            b = Piece.BLACK_BISHOP;
            r = Piece.BLACK_ROOK;
            q = Piece.BLACK_QUEEN;
            k = Piece.BLACK_KING;
        }

        if (pawns != 0) {
            int numberOfThreats = populationCount(PieceMovePawns.singlePawnCaptures(board, square, !myColour, pawns));
            for (int num = 0; num < numberOfThreats; num++){
                pieces.add(p);
            }
        }
        if (knights != 0) {
            int numberOfThreats = populationCount(PieceMoveKnight.singleKnightTable(board, square, myColour, knights));
            for (int num = 0; num < numberOfThreats; num++){
                pieces.add(n);
            }
        }
        if (bishops != 0) {
            int numberOfThreats = populationCount(PieceMoveSliding.singleBishopCaptures(board, square, myColour, bishops));
            for (int num = 0; num < numberOfThreats; num++){
                pieces.add(b);
            }
        }
        if (rooks != 0) {
            int numberOfThreats = populationCount(PieceMoveSliding.singleRookCaptures(board, square, myColour, rooks));
            for (int num = 0; num < numberOfThreats; num++){
                pieces.add(r);
            }
        }
        if (queens != 0) {
            int numberOfThreats = populationCount(PieceMoveSliding.singleQueenCaptures(board, square, myColour, queens));
            for (int num = 0; num < numberOfThreats; num++){
                pieces.add(q);
            }
        }
        if (king != 0) {
            int numberOfThreats = populationCount(PieceMoveKing.singleKingCaptures(board, square, myColour, king));
            for (int num = 0; num < numberOfThreats; num++){
                pieces.add(k);
            }
        }

        return pieces;
    }
}
