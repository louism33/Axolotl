package check;

import chess.Chessboard;
import chess.Copier;
import moveGeneration.MoveGeneratorPseudo;

public class CheckUtilities {

    static long kingDangerSquares(Chessboard board, boolean white){
        Chessboard boardWithoutMyKing = Copier.chessboardCopier(board, white, true);
        long ENEMY_PIECES = (white) ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();
        return MoveGeneratorPseudo.generatePseudoCaptureTable(boardWithoutMyKing, !white, 0, ~board.ALL_PIECES(), ENEMY_PIECES);
    }

}
