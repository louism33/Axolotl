package javacode.chessengine;

import javacode.chessprogram.bitboards.BitBoards;
import javacode.chessprogram.check.CheckChecker;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveMaking.MoveOrganiser;
import javacode.chessprogram.moveMaking.MoveParser;
import javacode.chessprogram.moveMaking.MoveUnmaker;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static javacode.chessengine.KillerMoves.killerMoves;
import static javacode.chessengine.KillerMoves.mateKiller;
import static javacode.chessprogram.chess.BitManipulations.newPieceOnSquare;
import static javacode.chessprogram.moveGeneration.MoveGeneratorMaster.generateLegalMoves;
import static javacode.chessprogram.moveMaking.MoveParser.*;

class MoveOrderer {

    private final Engine engine;
    private final HistoryMoves historyMoves;

    final int MAX_HISTORY_MOVE_SCORE = 90;
    private final int CAPTURE_BIAS = 100;
    private final int CAPTURE_BIAS_LAST_MOVED_PIECE = 5;

    private final int
            hashScore = 127,
            aiScore = 126,
            mateKillerScore = 125,
            queenPromotionScore = 109,
            killerOneScore = 102,
            killerTwoScore = 101,
            giveCheckMove = 100,
            oldKillerScoreOne = 99,
            oldKillerScoreTwo = 98,
            castlingMove = 10,
            knightPromotionScore = 2,
            uninterestingMove = 1,
            uninterestingPromotion = 0;

    MoveOrderer(Engine engine){
        this.engine = engine;
        this.historyMoves = new HistoryMoves(this);
    }

    /*
    Move Ordering:
    previous Hash Moves, promotions, capture of last moved piece, good captures, killers, killers from earlier plies, 
    castling, bad captures, quiet moves, bad promotions
     */
    List<Move> orderedMoves(Chessboard board, boolean white, int ply, Move hashMove, Move aiMove){
        return extractMoves(board, white,
                generateLegalMoves(board, board.isWhiteTurn()),
                ply, hashMove, aiMove);
    }

    private List<Move> extractMoves(Chessboard board, boolean white, List<Move> moves, int ply,
                                    Move hashMove, Move aiMove){
        List<MoveScore> moveScores = orderedMoveScores(board, white, moves, ply, hashMove, aiMove);
        List<Move> collect = moveScores.stream().map(moveScore -> moveScore.move).collect(Collectors.toList());
        return collect;
    }

    private List<MoveScore> orderedMoveScores(Chessboard board, boolean white, List<Move> moves, int ply,
                                              Move hashMove, Move aiMove){
        List<MoveScore> moveScores = scoreMoves(board, white, moves, ply, hashMove, aiMove);
        moveScores.sort(Comparator.comparingInt(MoveScore::getScore).reversed());
        return moveScores;
    }

    private List<MoveScore> scoreMoves(Chessboard board, boolean white, List<Move> moves, int ply,
                                       Move hashMove, Move aiMove){
        List<MoveScore> unsortedScoredMoves = new ArrayList<>();
        /*
        captures range from +92 to +108. +100 indicates an equal capture (Bxb, Pxp...)
         */
        for (Move move : moves){
            MoveScore moveScore;
            if (hashMove != null && move.equals(hashMove)){
                moveScore = new MoveScore(move, hashScore);
            }
            else if (aiMove != null && ply == 0 && move.equals(aiMove)){
                moveScore = new MoveScore(move, aiScore);
            }
            else if (this.engine.ALLOW_MATE_KILLERS && mateKiller[ply] != null && move.equals(mateKiller[ply])){
                moveScore = new MoveScore(move, mateKillerScore);
            }
            else if (moveIsCaptureOfLastMovePiece(board, move)){
                moveScore = new MoveScore(move, CAPTURE_BIAS_LAST_MOVED_PIECE + mvvLVA(board, move));
            }
            else if (isPromotionToQueen(move)) {
                moveScore = new MoveScore(move, queenPromotionScore);
            }
            else if (isPromotionToKnight(move)){
                moveScore = new MoveScore(move, knightPromotionScore);
            }
            else if (isPromotionToBishop(move) || isPromotionToRook(move)){
//                 promotions to rook and bishop are considered right at the end
                moveScore = new MoveScore(move, uninterestingPromotion);
            }
            
            else if (this.engine.ALLOW_KILLERS && killerMoves[ply][0] != null && killerMoves[ply][0].equals(move)){
                moveScore = new MoveScore(move, killerOneScore);
            }
            else if (this.engine.ALLOW_KILLERS && killerMoves[ply][1] != null && killerMoves[ply][1].equals(move)){
                moveScore = new MoveScore(move, killerTwoScore);
            }
            else if (this.engine.ALLOW_KILLERS && ply >= 2 && killerMoves.length > 2
                    && killerMoves[ply - 2][0] != null && killerMoves[ply - 2][0].equals(move)){
                moveScore = new MoveScore(move, oldKillerScoreOne);
            }
            else if (this.engine.ALLOW_KILLERS && ply >= 2 && killerMoves.length > 2
                    && killerMoves[ply - 2][1] != null && killerMoves[ply - 2][1].equals(move)){
                moveScore = new MoveScore(move, oldKillerScoreTwo);
            }
            else if (checkingMove(board, move)){
                moveScore = new MoveScore(move, giveCheckMove);
            }
            else if (moveIsCapture(board, move)){
                moveScore = new MoveScore(move, mvvLVA(board, move));
            }
            else if (MoveParser.isCastlingMove(move)){
                moveScore = new MoveScore(move, castlingMove);
            }
            else if (this.engine.ALLOW_HISTORY_MOVES){
                moveScore = new MoveScore(move, this.historyMoves.historyMoveScore(move));
            }
            else {
                moveScore = new MoveScore(move, uninterestingMove);
            }

            unsortedScoredMoves.add(moveScore);
        }

        return unsortedScoredMoves;
    }

    private boolean moveIsCaptureOfLastMovePiece(Chessboard board, Move move){
        if (board.moveStack.size() == 0){
            return false;
        }
        if (board.moveStack.peek().move == null){
            return false;
        }
        int previousMoveDestinationIndex = board.moveStack.peek().move.destinationIndex;
        return (move.destinationIndex == previousMoveDestinationIndex);
    }

    private int mvvLVA (Chessboard board, Move move){
        int sourceScore = scoreByPiece(board, move, newPieceOnSquare(move.getSourceAsPieceIndex()));
        int destinationScore = scoreByPiece(board, move, newPieceOnSquare(move.destinationIndex));
        return CAPTURE_BIAS + destinationScore - sourceScore;
    }

    private int scoreByPiece(Chessboard board, Move move, long piece){
        if (((piece & board.WHITE_PAWNS) != 0) || ((piece & board.BLACK_PAWNS) != 0)){
            return 1;
        }
        else if (((piece & board.WHITE_KNIGHTS) != 0) || ((piece & board.BLACK_KNIGHTS) != 0)){
            return 3;
        }
        else if (((piece & board.WHITE_BISHOPS) != 0) || ((piece & board.BLACK_BISHOPS) != 0)){
            return 4;
        }
        else if (((piece & board.WHITE_ROOKS) != 0) || ((piece & board.BLACK_ROOKS) != 0)){
            return 5;
        }
        else if (((piece & board.WHITE_QUEEN) != 0) || ((piece & board.BLACK_QUEEN) != 0)){
            return 9;
        }
        else if (((piece & board.WHITE_KING) != 0) || ((piece & board.BLACK_KING) != 0)){
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
    List<Move> orderMovesQuiescence (Chessboard board, boolean white, List<Move> allMoves){
        return extractMovesQuiescence(board, white, allMoves);
    }

    private List<Move> extractMovesQuiescence(Chessboard board, boolean white, List<Move> moves){
        List<MoveScore> moveScores = orderedMoveScoresQuiescence(board, white, moves);
        return moveScores.stream().map(moveScore -> moveScore.move).collect(Collectors.toList());
    }

    private List<MoveScore> orderedMoveScoresQuiescence(Chessboard board, boolean white, List<Move> moves){
        List<MoveScore> moveScores = scoreMovesQuiescence(board, white, moves);
        moveScores.sort(Comparator.comparingInt(MoveScore::getScore).reversed());
        return moveScores;
    }

    private List<MoveScore> scoreMovesQuiescence(Chessboard board, boolean white, List<Move> moves){
        List<MoveScore> unsortedScoredMoves = new ArrayList<>();
        for (Move move : moves){
            if (moveIsCapture(board, move)){
                if (isPromotionMove(move)) {
                    /*
                    ignore under promotions in Q search
                     */
                    if (isPromotionToQueen(move)) {
                        unsortedScoredMoves.add(new MoveScore(move, queenPromotionScore + 1));
                    }
                }
                else if (moveIsCaptureOfLastMovePiece(board, move)){
                    unsortedScoredMoves.add(new MoveScore(move, CAPTURE_BIAS_LAST_MOVED_PIECE + mvvLVA(board, move)));
                }
                else {
                    unsortedScoredMoves.add(new MoveScore(move, mvvLVA(board, move)));
                }
            }

            else if (isPromotionMove(move)) {
                if (isPromotionToQueen(move)) {
                    unsortedScoredMoves.add(new MoveScore(move, queenPromotionScore));
                }
            }
        }
        return unsortedScoredMoves;
    }

    boolean moveIsCapture(Chessboard board, Move move){
        long ENEMY_PIECES = board.isWhiteTurn() ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();
        long destinationSquare = newPieceOnSquare(move.destinationIndex);
        return (destinationSquare & ENEMY_PIECES) != 0;
    }

    boolean checkingMove(Chessboard board, Move move){
        Assert.assertTrue(generateLegalMoves(board, board.isWhiteTurn()).contains(move));

        MoveOrganiser.makeMoveMaster(board, move);
        MoveOrganiser.flipTurn(board);
        boolean checkingMove = CheckChecker.boardInCheck(board, board.isWhiteTurn());
        MoveUnmaker.unMakeMoveMaster(board);
        return checkingMove;
    }

    boolean moveWillBePawnPushSix(Chessboard board, Move move){
        long myPawns = board.isWhiteTurn() ? board.WHITE_PAWNS : board.BLACK_PAWNS;

        if (board.isWhiteTurn()){
            if ((newPieceOnSquare(move.getSourceAsPieceIndex()) & myPawns) != 0){
                return false;
            }
            return (newPieceOnSquare(move.destinationIndex) & BitBoards.RANK_SIX) != 0;
        }
        else {
            if ((newPieceOnSquare(move.getSourceAsPieceIndex()) & myPawns) != 0){
                return false;
            }
            return (newPieceOnSquare(move.destinationIndex) & BitBoards.RANK_THREE) != 0;
        }
    }

    boolean moveWillBePawnPushSeven(Chessboard board, Move move){
        long myPawns = board.isWhiteTurn() ? board.WHITE_PAWNS : board.BLACK_PAWNS;

        if (board.isWhiteTurn()){
            if ((newPieceOnSquare(move.getSourceAsPieceIndex()) & myPawns) != 0){
                return false;
            }
            return (newPieceOnSquare(move.destinationIndex) & BitBoards.RANK_SEVEN) != 0;
        }
        else {
            if ((newPieceOnSquare(move.getSourceAsPieceIndex()) & myPawns) != 0){
                return false;
            }
            return (newPieceOnSquare(move.destinationIndex) & BitBoards.RANK_TWO) != 0;
        }
    }

    void updateHistoryMoves(Move move, int ply){
        this.historyMoves.updateHistoryMoves(move, ply);
    }

    private class MoveScore {
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
