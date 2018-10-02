package moveGeneration;

import check.CheckChecker;
import check.CheckMoveOrganiser;
import chess.BitIndexing;
import chess.Chessboard;
import chess.Move;
import pinnedPieces.BoardWithoutPinnedPieces;

import java.util.ArrayList;
import java.util.List;

public class MoveGeneratorMaster {

    public static List<Move> generateLegalMoves(Chessboard board, boolean whiteTurn) {
        if (CheckChecker.boardInCheck(board, whiteTurn)){
            return CheckMoveOrganiser.evadeCheckMovesMaster(board, whiteTurn);
        }
        return notInCheckMoves(board, whiteTurn);
    }


    private static List<Move> notInCheckMoves(Chessboard board, boolean whiteTurn){
        List<Move> moves = new ArrayList<>();
        Chessboard boardWithoutPins = BoardWithoutPinnedPieces.removePinnedPieces(board, whiteTurn);

        //todo: careful not to forget that pinned pieces can still block stuff
//        List<Move> pinnedPiecesMoves = MoveGenerationPinnedPieces.masterMovePinned(board, whiteTurn, BitIndexing.UNIVERSE, BitIndexing.UNIVERSE);

        List<Move> regularPiecesMoves = MoveGeneratorPseudo.generatePseudoMoves(boardWithoutPins, whiteTurn, BitIndexing.UNIVERSE, BitIndexing.UNIVERSE);


//        moves.addAll(pinnedPiecesMoves);
        moves.addAll(regularPiecesMoves);

        return moves;


    }





}
