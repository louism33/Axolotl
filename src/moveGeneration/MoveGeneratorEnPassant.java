package moveGeneration;

import bitboards.BitBoards;
import chess.BitExtractor;
import chess.BitIndexing;
import chess.Chessboard;
import chess.Move;

import java.util.ArrayList;
import java.util.List;

public class MoveGeneratorEnPassant {

    public static List<Move> generateEnPassantMoves(Chessboard board, boolean white, long ignoreThesePieces) {
        List<Move> moves = new ArrayList<>();
        List<Move> movesBeforeFlag = new ArrayList<>();

        long myPawns = white ? board.WHITE_PAWNS : board.BLACK_PAWNS;
        long enemyPawns = white ? board.BLACK_PAWNS : board.WHITE_PAWNS;
        long enPassantTakingRank = white ? BitBoards.RANK_FIVE : BitBoards.RANK_FOUR;

        long myPawnsInPosition = myPawns & enPassantTakingRank;
        if (myPawnsInPosition == 0) return moves;

        long enemyPawnsInPosition = enemyPawns & enPassantTakingRank;
        if (enemyPawnsInPosition == 0) return moves;

        List<Long> allEnemyPawnsInPosition = BitExtractor.getAllPieces(enemyPawnsInPosition, ignoreThesePieces);

        long enemyTakingSpots = 0;
        for (Long enemyPawn : allEnemyPawnsInPosition){
            long takingSpot = white ? enemyPawn << 8 : enemyPawn >>> 8;
            enemyTakingSpots |= takingSpot;
        }

        List<Long> allMyPawnsInPosition = BitExtractor.getAllPieces(myPawnsInPosition, ignoreThesePieces);

        for (Long myPawn : allMyPawnsInPosition){
            int indexOfFirstPiece = BitIndexing.getIndexOfFirstPiece(myPawn);
            long pawnPossibleEnPassantCapture = PieceMovePawns.singlePawnCaptures(board, myPawn, white, enemyTakingSpots);
            List<Move> moveBeforeFlag = MoveGenerationUtilities.movesFromAttackBoard(pawnPossibleEnPassantCapture, indexOfFirstPiece);
            movesBeforeFlag.addAll(moveBeforeFlag);
        }


        boolean enPassantFlag = false;

        if (enPassantFlag){
            int indexOfEPMove = 0;
            Move legalEPMove = movesBeforeFlag.get(indexOfEPMove);
            moves.add(legalEPMove);
        }

        return new ArrayList<>();
//        return moves;
    }


}
