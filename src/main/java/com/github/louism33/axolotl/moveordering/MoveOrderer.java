package com.github.louism33.axolotl.moveordering;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.chesscore.BitboardResources;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.IllegalUnmakeException;
import com.github.louism33.chesscore.MoveParser;

import static com.github.louism33.axolotl.moveordering.KillerMoves.killerMoves;
import static com.github.louism33.axolotl.moveordering.KillerMoves.mateKiller;
import static com.github.louism33.axolotl.moveordering.MoveOrderingConstants.*;
import static com.github.louism33.chesscore.BitOperations.newPieceOnSquare;
import static com.github.louism33.chesscore.MoveParser.*;

@SuppressWarnings("FieldCanBeLocal")
public class MoveOrderer {

    /*
    Move Ordering:
    previous Hash Moves, promotions, capture of last moved piece, good captures, killers, killers from earlier plies, 
    castling, bad captures, quiet moves, bad promotions
     */
    public static MoveScore[] orderedMoves(Chessboard board, boolean white, int ply, int hashMove, int aiMove){
        return extractMoves(board, white,
                board.generateLegalMoves(),
                ply, hashMove, aiMove);
    }

    private static MoveScore[] extractMoves(Chessboard board, boolean white, int[] moves, int ply,
                                         int hashMove, int aiMove){
        return orderedMoveScores(board, white, moves, ply, hashMove, aiMove);
    }

    private static MoveScore[] orderedMoveScores(Chessboard board, boolean white, int[] moves, int ply,
                                              int hashMove, int aiMove){
        MoveScore[] moveScores = new MoveScore[0];
        try {
            moveScores = scoreMoves(board, white, moves, ply, hashMove, aiMove);
        } catch (IllegalUnmakeException e) {
            e.printStackTrace();
        }
//        moveScores.sort(Comparator.comparingInt(MoveScore::getScore).reversed());
        return moveScores;
    }

    private static MoveScore[] scoreMoves(Chessboard board, boolean white, int[] moves, int ply,
                                       int hashMove, int aiMove) throws IllegalUnmakeException {
        MoveScore[] unsortedScoredMoves = new MoveScore[MoveParser.numberOfRealMoves(moves)];
        /*
        captures range from +92 to +108. +100 indicates an equal capture (Bxb, Pxp...)
         */
        for (int i = 0; i < moves.length; i++) {
            int move = moves[i];
            if (move == 0){
                break;
            }
            MoveScore moveScore;
            if (move == hashMove) {
                moveScore = new MoveScore(move, hashScore);
            } else if (ply == 0 && move == aiMove) {
                moveScore = new MoveScore(move, aiScore);
            } else if (Engine.getEngineSpecifications().ALLOW_MATE_KILLERS && mateKiller[ply] != 0 && move == (mateKiller[ply])) {
                moveScore = new MoveScore(move, mateKillerScore);
            } else if (board.moveIsCaptureOfLastMovePiece(move)) {
                moveScore = new MoveScore(move, CAPTURE_BIAS_LAST_MOVED_PIECE + mvvLVA(board, move));
            } else if (isPromotionToQueen(move)) {
                moveScore = new MoveScore(move, queenPromotionScore);
            } else if (isPromotionToKnight(move)) {
                moveScore = new MoveScore(move, knightPromotionScore);
            } else if (isPromotionToBishop(move) || isPromotionToRook(move)) {
//                 promotions to rook and bishop are considered right at the end
                moveScore = new MoveScore(move, uninterestingPromotion);
            } else if (Engine.getEngineSpecifications().ALLOW_KILLERS && killerMoves[ply][0] != 0 && killerMoves[ply][0] == move) {
                moveScore = new MoveScore(move, killerOneScore);
            } else if (Engine.getEngineSpecifications().ALLOW_KILLERS && killerMoves[ply][1] != 0 && killerMoves[ply][1] == move) {
                moveScore = new MoveScore(move, killerTwoScore);
            } else if (Engine.getEngineSpecifications().ALLOW_KILLERS && ply >= 2 && killerMoves.length > 2
                    && killerMoves[ply - 2][0] != 0 && killerMoves[ply - 2][0] == move) {
                moveScore = new MoveScore(move, oldKillerScoreOne);
            } else if (Engine.getEngineSpecifications().ALLOW_KILLERS && ply >= 2 && killerMoves.length > 2
                    && killerMoves[ply - 2][1] != 0 && killerMoves[ply - 2][1] == (move)) {
                moveScore = new MoveScore(move, oldKillerScoreTwo);
            } else if (checkingMove(board, move)) {
                moveScore = new MoveScore(move, giveCheckMove);
            } else if (moveIsCapture(board, move)) {
                moveScore = new MoveScore(move, mvvLVA(board, move));
            } else if (MoveParser.isCastlingMove(move)) {
                moveScore = new MoveScore(move, castlingMove);
            } else if (Engine.getEngineSpecifications().ALLOW_HISTORY_MOVES) {
                moveScore = new MoveScore(move, HistoryMoves.historyMoveScore(move));
            } else {
                moveScore = new MoveScore(move, uninterestingMove);
            }

            unsortedScoredMoves[i] = moveScore;
        }

        return unsortedScoredMoves;
    }


    private static int mvvLVA (Chessboard board, int move){
        int sourceScore = scoreByPiece(board, move, newPieceOnSquare(MoveParser.getSourceIndex(move)));
        int destinationScore = scoreByPiece(board, move, newPieceOnSquare(MoveParser.getDestinationIndex(move)));
        return CAPTURE_BIAS + destinationScore - sourceScore;
    }

    private static int scoreByPiece(Chessboard board, int move, long piece){
        if (((piece & board.getWhitePawns()) != 0) || ((piece & board.getBlackPawns()) != 0)){
            return 1;
        }
        else if (((piece & board.getWhiteKnights()) != 0) || ((piece & board.getBlackKnights()) != 0)){
            return 3;
        }
        else if (((piece & board.getWhiteBishops()) != 0) || ((piece & board.getBlackBishops()) != 0)){
            return 4;
        }
        else if (((piece & board.getWhiteRooks()) != 0) || ((piece & board.getBlackRooks()) != 0)){
            return 5;
        }
        else if (((piece & board.getWhiteQueen()) != 0) || ((piece & board.getBlackQueen()) != 0)){
            return 9;
        }
        else if (((piece & board.getWhiteKing()) != 0) || ((piece & board.getBlackKing()) != 0)){
            return 10;
        }
        else {
            throw new RuntimeException("score by piece problem "+ move);
        }
    }

    /*
    Quiescence Search ordering:
    order moves by most valuable victim and least valuable aggressor
     */
    public static MoveScore[] orderMovesQuiescence(Chessboard board, boolean white, int[] allMoves){
        return extractMovesQuiescence(board, white, allMoves);
    }

    private static MoveScore[] extractMovesQuiescence(Chessboard board, boolean white, int[] moves){
        return orderedMoveScoresQuiescence(board, white, moves);
    }

    private static MoveScore[] orderedMoveScoresQuiescence(Chessboard board, boolean white, int[] moves){
        MoveScore[] moveScores = scoreMovesQuiescence(board, white, moves);
//        moveScores.sort(Comparator.comparingInt(MoveScore::getScore).reversed());
        return moveScores;
    }

    private static MoveScore[] scoreMovesQuiescence(Chessboard board, boolean white, int[] moves){
        MoveScore[] unsortedScoredMoves = new MoveScore[MoveParser.numberOfRealMoves(moves)];
        for (int i = 0; i < unsortedScoredMoves.length; i++) {
            int move = moves[i];
            if (moveIsCapture(board, move)) {
                if (isPromotionMove(move)) {
                    /*
                    ignore under promotions in Q search
                     */
                    if (isPromotionToQueen(move)) {
                        unsortedScoredMoves[i] = new MoveScore(move, queenPromotionScore + 1);
                    }
                } else if (board.moveIsCaptureOfLastMovePiece(move)) {
                    unsortedScoredMoves[i] = new MoveScore(move, CAPTURE_BIAS_LAST_MOVED_PIECE + mvvLVA(board, move));
                } else {
                    unsortedScoredMoves[i] = new MoveScore(move, mvvLVA(board, move));
                }
            } else if (isPromotionMove(move)) {
                if (isPromotionToQueen(move)) {
                    unsortedScoredMoves[i] = new MoveScore(move, queenPromotionScore);
                }
            }
        }
        return unsortedScoredMoves;
    }

    public static boolean moveIsCapture(Chessboard board, int move){
        long ENEMY_PIECES = board.isWhiteTurn() ? board.blackPieces() : board.whitePieces();
        long destinationSquare = newPieceOnSquare(MoveParser.getDestinationIndex(move));
        return (destinationSquare & ENEMY_PIECES) != 0;
    }

    public static boolean checkingMove(Chessboard board, int move) throws IllegalUnmakeException {
        board.makeMoveAndFlipTurn(move);
        boolean checkingMove = board.inCheck(board.isWhiteTurn());
        board.unMakeMoveAndFlipTurn();
        return checkingMove;
    }

    public static boolean moveWillBePawnPushSix(Chessboard board, int move){
        long myPawns = board.isWhiteTurn() ? board.getWhitePawns() : board.getBlackPawns();

        if (board.isWhiteTurn()){
            if ((newPieceOnSquare(MoveParser.getSourceIndex(move)) & myPawns) != 0){
                return false;
            }
            return (MoveParser.getDestinationIndex(move) & BitboardResources.RANK_SIX) != 0;
        }
        else {
            if ((newPieceOnSquare(MoveParser.getSourceIndex(move)) & myPawns) != 0){
                return false;
            }
            return (newPieceOnSquare(MoveParser.getDestinationIndex(move)) & BitboardResources.RANK_THREE) != 0;
        }
    }

    public static boolean moveWillBePawnPushSeven(Chessboard board, int move){
        long myPawns = board.isWhiteTurn() ? board.getWhitePawns() : board.getBlackPawns();

        if (board.isWhiteTurn()){
            if ((newPieceOnSquare(MoveParser.getSourceIndex(move)) & myPawns) != 0){
                return false;
            }
            return (newPieceOnSquare(MoveParser.getDestinationIndex(move)) & BitboardResources.RANK_SEVEN) != 0;
        }
        else {
            if ((newPieceOnSquare(MoveParser.getSourceIndex(move)) & myPawns) != 0){
                return false;
            }
            return (newPieceOnSquare(MoveParser.getDestinationIndex(move)) & BitboardResources.RANK_TWO) != 0;
        }
    }

    public static void updateHistoryMoves(int move, int ply){
        HistoryMoves.updateHistoryMoves(move, ply);
    }

    public static class MoveScore {
        private final int move;
        private final int score;

        MoveScore(int move, int score) {
            this.move = move;
            this.score = score;
        }

        public int getMove() {
            return move;
        }

        int getScore() {
            return score;
        }

        @Override
        public String toString() {
            return "MoveScore{" +
                    "move=" + move +
                    ", score=" + score +
                    '}';
        }
    }

    public static int numberOfRealMoves(int[] moves){
        int index = 0;
        while (moves[index] != 0){
            index++;
        }

        return index;
    }
}
