package com.github.louism33.axolotl.protocolhelperclasses;

import com.fluxchess.jcpi.commands.ProtocolInformationCommand;
import com.fluxchess.jcpi.models.GenericMove;
import com.github.louism33.axolotl.main.UCIBoardParser;
import com.github.louism33.axolotl.main.UCIEntry;
import com.github.louism33.axolotl.search.Engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY;
import static com.github.louism33.axolotl.main.UCIBoardParser.convertMyMoveToGenericMove;

public class UCIPrinter {

    public static void acceptPVLine(PVLine pvLine, long timeTaken){
        if (pvLine == null || pvLine.getPvMoves() == null || pvLine.getPvMoves().length == 0){
            return;
        }

        int[] pvMoves = pvLine.getPvMoves();
        int nodeScore = pvLine.getScore();

        sendInfoCommand(pvMoves, nodeScore, 0, mateFound(nodeScore), distanceToMate(nodeScore), timeTaken);
    }
    
    public static void acceptPVLine(PVLine pvLine, int depth, boolean mateFound, int distance, long timeTaken){
        if (pvLine == null || pvLine.getPvMoves() == null || pvLine.getPvMoves().length == 0){
            return;
        }

        int[] pvMoves = pvLine.getPvMoves();
        int nodeScore = pvLine.getScore();

        sendInfoCommand(pvMoves, nodeScore, depth, mateFound, distance, timeTaken);
    }

    private static void sendInfoCommand(int[] moves, int nodeScore, int depth,
                                        boolean mateFound, int distanceToMate, long timeTaken){
        if (Engine.getEngineSpecifications().INFO_LOG) {
            ProtocolInformationCommand protocolInformationCommand = new ProtocolInformationCommand();

            List<GenericMove> genericMovesPV = new ArrayList<>(moves.length);

            for (int i = 0; i < moves.length; i++){
                int move = moves[i];
                if (move == 0){
                    break;
                }
                genericMovesPV.add(convertMyMoveToGenericMove(move));
            }

            protocolInformationCommand.setMoveList(genericMovesPV);
            if (depth != 0) {
                protocolInformationCommand.setDepth(depth);
            }
            
            if (genericMovesPV.size() != 0) {
                protocolInformationCommand.setCurrentMove(genericMovesPV.get(0));
            }
            final long totalMovesMade = Engine.statistics.numberOfMovesMade + Engine.statistics.numberOfQuiescentMovesMade;

            protocolInformationCommand.setNodes(totalMovesMade);

            if (timeTaken != 0) {
                long nps = totalMovesMade * 1000 / timeTaken;
                protocolInformationCommand.setNps(nps);
            }
            if (mateFound){
                protocolInformationCommand.setMate(distanceToMate);
            }
            protocolInformationCommand.setCentipawns(nodeScore);

            if (Engine.getUciEntry() != null) {
                Engine.getUciEntry().sendInformation(protocolInformationCommand);
            }
            else if (Engine.PRINT_INFO) {
                if (mateFound) {
                    System.out.println("m"+distanceToMate + " : " + protocolInformationCommand.getMoveList());
                }
                else {
                    System.out.println(String.format("% 5d : ", protocolInformationCommand.getCentipawns()) + protocolInformationCommand.getMoveList());
                }
            }
        }
    }
    
    private static boolean mateFound(int score){
        return score >= CHECKMATE_ENEMY_SCORE_MAX_PLY;
    }

    private static int distanceToMate(int score){
        return CHECKMATE_ENEMY_SCORE - score;
    }
}
