package javacode.chessengine;

import java.util.Arrays;

import static javacode.chessengine.Engine.*;

public class Statistics {
    
    public void resetStatistics(){
        statistics = new Statistics();
        System.out.println(numberOfMovesMade);
    }
    
    public Statistics (){
        
    }

    public int[] whichMoveWasTheBest = new int[60];
    public int[] whichMoveWasTheBestQuiescence = new int[60];

    public long numberOfMovesMade = 0;
    public long numberOfQuiescentMovesMade = 0;
    public long numberOfEvals = 0;
    public long numberOfQuiescentEvals = 0;
    public int numberOfCheckmates = 0;
    public int numberOfStalemates = 0;
    public int numberOfFailHighs = 0;
    public int numberOfNullMoveHits = 0;
    public int numberOfNullMoveMisses = 0;
    public int numberOfPVSHits = 0;
    public int numberOfPVSMisses = 0;
    public int numberOfLateMovePrunings = 0;
    public int numberOfLateMoveReductions = 0;
    public int numberOfLateMoveReductionsHits = 0;
    public int numberOfLateMoveReductionsMisses = 0;
    public int numberOfCheckExtensions = 0;
    public int numberOfPassedPawnExtensions = 0;
    public int numberOfSuccessfulFutilities = 0;
    public int numberOfFailedFutilities = 0;
    public int numberOfSuccessfulQuiescenceFutilities = 0;
    public int numberOfFailedQuiescenceFutilities = 0;
    public int numberOfSuccessfulAlphaRazors = 0;
    public int numberOfFailedAlphaRazors = 0;
    public int numberOfSuccessfulBetaRazors = 0;
    public int numberOfSuccessfulAspirations = 0;
    public int numberOfFailedAspirations = 0;
    public int numberOfIIDs = 0;

    public int numberOfVictoriousKillersOne = 0;
    public int numberOfVictoriousKillersTwo = 0;
    public int numberOfVeteranVictoriousKillersOne = 0;
    public int numberOfVeteranVictoriousKillersTwo = 0;

    public int numberOfVictoriousMaters = 0;

    public int numberOfSearchesWithHash = 0;
    public int numberOfSearchesWithoutHash = 0;

    public int numberOfExacts = 0;
    public int numberOfLowerBounds = 0;
    public int numberOfUpperBounds = 0;
    public int numberOfHashBetaCutoffs = 0;

    void printStatistics(){
        System.out.println();
        System.out.println("------");

        System.out.println("Modifications:" +
                "\nALLOW_PRINCIPLE_VARIATION_SEARCH = "     + ALLOW_PRINCIPLE_VARIATION_SEARCH +
                "\nALLOW_MATE_DISTANCE_PRUNING = "          + ALLOW_MATE_DISTANCE_PRUNING +
                "\nALLOW_EXTENSIONS = "                     + ALLOW_EXTENSIONS +
                "\nALLOW_LATE_MOVE_REDUCTIONS = "           + ALLOW_LATE_MOVE_REDUCTIONS +
                "\nALLOW_LATE_MOVE_PRUNING = "              + ALLOW_LATE_MOVE_PRUNING +
                "\nALLOW_NULL_MOVE_PRUNING = "              + ALLOW_NULL_MOVE_PRUNING +
                "\nALLOW_ALPHA_RAZORING = "                 + ALLOW_ALPHA_RAZORING +
                "\nALLOW_BETA_RAZORING = "                  + ALLOW_BETA_RAZORING +
                "\nALLOW_FUTILITY_PRUNING = "               + ALLOW_FUTILITY_PRUNING +
                "\nALLOW_QUIESCENCE_FUTILITY_PRUNING = "    + ALLOW_QUIESCENCE_FUTILITY_PRUNING +
                "\nALLOW_KILLERS = "                        + ALLOW_KILLERS +
                "\nALLOW_HISTORY_MOVES = "                  + ALLOW_HISTORY_MOVES +
                "\nALLOW_ASPIRATION_WINDOWS = "             + ALLOW_ASPIRATION_WINDOWS +
                "\nALLOW_INTERNAL_ITERATIVE_DEEPENING = "   + ALLOW_INTERNAL_ITERATIVE_DEEPENING +
                "");

        System.out.println();
        if (ALLOW_PRINCIPLE_VARIATION_SEARCH) {
            System.out.println("number of PVS hits: " + numberOfPVSHits);
            System.out.println("number of PVS misses: " + numberOfPVSMisses);
            System.out.println();
        }
        if (ALLOW_ASPIRATION_WINDOWS) {
            System.out.println("Number of successful aspirations: " + numberOfSuccessfulAspirations);
            System.out.println("Number of failed aspirations: " + numberOfFailedAspirations);
            System.out.println();
        }

        System.out.println("Number of searches with hashmove: " + numberOfSearchesWithHash);
        System.out.println("Number of searches without hashmove: " + numberOfSearchesWithoutHash);
        System.out.println();
        System.out.println("Number of Exacts: " + numberOfExacts);
        System.out.println("Number of UBs: " + numberOfUpperBounds);
        System.out.println("Number of LBs: " + numberOfLowerBounds);
        System.out.println("numberOfHashBetaCutoffs: " + numberOfHashBetaCutoffs);
        System.out.println();

        if (ALLOW_KILLERS){
            System.out.println("numberOfVictoriousKillersOne: " + numberOfVictoriousKillersOne);
            System.out.println("numberOfVictoriousKillersTwo: " + numberOfVictoriousKillersTwo);
            System.out.println("numberOfVeteranVictoriousKillersOne: " + numberOfVeteranVictoriousKillersOne);
            System.out.println("numberOfVeteranVictoriousKillersTwo: " + numberOfVeteranVictoriousKillersTwo);
            System.out.println();
        }

        if (ALLOW_MATE_KILLERS){
            System.out.println("numberOfVictoriousMaters: " + numberOfVictoriousMaters);
            System.out.println();
        }

        if (ALLOW_EXTENSIONS) {
            System.out.println("number of Check Extensions " + numberOfCheckExtensions);
            System.out.println();
        }
        if (ALLOW_LATE_MOVE_REDUCTIONS) {
            System.out.println("number of late move reductions: " + numberOfLateMoveReductions);
            System.out.println("number of late move reduction hits: " + numberOfLateMoveReductionsHits);
            System.out.println("number of late move reduction misses: " + numberOfLateMoveReductionsMisses);
            System.out.println();
        }
        if (ALLOW_LATE_MOVE_PRUNING) {
            System.out.println("number of late move prunings: " + numberOfLateMovePrunings);
            System.out.println();
        }
        if (ALLOW_NULL_MOVE_PRUNING){
            System.out.println("number of null move hits: " + numberOfNullMoveHits);
            System.out.println("number of null move misses: " + numberOfNullMoveMisses);
            System.out.println();
        }
        if (ALLOW_ALPHA_RAZORING) {
            System.out.println("Number of successful alpha razors: " + numberOfSuccessfulAlphaRazors);
            System.out.println("Number of failed alpha razors: " + numberOfFailedAlphaRazors);
            System.out.println();
        }

        if (ALLOW_BETA_RAZORING) {
            System.out.println("Number of successful beta razors: " + numberOfSuccessfulBetaRazors);
            System.out.println();
        }

        if (ALLOW_FUTILITY_PRUNING) {
            System.out.println("Number of successful futilities: " + numberOfSuccessfulFutilities);
            System.out.println("Number of failed futilities: " + numberOfFailedFutilities);
            System.out.println();
        }        
        
        if (ALLOW_QUIESCENCE_FUTILITY_PRUNING) {
            System.out.println("Number of successful Q futilities: " + numberOfSuccessfulQuiescenceFutilities);
            System.out.println("Number of failed Q futilities: " + numberOfFailedQuiescenceFutilities);
            System.out.println();
        }

        if (ALLOW_INTERNAL_ITERATIVE_DEEPENING) {
            System.out.println("Number of IIDS: " + numberOfIIDs);
            System.out.println();
        }



        System.out.println("------");
        System.out.println("number of moves made: " + numberOfMovesMade);
        System.out.println("number of Q moves made: " + numberOfQuiescentMovesMade);
        System.out.println("total moves: " + (numberOfQuiescentMovesMade + numberOfMovesMade));
        System.out.println("number of evals: " + numberOfEvals);
        System.out.println("number of Q evals: " + numberOfQuiescentEvals);

        System.out.println("number of checkmates found: " + numberOfCheckmates);
        System.out.println("number of stalemates found: " + numberOfStalemates);
        System.out.println("transposition table size: "+ TranspositionTable.getInstance().size());


        System.out.println();
        System.out.println("Winning Quiescence moves by order:");
        System.out.println(Arrays.toString(whichMoveWasTheBestQuiescence));

        System.out.println();
        System.out.println("Winning moves by order:");
        System.out.println(Arrays.toString(whichMoveWasTheBest));
        System.out.println();
        System.out.println("By percent:");
        System.out.println(Arrays.toString(toPercent(whichMoveWasTheBest)));
        System.out.println();
        System.out.println("By collective percent:");
        System.out.println(Arrays.toString(toCollectivePercent(toPercent(whichMoveWasTheBest))));
        System.out.println();
        
    }


    private static float[] toPercent (int[] nums){
        int total = 0;
        for (Integer i : nums){
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

}
