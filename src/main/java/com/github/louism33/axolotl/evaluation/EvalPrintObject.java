package com.github.louism33.axolotl.evaluation;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.MY_TURN_BONUS;
import static com.github.louism33.axolotl.evaluation.Evaluator.percentOfStartgame;
import static com.github.louism33.chesscore.BoardConstants.BLACK;
import static com.github.louism33.chesscore.BoardConstants.WHITE;

public class EvalPrintObject {

    int[][] scores = new int[2][32];
    
    static final int totalScore = 0;
    static final int materialScore = 1;
    static final int imbalanceScore = 2;
    static final int pawnScore = 3;
    static final int knightScore = 4;
    static final int bishopScore = 5;
    static final int rookScore = 6;
    static final int queenScore = 7;
    static final int mobilityScore = 8;
    static final int kingSafetyScore = 9;
    static final int threatsScore = 10;
    static final int passedPawnsScore = 11;
    static final int positionScore = 12;   
    static final int spaceScore = 13;   
    static int turn = WHITE;
    static int percentOfEndgame;

    public EvalPrintObject(int[][] scores) {
        System.arraycopy(scores[WHITE], 0 , this.scores[WHITE], 0, scores[WHITE].length);
        System.arraycopy(scores[BLACK], 0 , this.scores[BLACK], 0, scores[BLACK].length);
    }

    @Override
    public String toString() {
        String evalString = "   Aspect    |     White     |     Black     |    Total  \n" +
                "             |               |               |           \n" +
                "-------------+---------------+---------------+-----------\n" +

                String.format("  Material   |     % 5d     |     % 5d     |    % 5d\n",
                        this.scores[WHITE][materialScore], this.scores[BLACK][materialScore], (this.scores[WHITE][materialScore] - this.scores[BLACK][materialScore])) +

                String.format("  Position   |     % 5d     |     % 5d     |    % 5d\n",
                        this.scores[WHITE][positionScore], this.scores[BLACK][positionScore], (this.scores[WHITE][positionScore] - this.scores[BLACK][positionScore])) +

                String.format("   Pawns     |     % 5d     |     % 5d     |    % 5d\n",
                        this.scores[WHITE][pawnScore], this.scores[BLACK][pawnScore], (this.scores[WHITE][pawnScore] - this.scores[BLACK][pawnScore])) +

                String.format("  Knights    |     % 5d     |     % 5d     |    % 5d\n",
                        this.scores[WHITE][knightScore], this.scores[BLACK][knightScore], (this.scores[WHITE][knightScore] - this.scores[BLACK][knightScore])) +

                String.format("  Bishops    |     % 5d     |     % 5d     |    % 5d\n",
                        this.scores[WHITE][bishopScore], this.scores[BLACK][bishopScore], (this.scores[WHITE][bishopScore] - this.scores[BLACK][bishopScore])) +

                String.format("   Rooks     |     % 5d     |     % 5d     |    % 5d\n",
                        this.scores[WHITE][rookScore], this.scores[BLACK][rookScore], (this.scores[WHITE][rookScore] - this.scores[BLACK][rookScore])) +

                String.format("  Queens     |     % 5d     |     % 5d     |    % 5d\n",
                        this.scores[WHITE][queenScore], this.scores[BLACK][queenScore], (this.scores[WHITE][queenScore] - this.scores[BLACK][queenScore])) +

                String.format("  Mobility   |     % 5d     |     % 5d     |    % 5d\n",
                        this.scores[WHITE][mobilityScore], this.scores[BLACK][mobilityScore], (this.scores[WHITE][mobilityScore] - this.scores[BLACK][mobilityScore])) +

                String.format("   Space     |     % 5d     |     % 5d     |    % 5d\n",
                        this.scores[WHITE][spaceScore], this.scores[BLACK][spaceScore], (this.scores[WHITE][spaceScore] - this.scores[BLACK][spaceScore])) +
                
                String.format("King Safety  |     % 5d     |     % 5d     |    % 5d\n",
                        this.scores[WHITE][kingSafetyScore], this.scores[BLACK][kingSafetyScore], (this.scores[WHITE][kingSafetyScore] - this.scores[BLACK][kingSafetyScore])) +

                String.format("  Threats    |     % 5d     |     % 5d     |    % 5d\n",
                        this.scores[WHITE][threatsScore], this.scores[BLACK][threatsScore], (this.scores[WHITE][threatsScore] - this.scores[BLACK][threatsScore])) +

                String.format("Passed Pawns |     % 5d     |     % 5d     |    % 5d\n",
                        this.scores[WHITE][passedPawnsScore], this.scores[BLACK][passedPawnsScore], (this.scores[WHITE][passedPawnsScore] - this.scores[BLACK][passedPawnsScore])) +

                ""
                + "\nTurn bonus: " + ((percentOfStartgame * MY_TURN_BONUS) / 100)
                + "\nWe are " + percentOfEndgame +"% in the endgame"
                + "\nTotal from white's POV: " + ((turn == BLACK ? -1 : 1) * this.scores[WHITE][totalScore]) + " cp";

        return evalString;
    }
}
