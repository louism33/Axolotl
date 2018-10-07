package moveGeneration;

import bitboards.BitBoards;
import chess.Chessboard;
import chess.Move;

import java.util.ArrayList;
import java.util.List;

public class MoveGeneratorPseudo {

    public static List<Move> generateAllPushes(Chessboard board, boolean whiteTurn,
                                               long ignoreThesePieces, long legalPushes){
        List<Move> moves = new ArrayList<>();
        moves.addAll(generateAllPushesWithoutKing(board, whiteTurn, ignoreThesePieces, legalPushes));
        moves.addAll(MoveGeneratorKing.masterKingPushes(board, whiteTurn, ignoreThesePieces, legalPushes));
        return moves;
    }

    public static List<Move> generateAllPushesWithoutKing(Chessboard board, boolean whiteTurn,
                                                          long ignoreThesePieces, long legalPushes){
        List<Move> moves = new ArrayList<>();
        moves.addAll(MoveGeneratorKnight.masterKnightPushes(board, whiteTurn, ignoreThesePieces, legalPushes));
        moves.addAll(MoveGeneratorSliding.masterSlidingPushes(board, whiteTurn, ignoreThesePieces, legalPushes));


        // remove promotable pawns here, as their moves are generated separately
        if (whiteTurn) {
            long PENULTIMATE_RANK = BitBoards.RANK_SEVEN;
            long promotablePawns = board.WHITE_PAWNS & PENULTIMATE_RANK;

            moves.addAll(MoveGeneratorPawns.masterPawnPushes(board, whiteTurn, ignoreThesePieces, legalPushes));
        }


            return moves;
    }

    public static List<Move> generateAllCaptures(Chessboard board, boolean whiteTurn,
                                                 long ignoreThesePieces, long legalCaptures){
        List<Move> moves = new ArrayList<>();
        moves.addAll(generateAllCapturesWithoutKing(board, whiteTurn, ignoreThesePieces, legalCaptures));
        moves.addAll(MoveGeneratorKing.masterKingCaptures(board, whiteTurn, ignoreThesePieces, legalCaptures));
        return moves;
    }

    public static List<Move> generateAllCapturesWithoutKing(Chessboard board, boolean whiteTurn,
                                                            long ignoreThesePieces, long legalCaptures){
        List<Move> moves = new ArrayList<>();
        moves.addAll(MoveGeneratorKnight.masterKnightCaptures(board, whiteTurn, ignoreThesePieces, legalCaptures));
        moves.addAll(MoveGeneratorSliding.masterSlidingCaptures(board, whiteTurn, ignoreThesePieces, legalCaptures));
        moves.addAll(MoveGeneratorPawns.masterPawnCaptures(board, whiteTurn, ignoreThesePieces, legalCaptures));

        return moves;
    }

    public static List<Move> generateAllMovesWithoutKing(Chessboard board, boolean whiteTurn,
                                                         long ignoreThesePieces, long legalPushes, long legalCaptures){
        List<Move> moves = new ArrayList<>();

        if (legalPushes != 0) {
            moves.addAll(generateAllPushesWithoutKing(board, whiteTurn, ignoreThesePieces, legalPushes));
        }
        if (legalCaptures != 0) {
            moves.addAll(generateAllCapturesWithoutKing(board, whiteTurn, ignoreThesePieces, legalCaptures));
        }

        return moves;
    }

    public static List<Move> generateAllMoves(Chessboard board, boolean whiteTurn,
                                              long ignoreThesePieces, long legalPushes, long legalCaptures){
        List<Move> moves = new ArrayList<>();

        if (legalPushes != 0) {
            moves.addAll(generateAllPushes(board, whiteTurn, ignoreThesePieces, legalPushes));
        }
        if (legalCaptures != 0) {
            moves.addAll(generateAllCaptures(board, whiteTurn, ignoreThesePieces, legalCaptures));
        }

        return moves;
    }


    public static long generatePseudoPushTable(Chessboard board, boolean whiteTurn,
                                               long ignoreThesePieces, long legalPushes, long legalCaptures){
        long ans = 0;
        ans |= PieceMoveKnight.masterAttackTableKnights(board, whiteTurn, ignoreThesePieces, legalPushes, legalCaptures);
        ans |= PieceMoveSliding.masterAttackTableSliding(board, whiteTurn, ignoreThesePieces, legalPushes, legalCaptures);
        ans |= PieceMovePawns.masterPawnPushesTable(board, whiteTurn, ignoreThesePieces, legalPushes);
        return ans;
    }

    public static long generatePseudoCaptureTable(Chessboard board, boolean whiteTurn,
                                                  long ignoreThesePieces, long legalPushes, long legalCaptures){
        long ans = 0;
        ans |= PieceMoveKnight.masterAttackTableKnights(board, whiteTurn, ignoreThesePieces, legalPushes, legalCaptures);
        ans |= PieceMoveSliding.masterAttackTableSliding(board, whiteTurn, ignoreThesePieces, legalPushes, legalCaptures);
        ans |= PieceMovePawns.masterPawnCapturesTable(board, whiteTurn, ignoreThesePieces, legalCaptures);
        return ans;
    }



}
