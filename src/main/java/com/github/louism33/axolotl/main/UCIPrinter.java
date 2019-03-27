package com.github.louism33.axolotl.main;

import com.fluxchess.jcpi.commands.ProtocolInformationCommand;
import com.fluxchess.jcpi.models.GenericMove;
import com.github.louism33.axolotl.search.EngineBetter;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;

import java.util.ArrayList;
import java.util.List;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY;
import static com.github.louism33.axolotl.main.UCIBoardParser.convertMyMoveToGenericMove;

public final class UCIPrinter {

    public static void sendInfoCommand(Chessboard board, int aiMove, int nodeScore, int depth, long time, long nodes){
        ProtocolInformationCommand protocolInformationCommand = new ProtocolInformationCommand();

        if (depth != 0) {
            protocolInformationCommand.setDepth(depth);
        }

        List<GenericMove> pvMoves = new ArrayList<>();
        pvMoves = PVLine.retrievePV(board);
        
        protocolInformationCommand.setMoveList(pvMoves);

        protocolInformationCommand.setCurrentMove(convertMyMoveToGenericMove(aiMove));

        protocolInformationCommand.setNodes(EngineBetter.numberOfMovesMade[0]);
        
        protocolInformationCommand.setNodes(nodes);

        EngineBetter.calculateNPS();
        long nps = EngineBetter.nps;
        protocolInformationCommand.setNps(nps);
        
        protocolInformationCommand.setTime(time);

        boolean mateFound = mateFound(nodeScore);
        if (mateFound){
            protocolInformationCommand.setMate(distanceToMate(nodeScore));
        }
        else {
            protocolInformationCommand.setCentipawns(nodeScore);
        }

        if (EngineBetter.uciEntry == null){
            System.out.println(buildString
                    (aiMove, nodeScore, depth, mateFound, 2*distanceToMate(nodeScore), pvMoves, nps, time, nodes));
        } else {
            EngineBetter.uciEntry.sendInformation(protocolInformationCommand);
        }
    }
    
    public static String buildString(int aiMove, int score, int depth, 
                                      boolean mateFound, int distanceToMate, 
                                      List<GenericMove> pvMoves, long nps, long time, long nodes){
        final String npsString = nps == 0 ? "" : ", nps: " + nps;
        if (mateFound){
            return String.format("   m%d : %s ", distanceToMate,
                    MoveParser.toString(aiMove)) + ", depth: " + depth + ", time " + time + ", " +  pvMoves + npsString + ", nodes " + nodes;
        }
        else {
            return String.format("% 5d : %s, depth: %d", score, MoveParser.toString(aiMove), depth) 
                    + ", time " + time + ", " + pvMoves + npsString + ", nodes " + nodes;
        }
    }

    public static boolean mateFound(int score){
        return score >= CHECKMATE_ENEMY_SCORE_MAX_PLY;
    }

    public static int distanceToMate(int score){
        return CHECKMATE_ENEMY_SCORE - score;
    }
}
