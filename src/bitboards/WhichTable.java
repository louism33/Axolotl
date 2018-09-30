package bitboards;

import chess.Chessboard;

public class WhichTable {

    public static long[] whichTable(Chessboard board, long piece){

        if (piece == board.WHITE_PAWNS) return PawnMoves.PAWN_MOVE_TABLE_WHITE;
        if (piece == board.BLACK_PAWNS) return PawnMoves.PAWN_MOVE_TABLE_BLACK;
        if (piece == board.WHITE_KNIGHTS || piece == board.BLACK_KNIGHTS) return Knight.KNIGHT_MOVE_TABLE;
        if (piece == board.WHITE_BISHOPS || piece == board.BLACK_BISHOPS) return Bishop.BISHOP_MOVE_TABLE;
        if (piece == board.WHITE_ROOKS || piece == board.BLACK_ROOKS) return Rook.ROOK_MOVE_TABLE;
        if (piece == board.WHITE_QUEEN || piece == board.BLACK_QUEEN) return Queen.QUEEN_MOVE_TABLE;
        if (piece == board.WHITE_KING || piece == board.BLACK_KING) return King.KING_MOVE_TABLE;

        return null;
    }
}
