package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.MoveParser;
import org.apache.maven.project.ModelUtils;

import static com.github.louism33.axolotl.evaluation.Evaluator.blackThreatsToSquare;
import static com.github.louism33.axolotl.evaluation.Evaluator.whiteThreatsToSquare;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MoveParser.*;

public class MoveTable {

    /*
    00000000
    00000000
    ttttkkqq
    rrbbnnpp
     */
    public static final int PAWN_ATTACKS_INCREMENT = 0x1;
    public static final int PAWN_ATTACKS = 0x3;

    public static final int KNIGHT_ATTACKS_INCREMENT = 0x4;
    public static final int KNIGHT_ATTACKS = 0xc;
    public static final int knightAttackOffset = 2;

    public static final int BISHOP_ATTACKS_INCREMENT = 0x10;
    public static final int BISHOP_ATTACKS = 0x30;
    public static final int bishopAttackOffset = 4;

    public static final int ROOK_ATTACKS_INCREMENT = 0x40;
    public static final int ROOK_ATTACKS = 0xc0;
    public static final int rookAttackOffset = 6;

    public static final int QUEEN_ATTACKS_INCREMENT = 0x100;
    public static final int QUEEN_ATTACKS = 0x300;
    public static final int queenAttackOffset = 8;

    public static final int KING_ATTACKS_INCREMENT = 0x400;
    public static final int KING_ATTACKS = 0xc00;
    public static final int kingAttackOffset = 10;

    public static final int TOTAL_ATTACKS_INCREMENT = 0x1000;
    public static final int TOTAL_ATTACKS = 0xf000;
    public static final int totalAttackOffset = 12;

    static int getTotalAttacksToSquare(boolean white, int squareIndex) {
        if (white) {
            return (whiteThreatsToSquare[squareIndex] & TOTAL_ATTACKS) >>> totalAttackOffset;
        }
        else {
            return (blackThreatsToSquare[squareIndex] & TOTAL_ATTACKS) >>> totalAttackOffset;
        }
    }

    static int numberOfAttacksBy(boolean white, int squareIndex, int piece) {
        switch (piece){
            case WHITE_PAWN:
                return (whiteThreatsToSquare[squareIndex] & PAWN_ATTACKS);
            case WHITE_KNIGHT:
                return (whiteThreatsToSquare[squareIndex] & KNIGHT_ATTACKS) >>> knightAttackOffset;
            case WHITE_BISHOP:
                return (whiteThreatsToSquare[squareIndex] & BISHOP_ATTACKS) >>> bishopAttackOffset;
            case WHITE_ROOK:
                return (whiteThreatsToSquare[squareIndex] & ROOK_ATTACKS) >>> rookAttackOffset;
            case WHITE_QUEEN:
                return (whiteThreatsToSquare[squareIndex] & QUEEN_ATTACKS) >>> queenAttackOffset;
            case WHITE_KING:
                return (whiteThreatsToSquare[squareIndex] & KING_ATTACKS) >>> kingAttackOffset;

            case BLACK_PAWN:
                return (blackThreatsToSquare[squareIndex] & PAWN_ATTACKS);
            case BLACK_KNIGHT:
                return (blackThreatsToSquare[squareIndex] & KNIGHT_ATTACKS) >>> knightAttackOffset;
            case BLACK_BISHOP:
                return (blackThreatsToSquare[squareIndex] & BISHOP_ATTACKS) >>> bishopAttackOffset;
            case BLACK_ROOK:
                return (blackThreatsToSquare[squareIndex] & ROOK_ATTACKS) >>> rookAttackOffset;
            case BLACK_QUEEN:
                return (blackThreatsToSquare[squareIndex] & QUEEN_ATTACKS) >>> queenAttackOffset;
            case BLACK_KING:
                return (blackThreatsToSquare[squareIndex] & KING_ATTACKS) >>> kingAttackOffset;
        }
        return 0;
    }
    
    static void populateFromMoves(int[] moves) {
        for (int i = 0; i < moves.length; i++) {
            int move = moves[i];
            if (move == 0) {
                break;
            }
            int destinationIndex = MoveParser.getDestinationIndex(move);

            switch (MoveParser.getMovingPieceInt(move)){
                case WHITE_PAWN:
                    whiteThreatsToSquare[destinationIndex] += PAWN_ATTACKS_INCREMENT;
                    whiteThreatsToSquare[destinationIndex] += TOTAL_ATTACKS_INCREMENT;
                    break;
                case WHITE_KNIGHT:
                    whiteThreatsToSquare[destinationIndex] += KNIGHT_ATTACKS_INCREMENT;
                    whiteThreatsToSquare[destinationIndex] += TOTAL_ATTACKS_INCREMENT;
                    break;
                case WHITE_BISHOP:
                    whiteThreatsToSquare[destinationIndex] += BISHOP_ATTACKS_INCREMENT;
                    whiteThreatsToSquare[destinationIndex] += TOTAL_ATTACKS_INCREMENT;
                    break;
                case WHITE_ROOK:
                    whiteThreatsToSquare[destinationIndex] += ROOK_ATTACKS_INCREMENT;
                    whiteThreatsToSquare[destinationIndex] += TOTAL_ATTACKS_INCREMENT;
                    break;
                case WHITE_QUEEN:
                    whiteThreatsToSquare[destinationIndex] += QUEEN_ATTACKS_INCREMENT;
                    whiteThreatsToSquare[destinationIndex] += TOTAL_ATTACKS_INCREMENT;
                    break;
                case WHITE_KING:
                    whiteThreatsToSquare[destinationIndex] += KING_ATTACKS_INCREMENT;
                    whiteThreatsToSquare[destinationIndex] += TOTAL_ATTACKS_INCREMENT;
                    break;

                case BLACK_PAWN:
                    blackThreatsToSquare[destinationIndex] += PAWN_ATTACKS_INCREMENT;
                    blackThreatsToSquare[destinationIndex] += TOTAL_ATTACKS_INCREMENT;
                    break;
                case BLACK_KNIGHT:
                    blackThreatsToSquare[destinationIndex] += KNIGHT_ATTACKS_INCREMENT;
                    blackThreatsToSquare[destinationIndex] += TOTAL_ATTACKS_INCREMENT;
                    break;
                case BLACK_BISHOP:
                    blackThreatsToSquare[destinationIndex] += BISHOP_ATTACKS_INCREMENT;
                    blackThreatsToSquare[destinationIndex] += TOTAL_ATTACKS_INCREMENT;
                    break;
                case BLACK_ROOK:
                    blackThreatsToSquare[destinationIndex] += ROOK_ATTACKS_INCREMENT;
                    blackThreatsToSquare[destinationIndex] += TOTAL_ATTACKS_INCREMENT;
                    break;
                case BLACK_QUEEN:
                    blackThreatsToSquare[destinationIndex] += QUEEN_ATTACKS_INCREMENT;
                    blackThreatsToSquare[destinationIndex] += TOTAL_ATTACKS_INCREMENT;
                    break;
                case BLACK_KING:
                    blackThreatsToSquare[destinationIndex] += KING_ATTACKS_INCREMENT;
                    blackThreatsToSquare[destinationIndex] += TOTAL_ATTACKS_INCREMENT;
                    break;
            }

        }
    }
}
