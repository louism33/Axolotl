package main.check;

import main.chess.Chessboard;
import main.chess.Copier;
import main.moveGeneration.MoveGeneratorPseudo;

import static main.chess.BitIndexing.UNIVERSE;

public class CheckUtilities {

    public static long kingDangerSquares(Chessboard board, boolean white){
        Chessboard boardWithoutMyKing = Copier.copyBoard(board, white, true);
        return MoveGeneratorPseudo.generatePseudoCaptureTable(boardWithoutMyKing, !white, 0, UNIVERSE, UNIVERSE);
    }

}
