package com.github.louism33.axolotl.main;

import com.fluxchess.jcpi.commands.ProtocolInformationCommand;
import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.chesscore.MoveParser;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY;
import static com.github.louism33.axolotl.main.UCIBoardParser.convertMyMoveToGenericMove;

public class UCIPrinter {

    public static void sendInfoCommand(int aiMove, int nodeScore, int depth){

        ProtocolInformationCommand protocolInformationCommand = new ProtocolInformationCommand();

        if (depth != 0) {
            protocolInformationCommand.setDepth(depth);
        }

        protocolInformationCommand.setCurrentMove(convertMyMoveToGenericMove(aiMove));

        protocolInformationCommand.setNodes(Engine.regularMovesMade + Engine.quiescentMovesMade);

        protocolInformationCommand.setNps(Engine.getNps());

        boolean mateFound = mateFound(nodeScore);
        if (mateFound){
            protocolInformationCommand.setMate(distanceToMate(nodeScore));
        }
        else {
            protocolInformationCommand.setCentipawns(nodeScore);
        }
        
        if (Engine.getUciEntry() == null){
            System.out.println(buildString(aiMove, nodeScore, depth, mateFound, distanceToMate(nodeScore)));
        } else {
            Engine.getUciEntry().sendInformation(protocolInformationCommand);
        }
    }
    
    private static String buildString(int aiMove, int score, int depth, boolean mateFound, int distanceToMate){
        if (mateFound){
            return String.format("   m%d : %s ", distanceToMate, MoveParser.toString(aiMove));
        }
        else {
            return String.format("% 5d : %s, depth: %d", score, MoveParser.toString(aiMove), depth);
        }
    }

    private static boolean mateFound(int score){
        return score >= CHECKMATE_ENEMY_SCORE_MAX_PLY;
    }

    private static int distanceToMate(int score){
        return CHECKMATE_ENEMY_SCORE - score;
    }
}
