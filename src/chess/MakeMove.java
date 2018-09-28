package chess;


public class MakeMove {

    void makeMove(Chessboard board, Move move) {

        // todo, need info from move about who is moving and being taken

        long sourceMask = BitManipulations.newPieceOnSquare(move.source);
        long destinationMask = BitManipulations.newPieceOnSquare(move.destination);

        board.WHITE_KING ^= sourceMask;


    }

    /*
    which squares are attacked by opponent ?
        if  & king != 0 we are in check
            move out of check
            remove king from board then generate king danger squares
                if double check, return king legal moves
            return king moves | blockers

        if no check
        generate all moves. only knight and king are not sliders
     */




}
