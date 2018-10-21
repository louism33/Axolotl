package javacode.chessengine;

import javacode.chessprogram.chess.BitManipulations;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveGeneration.MoveGeneratorMaster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static javacode.chessprogram.moveMaking.MoveParser.*;

class MoveOrderer {
    
    /*
    history heuristic
    static exchange evalHelper
     */

    static final Move[][] killerMoves = new Move[12][2];

    /*
    Move Ordering:
    previous Hash Moves, promotions, capture of last moved piece, good captures, killers, killers from earlier plies, 
    castling, bad captures, quiet moves, bad promotions
     */
    static List<Move> orderedMoves(Chessboard board, boolean white, int ply, Move hashMove){
        return extractMoves(board, white, 
                MoveGeneratorMaster.generateLegalMoves(board, board.isWhiteTurn()),
                ply, hashMove);
    }

    private static List<Move> extractMoves(Chessboard board, boolean white, List<Move> moves, int ply, Move hashMove){
        List<MoveScore> moveScores = orderedMoveScores(board, white, moves, ply, hashMove);
        return moveScores.stream().map(moveScore -> moveScore.move).collect(Collectors.toList());
    }

    private static List<MoveScore> orderedMoveScores(Chessboard board, boolean white, List<Move> moves, int ply, Move hashMove){
        List<MoveScore> moveScores = scoreMoves(board, white, moves, ply, hashMove);
        moveScores.sort(Comparator.comparingInt(MoveScore::getScore).reversed());
        return moveScores;
    }

    private static List<MoveScore> scoreMoves(Chessboard board, boolean white, List<Move> moves, int ply, Move hashMove){
        List<MoveScore> unsortedScoredMoves = new ArrayList<>();
        int hashScore = 30000, queenPromotionScore = 1000, knightPromotionScore = 350, captureOfLastMovedPiece = 500, killerScore = 50, oldKillerScore = 25, giveCheckMove = 25, castlingMove = 10, uninterestingMove = -30000;

        for (Move move : moves){
            MoveScore moveScore;
            if (move.equals(hashMove)){
                moveScore = new MoveScore(move, hashScore);
            }
            else if (Arrays.asList(killerMoves[ply]).contains(move)){
                moveScore = new MoveScore(move, killerScore);
            }
            else if (ply - 2 >= 0 && Arrays.asList(killerMoves[ply-2]).contains(move)){
                moveScore = new MoveScore(move, oldKillerScore);
            }
            else if (moveIsCaptureOfLastMovePiece(board, move)){
                moveScore = new MoveScore(move, captureOfLastMovedPiece);
            }
            else if (moveGivesCheck(board, move)){
                moveScore = new MoveScore(move, giveCheckMove);
            }
            else if (moveIsCapture(board, move)){
                int sourceScore = scoreByPiece(board, move, BitManipulations.newPieceOnSquare(move.getSourceAsPiece()));
                int destinationScore = 10 * scoreByPiece(board, move, BitManipulations.newPieceOnSquare(move.destination));
                moveScore = new MoveScore(move, destinationScore - sourceScore);
            }
            else if (isPromotionMove(move)){
                if (isQueenPromotionMove(move)) {
                    moveScore = new MoveScore(move, queenPromotionScore);
                }
                else if (isKnightPromotionMove(move)){
                    moveScore = new MoveScore(move, knightPromotionScore);
                }
                else {
                    // promotions to rook and bishop are considered right at the end
                    moveScore = new MoveScore(move, uninterestingMove - 1);
                }
            }
            else if (isCastlingMove(move)){
                moveScore = new MoveScore(move, castlingMove);
            }
            else {
                moveScore = new MoveScore(move, uninterestingMove);
            }

            unsortedScoredMoves.add(moveScore);
        }
        return unsortedScoredMoves;
    }
    
    private static boolean moveGivesCheck(Chessboard board, Move move){
        
        return false;
    }

    private static boolean moveIsCaptureOfLastMovePiece(Chessboard board, Move move){
        if (board.moveStack.size() == 0){
            return false;
        }
        int previousMoveDestination = board.moveStack.peek().move.destination;
        return (move.destination & previousMoveDestination) != 0;
    }

    private static int scoreByPiece(Chessboard board, Move move, long piece){
        if (((piece & board.WHITE_PAWNS) != 0) || ((piece & board.BLACK_PAWNS) != 0)){
            return 10;
        }
        else if (((piece & board.WHITE_KNIGHTS) != 0) || ((piece & board.BLACK_KNIGHTS) != 0)){
            return 30;
        }
        else if (((piece & board.WHITE_BISHOPS) != 0) || ((piece & board.BLACK_BISHOPS) != 0)){
            return 31;
        }
        else if (((piece & board.WHITE_ROOKS) != 0) || ((piece & board.BLACK_ROOKS) != 0)){
            return 50;
        }
        else if (((piece & board.WHITE_QUEEN) != 0) || ((piece & board.BLACK_QUEEN) != 0)){
            return 90;
        }
        else if (((piece & board.WHITE_KING) != 0) || ((piece & board.BLACK_KING) != 0)){
            return 200;
        }
        else {
            throw new RuntimeException("score by piece problem "+ move);
        }
    }

    private static boolean isPromotionMove(Move move){
        return (move.move & SPECIAL_MOVE_MASK) == PROMOTION_MASK;
    }

    private static boolean isQueenPromotionMove(Move move){
        return (move.move & WHICH_PROMOTION) == QUEEN_PROMOTION_MASK;
    }

    private static boolean isKnightPromotionMove(Move move){
        return (move.move & WHICH_PROMOTION) == KNIGHT_PROMOTION_MASK;
    }

    private static boolean isCastlingMove (Move move){
        return (move.move & SPECIAL_MOVE_MASK) == CASTLING_MASK;
    }

    /*
    Quiescence Search ordering:
    order moves by most valuable victim and least valuable aggressor
     */
    static List<Move> orderMovesQuiescence (Chessboard board, boolean white, List<Move> allMoves){
        return extractMovesQuiescence(board, white, allMoves);
    }

    private static List<Move> extractMovesQuiescence(Chessboard board, boolean white, List<Move> moves){
        List<MoveScore> moveScores = orderedMoveScoresQuiescence(board, white, moves);
        return moveScores.stream().map(moveScore -> moveScore.move).collect(Collectors.toList());
    }

    private static List<MoveScore> orderedMoveScoresQuiescence(Chessboard board, boolean white, List<Move> moves){
        List<MoveScore> moveScores = scoreMovesQuiescence(board, white, moves);
        moveScores.sort(Comparator.comparingInt(MoveScore::getScore).reversed());
        return moveScores;
    }
    
    private static List<MoveScore> scoreMovesQuiescence(Chessboard board, boolean white, List<Move> moves){
        List<MoveScore> unsortedScoredMoves = new ArrayList<>();
        for (Move move : moves){
            if (moveIsCapture(board, move)){
                int sourceScore = scoreByPiece(board, move, BitManipulations.newPieceOnSquare(move.getSourceAsPiece()));
                int destinationScore = 10 * scoreByPiece(board, move, BitManipulations.newPieceOnSquare(move.destination));
                unsortedScoredMoves.add(new MoveScore(move, destinationScore - sourceScore));
            }
        }
        return unsortedScoredMoves;
    }
    
    static boolean moveIsCapture(Chessboard board, Move move){
        long ENEMY_PIECES = board.isWhiteTurn() ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();
        long destinationSquare = BitManipulations.newPieceOnSquare(move.destination);
        return (destinationSquare & ENEMY_PIECES) != 0;
    }

    private static class MoveScore {
        private final Move move;
        private final int score;

        MoveScore(Move move, int score) {
            this.move = move;
            this.score = score;
        }

        public Move getMove() {
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
}
