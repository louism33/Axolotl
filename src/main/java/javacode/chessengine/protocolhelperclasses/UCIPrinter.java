package javacode.chessengine.protocolhelperclasses;

import com.fluxchess.jcpi.commands.ProtocolInformationCommand;
import com.fluxchess.jcpi.models.GenericMove;
import javacode.chessengine.main.UCIEntry;
import javacode.chessengine.search.Engine;
import javacode.chessprogram.chess.Move;

import java.util.List;
import java.util.stream.Collectors;

public class UCIPrinter {
    private UCIEntry uciEntry;
    private Engine engine;

    public UCIPrinter(UCIEntry uciEntry, Engine engine) {
        this.uciEntry = uciEntry;
        this.engine = engine;
    }

    public void acceptPVLine(PVLine pvLine, int depth, boolean mateFound, int distance, long timeTaken){
        if (pvLine == null || pvLine.getPvMoves() == null || pvLine.getPvMoves().size() == 0){
            return;
        }
        List<Move> pvMoves = pvLine.getPvMoves();
        int nodeScore = pvLine.getScore();
        
        sendInfoCommand(pvMoves, nodeScore, depth, mateFound, distance, timeTaken);
    }

    private void sendInfoCommand(List<Move> moves, int nodeScore, int depth, 
                                 boolean mateFound, int distanceToMate, long timeTaken){
        if (this.engine.INFO_LOG) {
            ProtocolInformationCommand protocolInformationCommand = new ProtocolInformationCommand();
    
            List<GenericMove> genericMovesPV = moves.stream()
                    .map(UCIBoardParser::convertMyMoveToGenericMove)
                    .collect(Collectors.toList());
            
            protocolInformationCommand.setMoveList(genericMovesPV);
            protocolInformationCommand.setDepth(depth);
            if (genericMovesPV.size() != 0) {
                protocolInformationCommand.setCurrentMove(genericMovesPV.get(0));
            }
            protocolInformationCommand.setNodes(this.engine.statistics.numberOfMovesMade);
            if (timeTaken != 0) {
                long nps = this.engine.statistics.numberOfMovesMade * 1000 / timeTaken;
                if (nps > 0) {
                    protocolInformationCommand.setNps(nps);
                }
            }
            if (mateFound){
                protocolInformationCommand.setMate(distanceToMate);
            }
            else {
                protocolInformationCommand.setCentipawns(nodeScore);
            }

            this.uciEntry.sendInformation(protocolInformationCommand);
        }
        
    }
}
