package com.github.louism33.axolotl.utilities;

import com.github.louism33.axolotl.search.EngineSpecifications;

import java.util.Arrays;

public class Statistics {
    
    private final int[] whichMoveWasTheBest = new int[60];
    private static final int[] whichMoveWasTheBestQuiescence = new int[60];

    private static final long numberOfMovesMade = 0;
    private static final long numberOfQuiescentMovesMade = 0;
    private static final long numberOfEvals = 0;
    private static final long numberOfQuiescentEvals = 0;
    private static final int numberOfCheckmates = 0;
    private static final int numberOfStalemates = 0;
    private static int numberOfFailHighs = 0;
    private static final int numberOfNullMoveHits = 0;
    private static final int numberOfNullMoveMisses = 0;
    private static final int numberOfPVSHits = 0;
    private static final int numberOfPVSMisses = 0;
    private static final int numberOfLateMovePrunings = 0;
    private static final int numberOfLateMoveReductions = 0;
    private static final int numberOfLateMoveReductionsHits = 0;
    private static final int numberOfLateMoveReductionsMisses = 0;
    public static int numberOfCheckExtensions = 0;
    public static int numberOfPassedPawnExtensions = 0;
    private static final int numberOfSuccessfulFutilities = 0;
    private static final int numberOfFailedFutilities = 0;
    private static final int numberOfSuccessfulQuiescenceFutilities = 0;
    private static final int numberOfFailedQuiescenceFutilities = 0;

    private static final int numberOfSuccessfulSEEs = 0;
    private static final int numberOfSuccessfulQuiescentSEEs = 0;
    
    private static final int numberOfSuccessfulAlphaRazors = 0;
    private static final int numberOfFailedAlphaRazors = 0;
    private static final int numberOfSuccessfulBetaRazors = 0;
    private static final int numberOfSuccessfulAspirations = 0;
    private static final int numberOfFailedAspirations = 0;
    
    private static final int numberOfIIDs = 0;
    private static final int numberOfSuccessfulIIDs = 0;
    private static final int numberOfFailedIIDs = 0;

    private static final int numberOfVictoriousKillersOne = 0;
    private static final int numberOfVictoriousKillersTwo = 0;
    private static final int numberOfVeteranVictoriousKillersOne = 0;
    private static final int numberOfVeteranVictoriousKillersTwo = 0;

    private static final int numberOfVictoriousMaters = 0;

    private static final int numberOfSearchesWithHash = 0;
    private static final int numberOfSearchesWithoutHash = 0;

    private static final int numberOfExacts = 0;
    private static final int numberOfLowerBounds = 0;
    private static final int numberOfUpperBounds = 0;
    private static final int numberOfHashBetaCutoffs = 0;

    public void infoLog(long endTime, long startTime, int move){
        long time = endTime - startTime;
        
        if (time > 1000) {
//            System.out.println("Best move: "+MoveParser.toString(move)+", nps: " +
//                    ((1000 * (this.numberOfMovesMade + this.numberOfQuiescentMovesMade)) / time));  
            
//            System.out.println("Nps: " +
//                    ((1000 * (this.numberOfMovesMade + this.numberOfQuiescentMovesMade)) / time));
        } 
        else {
//            System.out.println("Best move: " + MoveParser.toString(move));
        }

       
    }
    
    public void printStatistics(){
        System.out.println();
        System.out.println("------");

        System.out.println();
        if (EngineSpecifications.ALLOW_PRINCIPLE_VARIATION_SEARCH) {
            System.out.println("number of PVS hits:             " + numberOfPVSHits);
            System.out.println("number of PVS misses:           " + numberOfPVSMisses);
            System.out.println();
        }
        if (EngineSpecifications.ALLOW_ASPIRATION_WINDOWS) {
            System.out.println("Number of good aspirations:     " + numberOfSuccessfulAspirations);
            System.out.println("Number of bad aspirations:      " + numberOfFailedAspirations);
            System.out.println();
        }

        System.out.println("Number of searches with hashmove:   " + numberOfSearchesWithHash);
        System.out.println("Number of searches w/out hashmove:  " + numberOfSearchesWithoutHash);
        System.out.println();
        System.out.println("Number of Exacts:                   " + numberOfExacts);
        System.out.println("Number of UBs:                      " + numberOfUpperBounds);
        System.out.println("Number of LBs:                      " + numberOfLowerBounds);
        System.out.println("numberOfHashBetaCutoffs:            " + numberOfHashBetaCutoffs);
        System.out.println();

        if (EngineSpecifications.ALLOW_KILLERS){
            System.out.println("numberOfVictoriousKillersOne:   " + numberOfVictoriousKillersOne);
            System.out.println("numberOfVictoriousKillersTwo:   " + numberOfVictoriousKillersTwo);
            System.out.println("number of old killer ones:      " + numberOfVeteranVictoriousKillersOne);
            System.out.println("number of old killer twos:      " + numberOfVeteranVictoriousKillersTwo);
            System.out.println();
        }

        if (EngineSpecifications.ALLOW_MATE_KILLERS){
            System.out.println("numberOfVictoriousMaters:       " + numberOfVictoriousMaters);
            System.out.println();
        }

        if (EngineSpecifications.ALLOW_EXTENSIONS) {
            System.out.println("number of Check Extensions      " + numberOfCheckExtensions);
            System.out.println();
        }
        if (EngineSpecifications.ALLOW_LATE_MOVE_REDUCTIONS) {
            System.out.println("number of LMRs:                 " + numberOfLateMoveReductions);
            System.out.println("number of LMR hits:             " + numberOfLateMoveReductionsHits);
            System.out.println("number of LMR misses:           " + numberOfLateMoveReductionsMisses);
            System.out.println();
        }
        if (EngineSpecifications.ALLOW_LATE_MOVE_PRUNING) {
            System.out.println("number of late move prunings:   " + numberOfLateMovePrunings);
            System.out.println();
        }
        if (EngineSpecifications.ALLOW_NULL_MOVE_PRUNING){
            System.out.println("number of null move hits:       " + numberOfNullMoveHits);
            System.out.println("number of null move misses:     " + numberOfNullMoveMisses);
            System.out.println();
        }
        if (EngineSpecifications.ALLOW_ALPHA_RAZORING) {
            System.out.println("Number of good alpha razors:    " + numberOfSuccessfulAlphaRazors);
            System.out.println("Number of failed alpha razors:  " + numberOfFailedAlphaRazors);
            System.out.println();
        }

        if (EngineSpecifications.ALLOW_BETA_RAZORING) {
            System.out.println("Number of good beta razors:     " + numberOfSuccessfulBetaRazors);
            System.out.println();
        }

        if (EngineSpecifications.ALLOW_FUTILITY_PRUNING) {
            System.out.println("Number of good futilities:      " + numberOfSuccessfulFutilities);
            System.out.println("Number of bad futilities:       " + numberOfFailedFutilities);
            System.out.println();
        }   
        
        if (EngineSpecifications.ALLOW_SEE_PRUNING) {
            System.out.println("Number of successful SEEs:      " + numberOfSuccessfulSEEs);
            System.out.println();
        }            
        
        if (EngineSpecifications.ALLOW_QUIESCENCE_SEE_PRUNING) {
            System.out.println("Number of successful Q SEEs:    " + numberOfSuccessfulQuiescentSEEs);
            System.out.println();
        }        
        
        if (EngineSpecifications.ALLOW_QUIESCENCE_FUTILITY_PRUNING) {
            System.out.println("Number of good Q futilities:    " + numberOfSuccessfulQuiescenceFutilities);
            System.out.println("Number of bad Q futilities:     " + numberOfFailedQuiescenceFutilities);
            System.out.println();
        }

        if (EngineSpecifications.ALLOW_INTERNAL_ITERATIVE_DEEPENING) {
            System.out.println("Number of IIDS:                 " + numberOfIIDs);
            System.out.println("Number of successful IIDS:      " + numberOfSuccessfulIIDs);
            System.out.println("Number of failed IIDS:          " + numberOfFailedIIDs);
            System.out.println();
        }



        System.out.println("------");
        System.out.println("number of moves made:               " + numberOfMovesMade);
        System.out.println("number of Q moves made:             " + numberOfQuiescentMovesMade);
        System.out.println("total moves:                        " + (numberOfQuiescentMovesMade + numberOfMovesMade));
        System.out.println("number of evals:                    " + numberOfEvals);
        System.out.println("number of Q evals:                  " + numberOfQuiescentEvals);

        System.out.println("number of checkmates found:         " + numberOfCheckmates);
        System.out.println("number of stalemates found:         " + numberOfStalemates);


        System.out.println();
        System.out.println("Winning Quiescence moves by order:  ");
        System.out.println(Arrays.toString(whichMoveWasTheBestQuiescence));

        System.out.println();
        System.out.println("Winning moves by order:             ");
        System.out.println(Arrays.toString(whichMoveWasTheBest));
        System.out.println();
        System.out.println("By percent:                         ");
        System.out.println(Arrays.toString(toPercent(whichMoveWasTheBest)));
        System.out.println();
        System.out.println("By collective percent:              ");
        System.out.println(Arrays.toString(toCollectivePercent(toPercent(whichMoveWasTheBest))));
        System.out.println();
    }


    private static float[] toPercent (int[] nums){
        int total = 0;
        for (int i1 = 0; i1 < nums.length; i1++) {
            Integer i = nums[i1];
            total += i;
        }

        float[] percents = new float[nums.length];
        if (total <= 0){
            return percents;
        }
        for (int i = 0; i < nums.length; i++) {
            float num = 100 * ((float) nums[i]) / ((float) total);
            percents[i] = num < 0.01 ? 0 : num;
        }
        return percents;
    }

    private static float[] toCollectivePercent (float[] percents){
        float[] collectivePercents = new float[percents.length];
        float runningTotal = 0;
        for (int i = 0; i < percents.length; i++) {
            runningTotal += (percents[i] > 0.01 ? percents[i] : 0);
            runningTotal = runningTotal > 99.9 ? 100 : runningTotal;
            collectivePercents[i] = runningTotal;
        }
        return collectivePercents;
    }


    public void statisticsFailHigh(int ply, int numberOfMovesSearched, int move) {
        if (numberOfMovesSearched - 1 < whichMoveWasTheBest.length) {
            whichMoveWasTheBest[numberOfMovesSearched - 1]++;
        }

//        if (move == (mateKillers[ply])){
//            Statistics.numberOfVictoriousMaters++;
//        }
//
//        if (move == (killerMoves[ply][0])){
//            Statistics.numberOfVictoriousKillersOne++;
//        }
//        if (move == (killerMoves[ply][1])){
//            Statistics.numberOfVictoriousKillersTwo++;
//        }
//
//        if (ply > 1) {
//            if (move == (killerMoves[ply - 2][0])) {
//                Statistics.numberOfVeteranVictoriousKillersOne++;
//            }
//            if (move == (killerMoves[ply - 2][1])) {
//                Statistics.numberOfVeteranVictoriousKillersTwo++;
//            }
//        }
        Statistics.numberOfFailHighs++;
    }

}
