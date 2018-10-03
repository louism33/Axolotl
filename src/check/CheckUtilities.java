package check;

import chess.BitIndexing;
import chess.Chessboard;
import chess.Move;
import moveGeneration.MoveGeneratorPseudo;

import java.util.List;

public class CheckUtilities {

    static long kingDangerSquares(Chessboard board, boolean white){
        Chessboard boardWithoutMyKing = chessboardCopier(board, white, true);
        long ENEMY_PIECES = (white) ? board.ALL_BLACK_PIECES() : board.ALL_WHITE_PIECES();
        return MoveGeneratorPseudo.generatePseudoCaptureTable(boardWithoutMyKing, !white, ~board.ALL_PIECES(), ENEMY_PIECES);
    }


    public static Chessboard chessboardCopier (Chessboard board, boolean white, boolean ignoreMyKing){
        Chessboard newBoard = new Chessboard();
        newBoard.WHITE_PAWNS = board.WHITE_PAWNS;
        newBoard.WHITE_KNIGHTS = board.WHITE_KNIGHTS;
        newBoard.WHITE_BISHOPS = board.WHITE_BISHOPS;
        newBoard.WHITE_ROOKS = board.WHITE_ROOKS;
        newBoard.WHITE_QUEEN = board.WHITE_QUEEN;
        newBoard.WHITE_KING = board.WHITE_KING;

        newBoard.BLACK_PAWNS = board.BLACK_PAWNS;
        newBoard.BLACK_KNIGHTS = board.BLACK_KNIGHTS;
        newBoard.BLACK_BISHOPS = board.BLACK_BISHOPS;
        newBoard.BLACK_ROOKS = board.BLACK_ROOKS;
        newBoard.BLACK_QUEEN = board.BLACK_QUEEN;
        newBoard.BLACK_KING = board.BLACK_KING;

        if (ignoreMyKing && white) {
            newBoard.WHITE_KING = 0;
        }
        if (ignoreMyKing && !white) {
            newBoard.BLACK_KING = 0;
        }

        newBoard.whiteCanCastleK = board.whiteCanCastleK;
        newBoard.blackCanCastleK = board.blackCanCastleK;
        newBoard.whiteCanCastleQ = board.whiteCanCastleQ;
        newBoard.blackCanCastleQ = board.blackCanCastleQ;

        newBoard.whiteTurn = board.whiteTurn;

        return newBoard;
    }
}
