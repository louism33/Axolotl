package check;

import chess.Chessboard;
import chess.Copier;
import moveGeneration.MoveGeneratorPseudo;

import static chess.BitIndexing.UNIVERSE;

public class CheckUtilities {

    public static long kingDangerSquares(Chessboard board, boolean white){
        Chessboard boardWithoutMyKing = Copier.chessboardCopier(board, white, true);
        return MoveGeneratorPseudo.generatePseudoCaptureTable(boardWithoutMyKing, !white, 0, UNIVERSE, UNIVERSE);
    }

}
