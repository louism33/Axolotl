//package com.github.louism33.axolotl.helper.protocolhelperclasses;
//
//import com.fluxchess.jcpi.commands.ProtocolInformationCommand;
//import com.fluxchess.jcpi.models.GenericMove;
//import com.fluxchess.jcpi.protocols.NoProtocolException;
//import com.github.louism33.axolotl.search.Engine;
//import com.github.louism33.axolotl.search.EngineSpecifications;
//import com.github.louism33.chesscore.Chessboard;
//import com.github.louism33.chesscore.IllegalUnmakeException;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE;
//import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY;
//import static com.github.louism33.axolotl.main.UCIBoardParser.convertMyMoveToGenericMove;
//
//public class UCIPrinter {
//
//    public static void printPV(Chessboard board) throws IllegalUnmakeException {
//        PVLine.retrievePV(board);
//        acceptPVLine(PVLine.getNodeScore(), PVLine.getPvMoves(), PVLine.nps);
//    }
//
//    private static void acceptPVLine(int nodeScore, int[] pvMoves, long nps){
//        sendInfoCommand(pvMoves, nodeScore, 0, mateFound(nodeScore), distanceToMate(nodeScore), nps);
//    }
//
//    private static void sendInfoCommand(int[] moves, int nodeScore, int depth,
//                                        boolean mateFound, int distanceToMate, long nps){
//        if (!EngineSpecifications.INFO_LOG) {
//            return;
//        }
//        
//        ProtocolInformationCommand protocolInformationCommand = new ProtocolInformationCommand();
//
//        List<GenericMove> genericMovesPV = new ArrayList<>(moves.length);
//
//        for (int i = 0; i < moves.length; i++){
//            int move = moves[i];
//            if (move == 0){
//                break;
//            }
//            genericMovesPV.add(convertMyMoveToGenericMove(move));
//        }
//
//        protocolInformationCommand.setMoveList(genericMovesPV);
//        
//        if (depth != 0) {
//            protocolInformationCommand.setDepth(depth);
//        }
//
//        if (genericMovesPV.size() != 0) {
//            protocolInformationCommand.setCurrentMove(genericMovesPV.get(0));
//        }
//        
//        final long totalMovesMade = Engine.regularMovesMade + Engine.quiescentMovesMade;
//
//        protocolInformationCommand.setNodes(totalMovesMade);
//
//        if (nps != 0) {
//            protocolInformationCommand.setNps(nps);
//        }
//        
//        if (mateFound){
//            protocolInformationCommand.setMate(distanceToMate);
//        }
//        
//        protocolInformationCommand.setCentipawns(nodeScore);
//
//            if (Engine.getUciEntry() != null) {
//                try {
//                    Engine.getUciEntry().sendInformation(protocolInformationCommand);
//                }
//                catch (NoProtocolException e){}
//            }
//        if (EngineSpecifications.INFO_LOG) {
//            if (mateFound) {
//                System.out.println("  m"+distanceToMate + " : " + protocolInformationCommand.getMoveList());
//            }
//            else {
//                System.out.println(String.format("% 5d : ", protocolInformationCommand.getCentipawns()) + protocolInformationCommand.getMoveList());
//            }
//        }
//    }
//
//    private static boolean mateFound(int score){
//        return score >= CHECKMATE_ENEMY_SCORE_MAX_PLY;
//    }
//
//    private static int distanceToMate(int score){
//        return CHECKMATE_ENEMY_SCORE - score;
//    }
//}
