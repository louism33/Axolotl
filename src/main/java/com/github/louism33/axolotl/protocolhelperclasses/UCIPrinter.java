package com.github.louism33.axolotl.protocolhelperclasses;

import com.fluxchess.jcpi.commands.ProtocolInformationCommand;
import com.fluxchess.jcpi.models.GenericMove;
import com.github.louism33.axolotl.main.UCIBoardParser;
import com.github.louism33.axolotl.main.UCIEntry;
import com.github.louism33.axolotl.search.Engine;

import java.util.List;
import java.util.stream.Collectors;

public class UCIPrinter {

    private static UCIEntry uciEntry = new UCIEntry();
    
    public static void acceptPVLine(PVLine pvLine, int depth, boolean mateFound, int distance, long timeTaken){
        if (pvLine == null || pvLine.getPvMoves() == null || pvLine.getPvMoves().size() == 0){
            return;
        }
        List<Integer> pvMoves = pvLine.getPvMoves();
        int nodeScore = pvLine.getScore();

        sendInfoCommand(pvMoves, nodeScore, depth, mateFound, distance, timeTaken);
    }

    private static void sendInfoCommand(List<Integer> moves, int nodeScore, int depth,
                                 boolean mateFound, int distanceToMate, long timeTaken){
        if (Engine.getEngineSpecifications().INFO_LOG) {
            ProtocolInformationCommand protocolInformationCommand = new ProtocolInformationCommand();

            List<GenericMove> genericMovesPV = moves.stream()
                    .map(UCIBoardParser::convertMyMoveToGenericMove)
                    .collect(Collectors.toList());

            protocolInformationCommand.setMoveList(genericMovesPV);
            protocolInformationCommand.setDepth(depth);
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
            else {
                protocolInformationCommand.setCentipawns(nodeScore);
            }

//            if (uciEntry != null) {
//                uciEntry.sendInformation(protocolInformationCommand);
//            }
//            else{
            if (Engine.PRINT_INFO) {
                System.out.println(protocolInformationCommand.getCentipawns() + " : " + protocolInformationCommand.getMoveList());
            }
//            }
        }

    }
}
