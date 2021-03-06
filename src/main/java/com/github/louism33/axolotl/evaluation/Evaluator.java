package com.github.louism33.axolotl.evaluation;

import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MaterialHashUtil;
import org.junit.Assert;

import java.util.Arrays;

import static com.github.louism33.axolotl.evaluation.EndgameKBBK.evaluateKBBK;
import static com.github.louism33.axolotl.evaluation.EndgameKBNK.evaluateKBNK;
import static com.github.louism33.axolotl.evaluation.EndgameKPK.evaluateKPK;
import static com.github.louism33.axolotl.evaluation.EndgameKQK.evaluateKQK;
import static com.github.louism33.axolotl.evaluation.EndgameKRK.evaluateKRK;
import static com.github.louism33.axolotl.evaluation.EndgameKRRK.evaluateKRRK;
import static com.github.louism33.axolotl.evaluation.EvalPrintObject.*;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.K;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.Q;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.evaluation.EvaluatorPositionConstant.POSITION_SCORES;
import static com.github.louism33.axolotl.evaluation.EvaluatorPositionConstant.mobilityScores;
import static com.github.louism33.axolotl.evaluation.Init.kingSafetyArea;
import static com.github.louism33.axolotl.evaluation.PassedPawns.evalPassedPawnsByTurn;
import static com.github.louism33.axolotl.evaluation.PawnTranspositionTable.*;
import static com.github.louism33.axolotl.search.EngineSpecifications.*;
import static com.github.louism33.chesscore.BitOperations.fileForward;
import static com.github.louism33.chesscore.BitOperations.*;
import static com.github.louism33.chesscore.BoardConstants.*;
import static com.github.louism33.chesscore.MaterialHashUtil.KPK;
import static com.github.louism33.chesscore.MaterialHashUtil.*;
import static com.github.louism33.chesscore.PieceMove.*;
import static java.lang.Long.numberOfTrailingZeros;

//@SuppressWarnings("ALL")
public final class Evaluator {

    private static boolean readyEvaluator = false;
    private static long[][] turnThreatensSquaresBackend = new long[NUMBER_OF_THREADS][2];
    static final int[][] scoresForEPO = new int[2][32];

    public static void initEvaluator(boolean force) {
        if (force || !readyEvaluator) {
            turnThreatensSquaresBackend = new long[NUMBER_OF_THREADS][2];
        }
        readyEvaluator = true;
    }

    public static void printEval(Chessboard board) {
        System.out.println(stringEval(board));
    }

    public static EvalPrintObject stringEval(Chessboard board) {
        board.getCheckers();
        PRINT_EVAL = true;
        eval(board, 0);
        EvalPrintObject epo = new EvalPrintObject(scoresForEPO);
        EPOturn = board.turn;
        PRINT_EVAL = false;
        return epo;
    }

    /**
     * todo:
     * trapped pieces
     */
    /**
     * Don't call if in check
     */
    public static final int eval(final Chessboard board, int whichThread) {

        if (board.isDrawByInsufficientMaterial() || board.isDrawByFiftyMoveRule()
                || board.isDrawByRepetition(1)) {
            return 0;
        }

        if (MASTER_DEBUG) {
            Assert.assertTrue(board.materialHash != 0);
            Assert.assertEquals(MaterialHashUtil.makeMaterialHash(board), board.materialHash);
            Assert.assertEquals(MaterialHashUtil.typeOfEndgame(board), board.typeOfGameIAmIn);
        }

        switch (board.typeOfGameIAmIn) {
            case CERTAIN_DRAW:
                Assert.assertTrue(isBasicallyDrawn(board));
                return 0;

            case KQQK:
            case KQRK:
            case KRRK:
                Assert.assertTrue(!isBasicallyDrawn(board));
                return evaluateKRRK(board);
            case KQK:
                Assert.assertTrue(!isBasicallyDrawn(board));

                return evaluateKQK(board);

            case KRK:
                Assert.assertTrue(!isBasicallyDrawn(board));
                return evaluateKRK(board);

            case KBBK:
                Assert.assertTrue(!isBasicallyDrawn(board));
                Assert.assertTrue(populationCount(board.pieces[WHITE][BISHOP]) >= 2
                        || populationCount(board.pieces[BLACK][BISHOP]) >= 2);
                return evaluateKBBK(board);

            case KPK:
                Assert.assertTrue(!isBasicallyDrawn(board));
                return evaluateKPK(board);

            case KBNK:
                Assert.assertTrue(!isBasicallyDrawn(board));

                final boolean condition = 2 >= populationCount(board.pieces[WHITE][BISHOP] | board.pieces[WHITE][KNIGHT])
                        || 2 >= populationCount(board.pieces[BLACK][BISHOP] | board.pieces[BLACK][KNIGHT]);
                if (!condition) {
                    System.out.println(board);
                }
                Assert.assertTrue(condition);
                return evaluateKBNK(board);


            case UNKNOWN:
            default:
                switch (typeOfEndgame(board)) {
                    case CERTAIN_DRAW:
                        Assert.assertTrue(isBasicallyDrawn(board));
                        board.typeOfGameIAmIn = CERTAIN_DRAW;
                        return 0;

                    case KQQK:
                        Assert.assertTrue(!isBasicallyDrawn(board));
                        board.typeOfGameIAmIn = KQQK;
                        return evaluateKRRK(board);
                    case KQRK:
                        Assert.assertTrue(!isBasicallyDrawn(board));
                        board.typeOfGameIAmIn = KQRK;
                        return evaluateKRRK(board);
                    case KRRK:
                        Assert.assertTrue(!isBasicallyDrawn(board));
                        board.typeOfGameIAmIn = KRRK;
                        return evaluateKRRK(board);
                    case KQK:
                        Assert.assertTrue(!isBasicallyDrawn(board));
                        board.typeOfGameIAmIn = KQK;
                        return evaluateKQK(board);
                    case KRK:
                        Assert.assertTrue(!isBasicallyDrawn(board));
                        board.typeOfGameIAmIn = KRK;
                        return evaluateKRK(board);
                    case KBBK:
                        Assert.assertTrue(!isBasicallyDrawn(board));
                        board.typeOfGameIAmIn = KBBK;
                        return evaluateKBBK(board);
                    case KPK:
                        board.typeOfGameIAmIn = KPK;
                        return evaluateKPK(board);
                    case KBNK:
                        board.typeOfGameIAmIn = KBNK;
                        Assert.assertTrue(2 >= populationCount(board.pieces[WHITE][BISHOP] | board.pieces[WHITE][KNIGHT])
                                || 2 >= populationCount(board.pieces[BLACK][BISHOP] | board.pieces[BLACK][KNIGHT]));
                        return evaluateKBNK(board);

                    case UNKNOWN:
                    default:
                        return evalGeneric(board, whichThread);
                }

        }
    }

    private static int evalGeneric(final Chessboard board, int whichThread) {

        int turn = board.turn;

        setupEvalConst(false);
        EvaluatorPositionConstant.setupEvalPosConst(false);

        int percentOfEndgame;
        int percentOfStartgame;

        long[] turnThreatensSquares = turnThreatensSquaresBackend[whichThread];

        Assert.assertTrue(!board.inCheckRecorder);
        Assert.assertTrue(board.getCheckers() == 0);

        if (PRINT_EVAL) {
            Arrays.fill(scoresForEPO[WHITE], 0);
            Arrays.fill(scoresForEPO[BLACK], 0);
        }

        long myKingSafetyArea = kingSafetyArea(board, turn);
        long enemyKingSafetyArea = kingSafetyArea(board, 1 - turn);

        percentOfStartgame = getPercentageOfStartGame(board);
        percentOfEndgame = 100 - percentOfStartgame;

        int score = 0;

        long[] pawnData = getPawnData(board, board.zobristPawnHash, percentOfStartgame, whichThread);

        if (MASTER_DEBUG && NUMBER_OF_THREADS == 1) {
            // this fails if pawn data is shared between threads
            Assert.assertArrayEquals(pawnData, getPawnData(board, board.zobristPawnHash, percentOfStartgame, whichThread));
            if (!Arrays.equals(pawnData, noPawnsData)) {
                Assert.assertArrayEquals(pawnData, PawnEval.calculatePawnData(board, percentOfStartgame, 0));
            }
        }


        score += Score.getScore((int) pawnData[SCORE], percentOfStartgame);

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

    private static int evalTurn(Chessboard board, int turn, long[] pawnData,
                                long[] turnThreatensSquares, int percentOfStartgame,
                                long myKingSafetyArea, long enemyKingSafetyArea) {

        final long[][] pieces = board.pieces;

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

        final boolean itIsMyTurn = turn == board.turn;

        final long allPieces = friends | enemies;

        final long myPinnedPieces = board.pinnedPieces[turn];
        final long enemyPinnedPieces = board.pinnedPieces[1 - turn];
        final long enemyPiecesThatPinMyPieces = board.pinningPieces[turn];
        final long myPiecesThatPinEnemies = board.pinningPieces[1 - turn];

        final boolean turnToPlayInCheck = board.inCheckRecorder;

        Assert.assertTrue(!turnToPlayInCheck);
        Assert.assertTrue((myPinnedPieces & enemyPinnedPieces) == 0);
        Assert.assertTrue((myPinnedPieces & enemies) == 0);
        Assert.assertTrue((enemyPinnedPieces & friends) == 0);
        Assert.assertTrue((friends & enemies) == 0);
        Assert.assertTrue(myPinnedPieces == 0 || (myPinnedPieces & friends) != 0);
        Assert.assertTrue(enemyPinnedPieces == 0 || (enemyPinnedPieces & enemies) != 0);
        Assert.assertEquals(myPinnedPieces == 0, enemyPiecesThatPinMyPieces == 0);
        Assert.assertEquals(myPinnedPieces != 0, enemyPiecesThatPinMyPieces != 0);
        Assert.assertEquals(myPiecesThatPinEnemies != 0, enemyPinnedPieces != 0);
        Assert.assertEquals(myPiecesThatPinEnemies == 0, enemyPinnedPieces == 0);
        

      
        //todo disco possibility and check to queen

        //todo, use king vision to get enemies that could move to make a disco check (but not pawns?)
        //this should be in chessboard

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
//            long pins = myPinnedPieces;
            long pins = myPinnedPieces;
            while (pins != 0) {
                final int i = numberOfTrailingZeros(pins);
                final int pinnedPiece = board.pieceSquareTable[i];
                final int colourBlindPiece = pinnedPiece < 7 ? pinnedPiece : pinnedPiece - 6;
                threatsScore += pinnedPiecesScores[colourBlindPiece];
                pins &= pins - 1;
            }
        }

        final long squaresMyPawnsThreaten = pawnData[CAPTURES + turn];
        final long squaresEnemyPawnsThreaten = pawnData[CAPTURES + 1 - turn];

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

        while (myKnights != 0) {
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
                        knightsScore += knightFeatures[KNIGHT_REACH_OUTPOST_BONUS] * (1 + (populationCount(squaresMyPawnsThreaten & myThreatsToEmptyOutposts)));
                    }
                }

                if ((knight & behindPawnSpots) != 0) {
                    knightsScore += miscFeatures[PIECE_BEHIND_PAWN];
                }

                final long pseudoAttackEnemyKingSmall = pseudoMoves & enemyKingSmallArea;
                if (pseudoAttackEnemyKingSmall != 0) {
                    kingAttacks += populationCount(pseudoAttackEnemyKingSmall);
                }

                final long pseudoAttackEnemyKing = pseudoMoves & enemyKingSafetyArea;
                if (pseudoAttackEnemyKing != 0) {
                    kingAttackers++;
                    kingAttackersWeights += kingAttacksValues[KNIGHT_ATTACK_KING_UNITS];
                }
            }
            myKnights &= (myKnights - 1);
        }

        int bishopsScore = 0;

        if (populationCount(myBishops) >= 2) {
            bishopsScore += bishopFeatures[BISHOP_DOUBLE];
        }

        while (myBishops != 0) {
            final long bishop = getFirstPiece(myBishops);
            if ((bishop & ignoreThesePieces) == 0) {
                final int bishopIndex = numberOfTrailingZeros(bishop);
                positionScore += POSITION_SCORES[turn][BISHOP][63 - bishopIndex];

                // todo, as with queen, also consider xrays
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

                final long pseudoAttackEnemyKingSmall = pseudoMoves & enemyKingSmallArea;
                if (pseudoAttackEnemyKingSmall != 0) {
                    kingAttacks += populationCount(pseudoAttackEnemyKingSmall);
                }


                final long pseudoAttackEnemyKing = pseudoMoves & enemyKingSafetyArea;
                if (pseudoAttackEnemyKing != 0) {
                    kingAttackers++;
                    kingAttackersWeights += kingAttacksValues[BISHOP_ATTACK_KING_UNITS];
                }
            }
            myBishops &= (myBishops - 1);
        }

        int rooksScore = 0;

        while (myRooks != 0) {
            final long rook = getFirstPiece(myRooks);
            if ((rook & ignoreThesePieces) == 0) {
                final int rookIndex = numberOfTrailingZeros(rook);
                positionScore += POSITION_SCORES[turn][ROOK][63 - rookIndex];
                // todo, as with queen, also consider xrays 
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

                final long pseudoAttackEnemyKingSmall = pseudoMoves & enemyKingSmallArea;
                if (pseudoAttackEnemyKingSmall != 0) {
                    kingAttacks += populationCount(pseudoAttackEnemyKingSmall);
                }

                final long pseudoAttackEnemyKing = pseudoMoves & enemyKingSafetyArea;
                if (pseudoAttackEnemyKing != 0) {
                    kingAttackers++;
                    kingAttackersWeights += kingAttacksValues[ROOK_ATTACK_KING_UNITS];
                }
            }
            myRooks &= (myRooks - 1);
        }

        int queensScore = 0;

        long defendedByMyQueen = 0;

        while (myQueens != 0) {
            final long queen = getFirstPiece(myQueens);
            if ((queen & ignoreThesePieces) == 0) {
                final int queenIndex = numberOfTrailingZeros(queen);
                positionScore += POSITION_SCORES[turn][QUEEN][63 - queenIndex];

                final long rookMoves = singleRookTable(allPieces, queenIndex, UNIVERSE);
                final long bishopMoves = singleBishopTable(allPieces, queenIndex, UNIVERSE);

                Assert.assertEquals(rookMoves | bishopMoves, singleQueenTable(allPieces, queenIndex, UNIVERSE));

                final long pseudoMoves = rookMoves | bishopMoves;

                // todo   disco attacks to queen
                final long rookXRay = rookMoves ^ singleRookTable(allPieces ^ (allPieces & rookMoves), queenIndex, UNIVERSE);
                final long bishopXRay = bishopMoves ^ singleBishopTable(allPieces ^ (allPieces & bishopMoves), queenIndex, UNIVERSE);
                final long pseudoXRayMoves = rookXRay | bishopXRay;

                final long crossPinnersToMyQueen = (rookMoves ^
                        singleRookTable(allPieces ^ (myPawns | rookMoves), queenIndex, UNIVERSE))
                        & (enemyRooks | enemyQueens);

                Assert.assertTrue((crossPinnersToMyQueen & friends) == 0);
                Assert.assertTrue(crossPinnersToMyQueen == 0 || (crossPinnersToMyQueen & enemies) != 0);

                final long diagonalPinnersToMyQueen = (bishopMoves ^
                        (singleBishopTable(allPieces ^ (myPawns | bishopMoves), queenIndex, UNIVERSE)))
                        & (enemyBishops | enemyQueens);

                Assert.assertTrue((diagonalPinnersToMyQueen & friends) == 0);
                Assert.assertTrue(diagonalPinnersToMyQueen == 0 || (diagonalPinnersToMyQueen & enemies) != 0);

                queensScore += populationCount(diagonalPinnersToMyQueen & (enemyBishops))
                        * queenFeatures[FRIENDLY_PIECE_PINNED_TO_QUEEN_BY_BISHOP];
                queensScore += populationCount(crossPinnersToMyQueen & (enemyRooks))
                        * queenFeatures[FRIENDLY_PIECE_PINNED_TO_QUEEN_BY_ROOK];
                queensScore += populationCount((crossPinnersToMyQueen | diagonalPinnersToMyQueen) & (enemyQueens))
                        * queenFeatures[FRIENDLY_PIECE_PINNED_TO_QUEEN_BY_QUEEN];

                Assert.assertEquals(xrayQueenAttacks(allPieces, allPieces, queen), pseudoXRayMoves);

                final long table = pseudoMoves & safeMobSquares;

                squaresIThreatenWithPieces |= pseudoMoves;

                defendedByMyQueen |= pseudoMoves;

                mobilityScore += mobilityScores[QUEEN - 2][populationCount(table)];

                final long pseudoAttackEnemyKingSmall = pseudoMoves & enemyKingSmallArea;
                if (pseudoAttackEnemyKingSmall != 0) {
                    kingAttacks += populationCount(pseudoAttackEnemyKingSmall);
                }

                final long pseudoAttackEnemyKingX = pseudoXRayMoves & enemyKingSafetyArea;
                final long pseudoAttackEnemyKing = pseudoMoves & enemyKingSafetyArea;

                if (pseudoAttackEnemyKing != 0) {
                    kingAttackers++;
                    kingAttackersWeights += kingAttacksValues[QUEEN_ATTACK_KING_LOOKUP_UNITS];
                } else if (pseudoAttackEnemyKingX != 0) {
                    Assert.assertTrue(pseudoAttackEnemyKing == 0);
                    kingAttackers++;
                    kingAttackersWeights += kingAttacksValues[QUEEN_ATTACK_KING_X_LOOKUP_UNITS];
                }

            }
            myQueens &= (myQueens - 1);
        }

        myPawns = pieces[turn][PAWN] & ~ignoreThesePieces;

//        regular pawns
        while (myPawns != 0) {
            final long pawn = getFirstPiece(myPawns);
            final int pawnIndex = numberOfTrailingZeros(pawn);
            positionScore += POSITION_SCORES[turn][PAWN][63 - pawnIndex];

            myPawns &= myPawns - 1;
        }

        final long pseudoAttackEnemyKingSmall = squaresMyPawnsThreaten & enemyKingSmallArea;
        if (pseudoAttackEnemyKingSmall != 0) {
            kingAttacks += populationCount(pseudoAttackEnemyKingSmall);
        }


        final long pseudoAttackEnemyKing = squaresMyPawnsThreaten & enemyKingSafetyArea;
        if (pseudoAttackEnemyKing != 0) {
            kingAttackers++;
            kingAttackersWeights += 1;
        }

        threatsScore += populationCount(squaresMyPawnsThreaten & enemyBigPieces) * PAWN_THREATENS_BIG_THINGS;

        Assert.assertTrue(percentOfStartgame >= 0 && percentOfStartgame <= 100);


//        king
        int kingIndex = numberOfTrailingZeros(myKing);
        long kingPseudoMoves = KING_MOVE_TABLE[kingIndex];

        squaresIThreatenWithPieces |= kingPseudoMoves;

        positionScore += POSITION_SCORES[turn][KING][63 - kingIndex];


        int enemyKingDanger = kingSafetyMisc[STARTING_PENALTY]
                + kingAttackersWeights * kingAttackers
                + kingSafetyMisc[NUMBER_OF_ATTACKS_FACTOR] * kingAttacks
                + ((enemyKingSafetyArea & fileWithoutEnemyPawns) != 0 ? kingSafetyMisc[KING_NEAR_SEMI_OPEN_FILE_LOOKUP] : 0)
                + (turn == board.turn ? 1 : 0)
//                + (populationCount(enemyPinnedPieces) * kingSafetyMisc[PINNED_PIECES_KING_SAFETY_LOOKUP])
                + (turn == board.turn ? (populationCount(myPinnedPieces) * kingSafetyMisc[PINNED_PIECES_KING_SAFETY_LOOKUP]) : 0)

                - (populationCount(board.pieces[turn][QUEEN]) == 0 ? kingSafetyMisc[MISSING_QUEEN_KING_SAFETY_UNITS] : 0)
                - (populationCount(enemies & enemyKingSafetyArea))
                - (populationCount((enemyPawns & enemyKingSafetyArea))); //pawns counted twice


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

