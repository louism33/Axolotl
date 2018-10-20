package javacode.chessprogram.moveGeneration;

import javacode.chessprogram.bitboards.BitBoards;
import javacode.chessprogram.check.CheckChecker;
import javacode.chessprogram.chess.BitExtractor;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.List;

class MoveGeneratorCastling {

    // checking if we are in check happens elsewhere
    static List<Move> generateCastlingMoves(Chessboard board, boolean white){
        List<Move> moves = new ArrayList<>();

        if (white){
            if(board.whiteCanCastleK){
                if (areTheseSquaresEmpty(board, BitBoards.whiteCastleKingEmpties)
                        && areTheseSquaresUnthreatened(board, true, BitBoards.whiteCastleKingEmpties)
                        && ((board.WHITE_KING & BitBoards.WHITE_KING) != 0)
                        && ((board.WHITE_ROOKS & BitBoards.SOUTH_EAST_CORNER) != 0)){

                    Assert.assertTrue(areTheseSquaresUnthreatened(board, true, board.WHITE_KING));
                    Assert.assertTrue(!CheckChecker.boardInCheck(board, true));
                    
                    Move whiteCastleSE = new Move(3, 1, true, false, false, false, false, false, false);
                    moves.add(whiteCastleSE);
                }
            }

            if(board.whiteCanCastleQ){
                if (areTheseSquaresEmpty(board, BitBoards.whiteCastleQueenEmpties)
                        && areTheseSquaresUnthreatened(board, true, BitBoards.whiteCastleQueenUnthreateneds)
                        && ((board.WHITE_KING & BitBoards.WHITE_KING) != 0)
                        && ((board.WHITE_ROOKS & BitBoards.SOUTH_WEST_CORNER) != 0)){

                    Assert.assertTrue(areTheseSquaresUnthreatened(board, true, board.WHITE_KING));
                    Assert.assertTrue(!CheckChecker.boardInCheck(board, true));

                    Move whiteCastleSW = new Move(3, 5, true, false, false, false, false, false, false);
                    moves.add(whiteCastleSW);
                }
            }


        }
        else {
            if(board.blackCanCastleK){
                if (areTheseSquaresEmpty(board, BitBoards.blackCastleKingEmpties)
                        && areTheseSquaresUnthreatened(board, false, BitBoards.blackCastleKingEmpties)
                        && ((board.BLACK_KING & BitBoards.BLACK_KING) != 0)
                        && ((board.BLACK_ROOKS & BitBoards.NORTH_EAST_CORNER) != 0)){

                    Assert.assertTrue(areTheseSquaresUnthreatened(board, false, board.BLACK_KING));
                    Assert.assertTrue(!CheckChecker.boardInCheck(board, false));
                    
                    Move blackCastleNE = new Move(59, 57, true, false, false, false, false, false, false);
                    moves.add(blackCastleNE);
                }
            }

            if(board.blackCanCastleQ){
                if (areTheseSquaresEmpty(board, BitBoards.blackCastleQueenEmpties)
                        && areTheseSquaresUnthreatened(board, false, BitBoards.blackCastleQueenUnthreateneds)
                        && ((board.BLACK_KING & BitBoards.BLACK_KING) != 0)
                        && ((board.BLACK_ROOKS & BitBoards.NORTH_WEST_CORNER) != 0)){

                    Assert.assertTrue(areTheseSquaresUnthreatened(board, false, board.BLACK_KING));
                    Assert.assertTrue(!CheckChecker.boardInCheck(board, false));
                    
                    Move blackCastleNW = new Move(59, 61, true, false, false, false, false, false, false);
                    moves.add(blackCastleNW);
                }
            }
        }

        return moves;
    }



    private static boolean areTheseSquaresUnthreatened(Chessboard board, boolean white, long squares){
        List<Long> squaresThatShouldBeUnthreatened = BitExtractor.getAllPieces(squares, 0);
        for (long square : squaresThatShouldBeUnthreatened) {
            int numberOfThreats = CheckChecker.numberOfPiecesThatLegalThreatenSquare(board, white, square); 
            if (numberOfThreats > 0){
                return false;
            }
        }
        return true;
    }

    private static boolean areTheseSquaresEmpty(Chessboard board, long squares){
        return ((board.ALL_PIECES() & squares) == 0);
    }
}
