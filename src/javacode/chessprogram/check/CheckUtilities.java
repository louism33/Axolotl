package javacode.chessprogram.check;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Copier;
import javacode.chessprogram.moveGeneration.MoveGeneratorPseudo;

import static javacode.chessprogram.chess.BitIndexing.UNIVERSE;

class CheckUtilities {

    static long kingDangerSquares(Chessboard board, boolean white){
        Chessboard boardWithoutMyKing = Copier.copyBoard(board, white, true);
        return MoveGeneratorPseudo.generatePseudoCaptureTable(boardWithoutMyKing, !white, 0, UNIVERSE, UNIVERSE);
    }

}
