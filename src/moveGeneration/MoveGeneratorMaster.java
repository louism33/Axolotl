package moveGeneration;

import check.CheckChecker;
import check.CheckMoveOrganiser;
import check.KingLegalMoves;
import chess.BitIndexing;
import chess.Chessboard;
import chess.Move;
import pinnedPieces.BoardWithoutPinnedPieces;

import java.util.ArrayList;
import java.util.List;

import static chess.BitIndexing.UNIVERSE;

public class MoveGeneratorMaster {



    public static List<Move> generateLegalMoves(Chessboard board, boolean whiteTurn) {
        if (CheckChecker.boardInCheck(board, whiteTurn)){
            return CheckMoveOrganiser.evadeCheckMovesMaster(board, whiteTurn);
        }
        return notInCheckMoves(board, whiteTurn);
    }




    private static List<Move> notInCheckMoves(Chessboard board, boolean whiteTurn){
        List<Move> moves = new ArrayList<>();

//        List<Move> kingLegalMovesOnly = KingLegalMoves.kingLegalMovesOnly(board, whiteTurn);
//        System.out.println(kingLegalMovesOnly);
//        moves.addAll(kingLegalMovesOnly);






        Chessboard boardWithoutPins = BoardWithoutPinnedPieces.removePinnedPieces(board, whiteTurn);
        List<Move> pinnedMovesOnly = pinnedMoveManager(board, boardWithoutPins, whiteTurn);
        System.out.println(pinnedMovesOnly);
        moves.addAll(pinnedMovesOnly);





//        //modify functions to accept board where we get moves from , and board which we use to generate moves ?
//        //todo: careful not to forget that pinned pieces can still block stuff
//        long ENEMY_PIECES = (whiteTurn) ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();
//        List<Move> regularPiecesMoves = MoveGeneratorPseudo.generateAllMovesWithoutKing
//
//                (board // boardWithoutPins
//
//                        , whiteTurn, ~board.ALL_PIECES(), ENEMY_PIECES);
//        moves.addAll(regularPiecesMoves);
//        System.out.println(regularPiecesMoves);




        return moves;
    }

    public static List<Move> heyThisWorked (Chessboard board, boolean whiteTurn){
        long ENEMY_PIECES = (whiteTurn) ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();
        List<Move> regularPiecesMoves = MoveGeneratorPseudo.generateAllMovesWithoutKing
                (board, whiteTurn, ~board.ALL_PIECES(), ENEMY_PIECES);

        return regularPiecesMoves;
    }



    public static List<Move> pinnedMoveManager(Chessboard originalBoard, Chessboard boardWithoutPins, boolean whiteTurn){
        List<Move> moves = new ArrayList<>();

        /*
        which pieces are pinned, then draw line between them and pinner, then generate moves with this line as mask
         */

        long pinner = 0;
        long victim = 0;

        long ray = CheckMoveOrganiser.extractRayFromTwoPieces(originalBoard, pinner, victim);

        /*
        genMoves ( ,  , ray, pinner)
         */


        return moves;
    }




}
