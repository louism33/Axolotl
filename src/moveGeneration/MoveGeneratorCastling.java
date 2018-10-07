package moveGeneration;

import bitboards.BitBoards;
import chess.Chessboard;
import chess.Move;

import java.util.ArrayList;
import java.util.List;

public class MoveGeneratorCastling {

    //check if we are in check happens elsewhere
    public static List<Move> generateCastlingMoves(Chessboard board, boolean white){
        List<Move> moves = new ArrayList<>();

        if (white){
            if(board.whiteCanCastleK){
                if (areTheseSquaresEmpty(board, BitBoards.whiteCastleKingEmpties)
                        && areTheseSquaresUnthreatened(board, white, BitBoards.whiteCastleKingEmpties)
                        && ((board.WHITE_KING & BitBoards.WHITE_KING) != 0)
                        && ((board.WHITE_ROOKS & BitBoards.SOUTH_EAST_CORNER) != 0)){

                    Move whiteCastleSE = new Move(3, 1, true, false, false, false, false, false, false);
                    moves.add(whiteCastleSE);
                }
            }

            if(board.whiteCanCastleQ){
                if (areTheseSquaresEmpty(board, BitBoards.whiteCastleQueenEmpties)
                        && areTheseSquaresUnthreatened(board, white, BitBoards.whiteCastleQueenEmpties)
                        && ((board.WHITE_KING & BitBoards.WHITE_KING) != 0)
                        && ((board.WHITE_ROOKS & BitBoards.SOUTH_WEST_CORNER) != 0)){

                    Move whiteCastleSW = new Move(3, 5, true, false, false, false, false, false, false);
                    moves.add(whiteCastleSW);
                }
            }


        }
        else {
            if(board.blackCanCastleK){
                if (areTheseSquaresEmpty(board, BitBoards.blackCastleKingEmpties)
                        && areTheseSquaresUnthreatened(board, white, BitBoards.blackCastleKingEmpties)
                        && ((board.BLACK_KING & BitBoards.BLACK_KING) != 0)
                        && ((board.BLACK_ROOKS & BitBoards.NORTH_EAST_CORNER) != 0)){

                    Move blackCastleNE = new Move(59, 57, true, false, false, false, false, false, false);
                    moves.add(blackCastleNE);
                }
            }

            if(board.blackCanCastleQ){
                if (areTheseSquaresEmpty(board, BitBoards.blackCastleQueenEmpties)
                        && areTheseSquaresUnthreatened(board, white, BitBoards.blackCastleQueenEmpties)
                        && ((board.BLACK_KING & BitBoards.BLACK_KING) != 0)
                        && ((board.BLACK_ROOKS & BitBoards.NORTH_WEST_CORNER) != 0)){

                    Move blackCastleNW = new Move(59, 61, true, false, false, false, false, false, false);
                    moves.add(blackCastleNW);
                }
            }
        }





        return moves;
    }



    public static boolean areTheseSquaresUnthreatened(Chessboard board, boolean white, long squares){
        return true;
    }

    public static boolean areTheseSquaresEmpty(Chessboard board, long squares){
        return ((board.ALL_PIECES() & squares) == 0);
    }


}
