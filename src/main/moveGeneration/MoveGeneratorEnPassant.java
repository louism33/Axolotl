package main.moveGeneration;

import main.bitboards.BitBoards;
import main.check.CheckChecker;
import main.chess.*;
import main.moveMaking.MoveOrganiser;
import main.moveMaking.MoveParser;
import main.moveMaking.MoveUnmaker;
import main.moveMaking.StackMoveData;

import java.util.ArrayList;
import java.util.List;

public class MoveGeneratorEnPassant {

    public static List<Move> generateEnPassantMoves(Chessboard board, boolean white, long ignoreThesePieces) {
        List<Move> moves = new ArrayList<>();

        long myPawns = white ? board.WHITE_PAWNS : board.BLACK_PAWNS;
        long enemyPawns = white ? board.BLACK_PAWNS : board.WHITE_PAWNS;
        long enPassantTakingRank = white ? BitBoards.RANK_FIVE : BitBoards.RANK_FOUR;

        long myPawnsInPosition = myPawns & enPassantTakingRank;
        if (myPawnsInPosition == 0) {
            return new ArrayList<>();
        }

        long enemyPawnsInPosition = enemyPawns & enPassantTakingRank;
        if (enemyPawnsInPosition == 0) {
            return new ArrayList<>();
        }

        StackMoveData previousMove = board.moveStack.peek();
        if (!(previousMove.typeOfSpecialMove == StackMoveData.SpecialMove.ENPASSANTVICTIM)){
            return new ArrayList<>();
        }

        long FILE = extractFileFromInt(previousMove.enPassantFile);

        List<Long> allEnemyPawnsInPosition = BitExtractor.getAllPieces(enemyPawnsInPosition, ignoreThesePieces);

        long enemyTakingSpots = 0;
        for (Long enemyPawn : allEnemyPawnsInPosition){
            long takingSpot = white ? enemyPawn << 8 : enemyPawn >>> 8;
            enemyTakingSpots |= (takingSpot & FILE);
        }

        if (enemyTakingSpots == 0){
            return new ArrayList<>();
        }

        List<Long> allMyPawnsInPosition = BitExtractor.getAllPieces(myPawnsInPosition, ignoreThesePieces);

        for (Long myPawn : allMyPawnsInPosition){
            int indexOfFirstPiece = BitIndexing.getIndexOfFirstPiece(myPawn);
            long pawnEnPassantCapture = PieceMovePawns.singlePawnCaptures(board, myPawn, white, enemyTakingSpots);
            List<Move> epMoves = MoveGenerationUtilities.movesFromAttackBoard(pawnEnPassantCapture, indexOfFirstPiece);
            moves.addAll(epMoves);
        }

        List<Move> safeEPMoves = new ArrayList<>();
        // remove moves that would lead us in check
        for (Move move : moves){
            move.move |= MoveParser.ENPASSANT_MASK;

            MoveOrganiser.makeMoveMaster(board, move);
            boolean enPassantWouldLeadToCheck = CheckChecker.boardInCheck(board, white);
            MoveUnmaker.unMakeMoveMaster(board);
            
            if (enPassantWouldLeadToCheck){
                System.out.println("Unsafe EP Move");
                System.out.println(Art.boardArt(board));
                continue;
            }
            safeEPMoves.add(move);
        }

        return safeEPMoves;
    }


    private static long extractFileFromInt(int file){
        if (file == 1){
            return BitBoards.FILE_A;
        }
        else if (file == 2){
            return BitBoards.FILE_B;
        }
        else if (file == 3){
            return BitBoards.FILE_C;
        }
        else if (file == 4){
            return BitBoards.FILE_D;
        }
        else if (file == 5){
            return BitBoards.FILE_E;
        }
        else if (file == 6){
            return BitBoards.FILE_F;
        }
        else if (file == 7){
            return BitBoards.FILE_G;
        }
        else if (file == 8){
            return BitBoards.FILE_H;
        }
        throw new RuntimeException("Incorrect File gotten from Stack.");

    }


}
