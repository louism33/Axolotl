package moveGeneration;

import chess.Chessboard;
import chess.Move;

import java.util.ArrayList;
import java.util.List;

public class MoveGeneratorPseudo {

    public static List<Move> generateAllPushes(Chessboard board, boolean whiteTurn, long legalPushes){
        List<Move> moves = new ArrayList<>();
        moves.addAll(generateAllPushesWithoutKing(board, whiteTurn, legalPushes));
        moves.addAll(MoveGeneratorKing.masterKingPushes(board, whiteTurn, legalPushes));
        return moves;
    }

    public static List<Move> generateAllPushesWithoutKing(Chessboard board, boolean whiteTurn, long legalPushes){
        List<Move> moves = new ArrayList<>();
        moves.addAll(MoveGeneratorKnight.masterKnightPushes(board, whiteTurn, legalPushes));
        moves.addAll(MoveGeneratorSliding.masterSlidingPushes(board, whiteTurn, legalPushes));
        moves.addAll(MoveGeneratorPawns.masterPawnPushes(board, whiteTurn, legalPushes));

        return moves;
    }

    public static List<Move> generateAllCaptures(Chessboard board, boolean whiteTurn, long legalCaptures){
        List<Move> moves = new ArrayList<>();
        moves.addAll(generateAllCapturesWithoutKing(board, whiteTurn, legalCaptures));
        moves.addAll(MoveGeneratorKing.masterKingCaptures(board, whiteTurn, legalCaptures));
        return moves;
    }

    public static List<Move> generateAllCapturesWithoutKing(Chessboard board, boolean whiteTurn, long legalCaptures){
        List<Move> moves = new ArrayList<>();
        moves.addAll(MoveGeneratorKnight.masterKnightCaptures(board, whiteTurn, legalCaptures));
        moves.addAll(MoveGeneratorSliding.masterSlidingCaptures(board, whiteTurn, legalCaptures));
        moves.addAll(MoveGeneratorPawns.masterPawnCaptures(board, whiteTurn, legalCaptures));

        return moves;
    }

    public static List<Move> generateAllMovesWithoutKing(Chessboard board, boolean whiteTurn, long legalPushes, long legalCaptures){
        List<Move> moves = new ArrayList<>();

        if (legalPushes != 0) {
            moves.addAll(generateAllPushesWithoutKing(board, whiteTurn, legalPushes));
        }
        if (legalCaptures != 0) {
            moves.addAll(generateAllCapturesWithoutKing(board, whiteTurn, legalCaptures));
        }

        return moves;
    }

    public static List<Move> generateAllMoves(Chessboard board, boolean whiteTurn, long legalPushes, long legalCaptures){
        List<Move> moves = new ArrayList<>();

        if (legalPushes != 0) {
            moves.addAll(generateAllPushes(board, whiteTurn, legalPushes));
        }
        if (legalCaptures != 0) {
            moves.addAll(generateAllCaptures(board, whiteTurn, legalCaptures));
        }

        return moves;
    }


    public static long generatePseudoPushTable(Chessboard board, boolean whiteTurn, long legalPushes, long legalCaptures){
        long ans = 0;
        ans |= PieceMoveKnight.masterAttackTableKnights(board, whiteTurn, legalPushes, legalCaptures);
        ans |= PieceMoveSliding.masterAttackTableSliding(board, whiteTurn, legalPushes, legalCaptures);
        ans |= PieceMovePawns.masterPawnPushesTable(board, whiteTurn, legalPushes);
        return ans;
    }

    public static long generatePseudoCaptureTable(Chessboard board, boolean whiteTurn, long legalPushes, long legalCaptures){
        long ans = 0;
        ans |= PieceMoveKnight.masterAttackTableKnights(board, whiteTurn, legalPushes, legalCaptures);
        ans |= PieceMoveSliding.masterAttackTableSliding(board, whiteTurn, legalPushes, legalCaptures);
        ans |= PieceMovePawns.masterPawnCapturesTable(board, whiteTurn, legalCaptures);
        return ans;
    }



}
