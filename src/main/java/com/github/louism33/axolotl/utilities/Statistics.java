package com.github.louism33.axolotl.utilities;

import com.github.louism33.axolotl.search.EngineSpecifications;

import java.util.Arrays;

public class Statistics {
    
    private final int[] whichMoveWasTheBest = new int[60];
    public static final int[] whichMoveWasTheBestQuiescence = new int[60];

    public static long numberOfMovesMade = 0;
    public static long numberOfQuiescentMovesMade = 0;
    public static long numberOfEvals = 0;
    public static long numberOfQuiescentEvals = 0;
    public static int numberOfCheckmates = 0;
    public static int numberOfStalemates = 0;
    private static int numberOfFailHighs = 0;
    public static int numberOfNullMoveHits = 0;
    public static int numberOfNullMoveMisses = 0;
    public static int numberOfPVSHits = 0;
    public static int numberOfPVSMisses = 0;
    public static int numberOfLateMovePrunings = 0;
    public static int numberOfLateMoveReductions = 0;
    public static int numberOfLateMoveReductionsHits = 0;
    public static int numberOfLateMoveReductionsMisses = 0;
    public static int numberOfCheckExtensions = 0;
    public static int numberOfPassedPawnExtensions = 0;
    public static int numberOfSuccessfulFutilities = 0;
    public static int numberOfFailedFutilities = 0;
    public static int numberOfSuccessfulQuiescenceFutilities = 0;
    public static int numberOfFailedQuiescenceFutilities = 0;

    public static int numberOfSuccessfulSEEs = 0;
    public static int numberOfSuccessfulQuiescentSEEs = 0;
    
    public static int numberOfSuccessfulAlphaRazors = 0;
    public static int numberOfFailedAlphaRazors = 0;
    public static int numberOfSuccessfulBetaRazors = 0;
    public static int numberOfSuccessfulAspirations = 0;
    public static int numberOfFailedAspirations = 0;
    
    public static int numberOfIIDs = 0;
    public static int numberOfSuccessfulIIDs = 0;
    public static int numberOfFailedIIDs = 0;

    private static int numberOfVictoriousKillersOne = 0;
    private static int numberOfVictoriousKillersTwo = 0;
    private static int numberOfVeteranVictoriousKillersOne = 0;
    private static int numberOfVeteranVictoriousKillersTwo = 0;

    private static int numberOfVictoriousMaters = 0;

    public static int numberOfSearchesWithHash = 0;
    public static int numberOfSearchesWithoutHash = 0;

    public static int numberOfExacts = 0;
    public static int numberOfLowerBounds = 0;
    public static int numberOfUpperBounds = 0;
    public static int numberOfHashBetaCutoffs = 0;

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
