package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.axolotl.evaluation.EvalPrintObject.*;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.K;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.Q;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.evaluation.EvaluatorPositionConstant.POSITION_SCORES;
import static com.github.louism33.axolotl.evaluation.EvaluatorPositionConstant.mobilityScores;
import static com.github.louism33.axolotl.evaluation.Init.kingSafetyArea;
import static com.github.louism33.axolotl.evaluation.PassedPawns.evalPassedPawnsByTurn;
import static com.github.louism33.axolotl.evaluation.PawnTranspositionTable.*;
import static com.github.louism33.axolotl.search.EngineSpecifications.PRINT_EVAL;
import static com.github.louism33.chesscore.BitOperations.fileForward;
import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.PieceMove.*;
import static java.lang.Long.numberOfTrailingZeros;

@SuppressWarnings("ALL")
public final class Evaluator {

    public static void printEval(Chessboard board, int turn){
        printEval(board, turn, board.generateLegalMoves());
    }
    public static void printEval(Chessboard board, int turn, int[] moves){
        System.out.println(stringEval(board, turn, moves));
    }

    public static EvalPrintObject stringEval(Chessboard board, int turn){
        return stringEval(board, turn, board.generateLegalMoves());
    }
    public static EvalPrintObject stringEval(Chessboard board, int turn, int[] moves){
        PRINT_EVAL = true;
        eval(board, moves);
        EvalPrintObject epo = new EvalPrintObject(scoresForEPO);
        epo.turn = board.turn;
        PRINT_EVAL = false;
        return epo;
    }

    /**
     todo:
     trapped pieces
     pinned pieces, and to queen
     */


    public static final int eval(final Chessboard board, final int[] moves) {
        if (!EvaluationConstants.ready) {
            setup();
        }
        if (!EvaluatorPositionConstant.ready) {
            EvaluatorPositionConstant.setup();
        }

        int percentOfEndgame;
        int percentOfStartgame;

        long[] turnThreatensSquares = new long[2];
        
        Assert.assertTrue(moves != null);

        if (PRINT_EVAL) {
            Arrays.fill(scoresForEPO[WHITE], 0);
            Arrays.fill(scoresForEPO[BLACK], 0);
        }

        long myKingSafetyArea = kingSafetyArea(board, turn);
        long enemyKingSafetyArea = kingSafetyArea(board, 1 - turn);

        percentOfStartgame = getPercentageOfStartGame(board);
        percentOfEndgame = 100 - percentOfStartgame;
        
        long[] pawnData = null; // = PawnTranspositionTable.retrieveFromTable(board.zobristPawnHash, percentOfStartgame);
int pawnFeatureScore = 0;
        if (pawnData == null || PRINT_EVAL) {
            pawnData = PawnEval.calculatePawnData(board, percentOfStartgame);
            pawnFeatureScore = (int)pawnData[16];
            PawnTranspositionTable.addToTableReplaceArbitrarily(board.zobristPawnHash, pawnData, pawnFeatureScore);
        }
        int score = 0;

        score += Score.getScore(pawnFeatureScore, percentOfStartgame);

        // todo colour has insuf mat to mate

        final int turn = board.turn;
        
        final int myTurnScore = evalTurn(board, turn, pawnData, turnThreatensSquares, percentOfStartgame, myKingSafetyArea, enemyKingSafetyArea);
        final int yourTurnScore = evalTurn(board, 1 - turn, pawnData, turnThreatensSquares, percentOfStartgame, enemyKingSafetyArea, myKingSafetyArea);

        // todo bring together?
        int myPassedPawnScore = Score.getScore(evalPassedPawnsByTurn(board, turn, pawnData, turnThreatensSquares), percentOfStartgame);
        int enemyPassedPawnScore = Score.getScore(evalPassedPawnsByTurn(board, 1 - turn, pawnData, turnThreatensSquares), percentOfStartgame);

        final int turnBonus = Score.getScore(miscFeatures[MY_TURN_BONUS], percentOfStartgame);

        score += turnBonus;
        score += myTurnScore;
        score -= yourTurnScore;
        score += myPassedPawnScore;
        score -= enemyPassedPawnScore;

        if (PRINT_EVAL) {
            EvalPrintObject.percentOfEndgame = percentOfEndgame;
            scoresForEPO[turn][passedPawnsScore] = myPassedPawnScore;
            scoresForEPO[1 - turn][passedPawnsScore] = enemyPassedPawnScore;
            // hacks
            scoresForEPO[WHITE][turnScore] = turnBonus; 
            scoresForEPO[WHITE][totalScore] = score; // total score from white's pov
        }

        Assert.assertTrue(score > IN_CHECKMATE_SCORE_MAX_PLY);
        Assert.assertTrue(score < CHECKMATE_ENEMY_SCORE_MAX_PLY);

        return score;
    }

    public static int[][] scoresForEPO = new int[2][32];

    private final static int evalTurn(Chessboard board, int turn, long[] pawnData, long[] turnThreatensSquares, int percentOfStartgame, long myKingSafetyArea, long enemyKingSafetyArea){
        //please generate moves before calling this
        final long[][] pieces = board.pieces;

        // todo consider xray instead of real attacks for brq
        
        int kingAttackers = 0;
        int kingAttacks = 0;
        int kingAttackersWeights = 0;
        
        long squaresIThreatenWithPieces = 0;

        long myPawns, myKnights, myBishops, myRooks, myQueens, myKing;
        long enemyPawns, enemyKnights, enemyBishops, enemyRooks, enemyQueens, enemyKing;
        final long friends, enemies;

        myPawns = pieces[turn][PAWN];
        myKnights = pieces[turn][KNIGHT];
        myBishops = pieces[turn][BISHOP];
        myRooks = pieces[turn][ROOK];
        myQueens = pieces[turn][QUEEN];
        myKing = pieces[turn][KING];

        enemyPawns = pieces[1 - turn][PAWN];
        enemyKnights = pieces[1 - turn][KNIGHT];
        enemyBishops = pieces[1 - turn][BISHOP];
        enemyRooks = pieces[1 - turn][ROOK];
        enemyQueens = pieces[1 - turn][QUEEN];
        enemyKing = pieces[1 - turn][KING];

        friends = pieces[turn][ALL_COLOUR_PIECES];
        enemies = pieces[1 - turn][ALL_COLOUR_PIECES];

        Assert.assertTrue(friends != 0);
        Assert.assertTrue(enemies != 0);

        final long allPieces = friends | enemies;
        final long pinnedPieces = board.pinnedPieces;
        final boolean inCheck = board.inCheckRecorder;

        int finalScore = 0, materialScore = 0;

        materialScore += populationCount(myPawns) * material[P];
        materialScore += populationCount(myKnights) * material[K];
        materialScore += populationCount(myBishops) * material[B];
        materialScore += populationCount(myRooks) * material[R];
        materialScore += populationCount(myQueens) * material[Q];

        final long ignoreThesePieces = 0; // maybe pins

        final long enemyBigPieces = enemies & ~enemyPawns;

        int positionScore = 0;
        int mobilityScore = 0;

        int threatsScore = 0;

        if (turn == board.turn) {
            long pins = pinnedPieces;
            while (pins != 0) {
                final int i = numberOfTrailingZeros(pins);
                final int pinnedPiece = board.pieceSquareTable[i];
                final int colourBlindPiece = pinnedPiece < 7 ? pinnedPiece : pinnedPiece - 6;
                threatsScore += pinnedPiecesScores[colourBlindPiece];
                pins &= pins - 1;
            }
        }
        
        final long squaresMyPawnsThreaten = pawnData[CAPTURES + turn];
        final long squaresMyPawnsDoubleThreaten = pawnData[DOUBLE_CAPTURES + turn];
        final long squaresEnemyPawnsThreaten = pawnData[CAPTURES + 1 - turn];
        final long squaresEnemyPawnsDoubleThreaten = pawnData[DOUBLE_CAPTURES + 1 - turn];

        final long myPawnAttackSpan = pawnData[SPANS + turn];
        final long enemyPawnAttackSpan = pawnData[SPANS + 1 - turn];

        final boolean whiteToPlay = turn == WHITE;
        final long outpostRanks = whiteToPlay ? (RANK_FOUR | RANK_FIVE | RANK_SIX) : (RANK_FIVE | RANK_FOUR | RANK_THREE);
        final long unthreatenableOutpostSpots = outpostRanks & ~(enemyPawnAttackSpan | squaresEnemyPawnsThreaten);

        final long fileWithoutMyPawns = pawnData[FILE_WITHOUT_MY_PAWNS + turn];
        final long fileWithoutEnemyPawns = pawnData[FILE_WITHOUT_MY_PAWNS + 1 - turn];
        final long openFiles = fileWithoutEnemyPawns & fileWithoutMyPawns;
        final long closedFiles = ~openFiles;

        final long enemyPawnShifts = whiteToPlay ? enemyPawns >>> 8 : enemyPawns << 8;
        final long myBlockedPawns = enemyPawnShifts & myPawns;
        final long myBackwardsPawns = myPawns & (PENULTIMATE_RANKS[1 - turn] | INTERMEDIATE_RANKS[turn]);

        final long safeMobSquares = ~(myKing | myQueens | myBackwardsPawns | myBlockedPawns | squaresEnemyPawnsThreaten);

        if (percentOfStartgame != 0) {
            //space
            long mySafeSquares = (~myPawns
                    & (whiteToPlay ? (RANK_TWO | RANK_THREE | RANK_FOUR) : (RANK_FIVE | RANK_SIX | RANK_SEVEN))
                    & (FILE_C | FILE_D | FILE_E | FILE_F)
                    & ~squaresEnemyPawnsThreaten);

            int spaceScore = populationCount(mySafeSquares) * miscFeatures[SPACE];

            long myDevelopedPawns = myPawns & ~(RANK_SEVEN | RANK_TWO);
            while (myDevelopedPawns != 0) {
                final int pawnIndex = numberOfTrailingZeros(myDevelopedPawns);
                final long fileBack = fileForward(pawnIndex, turn == BLACK) & mySafeSquares;

                spaceScore += populationCount(fileBack) * (1 + miscFeatures[SPACE]);
                myDevelopedPawns &= myDevelopedPawns - 1;
            }


            spaceScore = Score.getScore(spaceScore, percentOfStartgame);
            
            finalScore += spaceScore;

            if (PRINT_EVAL) {
                scoresForEPO[turn][EvalPrintObject.spaceScore] = spaceScore;
            }
        }

        final long enemyKingSmallArea = squareCentredOnIndex(numberOfTrailingZeros(enemyKing));

        final long myKingSmallArea = squareCentredOnIndex(numberOfTrailingZeros(myKing));

        int defendingMyKingLookup = 0;

        final long allPawns = myPawns | enemyPawns;

        final int numberOfPawns = populationCount(allPawns);

        final long wps = WHITE_COLOURED_SQUARES & allPawns;
        final long bps = BLACK_COLOURED_SQUARES & allPawns;

        final long wpsc = wps & (centreNineSquares ^ centreFourSquares);
        final long bpsc = bps & (centreNineSquares ^ centreFourSquares);

        final long wpscc = wps & centreFourSquares;
        final long bpscc = bps & centreFourSquares;

        final long developpedPawns = myPawns & ~PENULTIMATE_RANKS[1 - turn];
        final long behindPawnSpots = whiteToPlay ? developpedPawns >>> 8 : developpedPawns << 8;

        int knightsScore = 0;

        final long enemyBigPiecesForFork = enemyBigPieces ^ enemyKnights;

        while (myKnights != 0){
            final long knight = getFirstPiece(myKnights);
            if ((knight & ignoreThesePieces) == 0) {
                final int knightIndex = numberOfTrailingZeros(knight);
                positionScore += POSITION_SCORES[turn][KNIGHT][63 - knightIndex];

                final long pseudoMoves = KNIGHT_MOVE_TABLE[knightIndex];
                final long table = pseudoMoves & safeMobSquares;

                squaresIThreatenWithPieces |= pseudoMoves;

                mobilityScore += mobilityScores[KNIGHT - 2][populationCount(table)];

                knightsScore += (numberOfPawns * knightFeatures[KNIGHT_PAWN_NUMBER_BONUS]);

                if (populationCount(pseudoMoves & enemyBigPiecesForFork) > 1) {
                    knightsScore += knightFeatures[KNIGHT_FORK];
                }
                
                if ((knight & squaresMyPawnsThreaten) != 0) {
                    knightsScore += knightFeatures[KNIGHT_PROTECTED_PAWN];
                }

                //outpost, double score if defended by friendly pawn
                if ((knight & unthreatenableOutpostSpots) != 0) {
                    knightsScore += knightFeatures[KNIGHT_ON_OUTPOST_BONUS] * (1 + (populationCount(squaresMyPawnsThreaten & knight)));
                } else {
                    long myThreatsToEmptyOutposts = table & (unthreatenableOutpostSpots & ~friends);
                    if (myThreatsToEmptyOutposts != 0) {
                        knightsScore += knightFeatures[KNIGHT_REACH_OUTPOST_BONUS ]* (1 + (populationCount(squaresMyPawnsThreaten & myThreatsToEmptyOutposts)));
                    }
                }

                if ((knight & behindPawnSpots) != 0) {
                    knightsScore += miscFeatures[PIECE_BEHIND_PAWN];
                }

                final long pseudoAttackEnemyKing = pseudoMoves & enemyKingSafetyArea;
                if (pseudoAttackEnemyKing != 0) {
                    kingAttackers++;
                    kingAttackersWeights += kingAttacksValues[KNIGHT_ATTACK_KING_UNITS];
                    kingAttacks += populationCount(pseudoAttackEnemyKing);
                }
            }
            myKnights &= (myKnights - 1);
        }

        int bishopsScore = 0;

        if (populationCount(myBishops) >= 2) {
            bishopsScore += bishopFeatures[BISHOP_DOUBLE];
        }

        while (myBishops != 0){
            final long bishop = getFirstPiece(myBishops);
            if ((bishop & ignoreThesePieces) == 0) {
                final int bishopIndex = numberOfTrailingZeros(bishop);
                positionScore += POSITION_SCORES[turn][BISHOP][63 - bishopIndex];

                final long pseudoMoves = singleBishopTable(allPieces, bishopIndex, UNIVERSE);
                final long table = pseudoMoves & safeMobSquares;

                squaresIThreatenWithPieces |= pseudoMoves;

                mobilityScore += mobilityScores[BISHOP - 2][populationCount(table)];

                if ((bishop & squaresMyPawnsThreaten) != 0) {
                    bishopsScore += bishopFeatures[BISHOP_PROTECTED_PAWN];
                }

                //outpost, double score if defended by friendly pawn
                if ((bishop & unthreatenableOutpostSpots) != 0) {
                    bishopsScore += bishopFeatures[BISHOP_ON_OUTPOST_BONUS] * (1 + (populationCount(squaresMyPawnsThreaten & bishop)));
                } else {
                    long myThreatsToEmptyOutposts = table & (unthreatenableOutpostSpots & ~friends);
                    if (myThreatsToEmptyOutposts != 0) {
                        bishopsScore += bishopFeatures[BISHOP_REACH_OUTPOST_BONUS] * (1 + (populationCount(squaresMyPawnsThreaten & myThreatsToEmptyOutposts)));
                    }
                }

                if ((bishop & behindPawnSpots) != 0) {
                    bishopsScore += miscFeatures[PIECE_BEHIND_PAWN];
                }

                if (populationCount(pseudoMoves & centreFourSquares) > 1) {
                    bishopsScore += bishopFeatures[BISHOP_PRIME_DIAGONAL];
                }

                if ((bishop & WHITE_COLOURED_SQUARES) != 0) {
                    bishopsScore += (bishopFeatures[BISHOP_COLOUR_PAWNS] * populationCount(wps) *
                            (1 + populationCount(wpscc) / 2 + populationCount(wpsc) / 3));
                } else {
                    bishopsScore += (bishopFeatures[BISHOP_COLOUR_PAWNS] * populationCount(bps) *
                            (1 + populationCount(bpscc) / 2 + populationCount(bpsc) / 3));
                }

                final long pseudoAttackEnemyKing = pseudoMoves & enemyKingSafetyArea;
                if (pseudoAttackEnemyKing != 0) {
                    kingAttackers++;
                    kingAttackersWeights += kingAttacksValues[BISHOP_ATTACK_KING_UNITS];
                    kingAttacks += populationCount(pseudoAttackEnemyKing);
                }
            }
            myBishops &= (myBishops - 1);
        }

        int rooksScore = 0;

        while (myRooks != 0){
            final long rook = getFirstPiece(myRooks);
            if ((rook & ignoreThesePieces) == 0) {
                final int rookIndex = numberOfTrailingZeros(rook);
                positionScore += POSITION_SCORES[turn][ROOK][63 - rookIndex];

                final long pseudoMoves = singleRookTable(allPieces, rookIndex, UNIVERSE);
                final long table = pseudoMoves & safeMobSquares;

                squaresIThreatenWithPieces |= pseudoMoves;

                mobilityScore += mobilityScores[ROOK - 2][populationCount(table)];

                //table does not include pawns defended by pawns
                rooksScore += (populationCount(table & enemyPawns)) * rookFeatures[ROOKS_ATTACK_UNDEFENDED_PAWNS];

                //trapped by king
                if (percentOfStartgame > 50) {
                    final long startingSpotsForKing = whiteToPlay
                            ? (INITIAL_WHITE_KING | INITIAL_WHITE_QUEEN)
                            : (INITIAL_BLACK_KING | INITIAL_BLACK_QUEEN);
                    if (((myKing & startingSpotsForKing) != 0)
                            && populationCount(pseudoMoves) <= 5
                            && ((rook & FINAL_RANKS[1 - turn]) != 0)) {
                        rooksScore += rookFeatures[TRAPPED_ROOK];
                        if ((board.castlingRights & (whiteToPlay ? 0b11 : 0b1100)) == 0) {
                            rooksScore += rookFeatures[TRAPPED_ROOK];
                        }
                    }
                }

                if ((rook & PENULTIMATE_RANKS[turn]) != 0) {
                    rooksScore += rookFeatures[ROOK_ON_SEVENTH_BONUS];
                }

                if ((myRooks & (FILES[rookIndex % 8] ^ rook)) != 0) {
                    rooksScore += rookFeatures[ROOK_BATTERY_SCORE];
                }

                if ((rook & openFiles) != 0) {
                    rooksScore += rookFeatures[ROOK_OPEN_FILE_BONUS];
                } else if ((rook & fileWithoutMyPawns) != 0) {
                    rooksScore += rookFeatures[ROOK_ON_SEMI_OPEN_FILE_BONUS];
                }

                final long pseudoAttackEnemyKing = pseudoMoves & enemyKingSafetyArea;
                if (pseudoAttackEnemyKing != 0) {
                    kingAttackers++;
                    kingAttackersWeights += kingAttacksValues[ROOK_ATTACK_KING_UNITS];
                    kingAttacks += populationCount(pseudoAttackEnemyKing);
                }
            }
            myRooks &= (myRooks - 1);
        }

        int queensScore = 0;

        long defendedByMyQueen = 0;

        while (myQueens != 0){
            final long queen = getFirstPiece(myQueens);
            if ((queen & ignoreThesePieces) == 0) {
                final int queenIndex = numberOfTrailingZeros(queen);
                positionScore += POSITION_SCORES[turn][QUEEN][63 - queenIndex];

                long pseudoMoves = singleQueenTable(allPieces, queenIndex, UNIVERSE);
                //todo pins to queen
//                long pseudoXRayMoves = xrayQueenAttacks(allPieces, blockers, queen);
                        
                final long table = pseudoMoves & safeMobSquares;

                squaresIThreatenWithPieces |= pseudoMoves;

                defendedByMyQueen |= pseudoMoves;

                mobilityScore += mobilityScores[QUEEN - 2][populationCount(table)];

                final long pseudoAttackEnemyKing = pseudoMoves & enemyKingSafetyArea;
                if (pseudoAttackEnemyKing != 0) {
                    kingAttackers++;
                    kingAttackersWeights += kingAttacksValues[QUEEN_ATTACK_KING_LOOKUP_UNITS];
                    kingAttacks += populationCount(pseudoAttackEnemyKing);
                }
            }
            myQueens &= (myQueens - 1);
        }

        myPawns = pieces[turn][PAWN] & ~ignoreThesePieces;

        /*
        regular pawns
         */
        while (myPawns != 0){
            final long pawn = getFirstPiece(myPawns);
            final int pawnIndex = numberOfTrailingZeros(pawn);
            positionScore += POSITION_SCORES[turn][PAWN][63 - pawnIndex];

            myPawns &= myPawns - 1;
        }

        final long pseudoAttackEnemyKing = squaresMyPawnsThreaten & enemyKingSafetyArea;
        if (pseudoAttackEnemyKing != 0) {
            kingAttackers++;
            kingAttackersWeights += 1;
            kingAttacks += populationCount(pseudoAttackEnemyKing);
        }
        
        threatsScore += populationCount(squaresMyPawnsThreaten & enemyBigPieces) * PAWN_THREATENS_BIG_THINGS;


        Assert.assertTrue(percentOfStartgame >= 0 && percentOfStartgame <= 100);

       
        /*
        king
         */
        int kingIndex = numberOfTrailingZeros(myKing);
        long kingPseudoMoves = KING_MOVE_TABLE[kingIndex];

        squaresIThreatenWithPieces |= kingPseudoMoves;

        positionScore += POSITION_SCORES[turn][KING][63 - kingIndex];


        int enemyKingDanger = kingSafetyMisc[STARTING_PENALTY]
                + kingAttackersWeights * kingAttackers 
                + kingSafetyMisc[NUMBER_OF_ATTACKS_FACTOR] * kingAttacks
                - (populationCount(board.pieces[turn][QUEEN]) == 0 ? kingSafetyMisc[MISSING_QUEEN_KING_SAFETY_UNITS] : 0)
                + ((myKingSafetyArea & fileWithoutMyPawns) != 0 ? kingSafetyMisc[KING_NEAR_SEMI_OPEN_FILE_LOOKUP] : 0)
                + (turn == board.turn ? 1 : 0)
                + (populationCount(pinnedPieces) * kingSafetyMisc[PINNED_PIECES_KING_SAFETY_LOOKUP])
                - (populationCount(enemies & enemyKingSafetyArea))
                - (populationCount((enemyPawns & enemyKingSafetyArea) >> 1))
                ;


        enemyKingDanger = Math.max(0, Math.min(enemyKingDanger, KING_SAFETY_ARRAY.length - 1));
        
        finalScore += KING_SAFETY_ARRAY[enemyKingDanger];
        
        turnThreatensSquares[turn] += squaresIThreatenWithPieces;
        
        finalScore += Score.getScore(materialScore, percentOfStartgame);
        finalScore += Score.getScore(positionScore, percentOfStartgame);
        finalScore += mobilityScore;
        finalScore += threatsScore;

        finalScore += Score.getScore(knightsScore, percentOfStartgame);
        finalScore += Score.getScore(bishopsScore, percentOfStartgame);
        finalScore += Score.getScore(rooksScore, percentOfStartgame);
        finalScore += Score.getScore(queensScore, percentOfStartgame);

        if (PRINT_EVAL) {
            scoresForEPO[turn][EvalPrintObject.materialScore] = Score.getScore(materialScore, percentOfStartgame);
            scoresForEPO[turn][EvalPrintObject.positionScore] = Score.getScore(positionScore, percentOfStartgame);
            scoresForEPO[turn][EvalPrintObject.mobilityScore] = mobilityScore;
            scoresForEPO[turn][EvalPrintObject.threatsScore] = threatsScore;
            
            scoresForEPO[turn][EvalPrintObject.knightScore] = Score.getScore(knightsScore, percentOfStartgame);
            scoresForEPO[turn][EvalPrintObject.bishopScore] = Score.getScore(bishopsScore, percentOfStartgame);
            scoresForEPO[turn][EvalPrintObject.rookScore] = Score.getScore(rooksScore, percentOfStartgame);
            scoresForEPO[turn][EvalPrintObject.queenScore] = Score.getScore(queensScore, percentOfStartgame);
            
            scoresForEPO[1 - turn][kingSafetyScore] = -KING_SAFETY_ARRAY[enemyKingDanger];
        }
        
        Assert.assertTrue(finalScore > IN_CHECKMATE_SCORE_MAX_PLY);
        Assert.assertTrue(finalScore < CHECKMATE_ENEMY_SCORE_MAX_PLY);

        return finalScore;
    }

    // thanks H.G.Muller
    static int getPercentageOfStartGame(Chessboard board) {
        int answer = 0;

        for (int t = WHITE; t <= BLACK; t++) {
            answer += populationCount(board.pieces[t][QUEEN]) * 6;
            answer += populationCount(board.pieces[t][ROOK]) * 3;
            answer += populationCount(board.pieces[t][BISHOP]);
            answer += populationCount(board.pieces[t][KNIGHT]);
        }

        return Math.min((answer * 100) / 32, 100);
    }
}
