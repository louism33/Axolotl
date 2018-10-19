package javacode.chessprogram.moveGeneration;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import java.util.ArrayList;
import java.util.List;

public class MoveGeneratorPseudo {

    public static List<Move> generateAllMovesWithoutKing(Chessboard board, boolean whiteTurn,
                                                         long ignoreThesePieces, long legalPushes, long legalCaptures){
        List<Move> moves = new ArrayList<>();
        if (legalCaptures != 0) {
            moves.addAll(generateAllCapturesWithoutKing(board, whiteTurn, ignoreThesePieces, legalCaptures));
        }
        
        if (legalPushes != 0) {
            moves.addAll(generateAllPushesWithoutKing(board, whiteTurn, ignoreThesePieces, legalPushes));
        }

        return moves;
    }

    private static List<Move> generateAllPushesWithoutKing(Chessboard board, boolean whiteTurn,
                                                          long ignoreThesePieces, long legalPushes){
        List<Move> moves = new ArrayList<>();
        moves.addAll(MoveGeneratorKnight.masterKnightPushes(board, whiteTurn, ignoreThesePieces, legalPushes));
        moves.addAll(MoveGeneratorSliding.masterSlidingPushes(board, whiteTurn, ignoreThesePieces, legalPushes));
        moves.addAll(MoveGeneratorPawns.masterPawnPushes(board, whiteTurn, ignoreThesePieces, legalPushes));

        return moves;
    }

    private static List<Move> generateAllCapturesWithoutKing(Chessboard board, boolean whiteTurn,
                                                             long ignoreThesePieces, long legalCaptures){
        List<Move> moves = new ArrayList<>();
        moves.addAll(MoveGeneratorKnight.masterKnightCaptures(board, whiteTurn, ignoreThesePieces, legalCaptures));
        moves.addAll(MoveGeneratorSliding.masterSlidingCaptures(board, whiteTurn, ignoreThesePieces, legalCaptures));
        moves.addAll(MoveGeneratorPawns.masterPawnCaptures(board, whiteTurn, ignoreThesePieces, legalCaptures));

        return moves;
    }

    public static long generatePseudoCaptureTable(Chessboard board, boolean whiteTurn,
                                                  long ignoreThesePieces, long legalPushes, long legalCaptures){
        long ans = 0;

        ans |= PieceMoveKing.masterAttackTableKing(board, whiteTurn, ignoreThesePieces, legalPushes, legalCaptures);
        
        ans |= PieceMoveKnight.masterAttackTableKnights(board, whiteTurn, ignoreThesePieces, legalPushes, legalCaptures);

        ans |= PieceMoveSliding.masterAttackTableSliding(board, whiteTurn, ignoreThesePieces, legalPushes, legalCaptures);

        ans |= PieceMovePawns.masterPawnCapturesTable(board, whiteTurn, ignoreThesePieces, legalCaptures);

        return ans;
    }

}
