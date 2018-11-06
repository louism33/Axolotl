package javacode.chessprogram.chess;

import static javacode.chessprogram.bitboards.BitBoards.boardWithoutEdges;

public class BitManipulations {

    public static long newPieceOnSquare (int x){
        return 0x0000000000000001L << x;
    }

    public static long squareCentredOnIndexNaive(int x){
        long piece = newPieceOnSquare(x);
        return squareCentredOnPieceNaive(piece);
    }

    private static long squareCentredOnPieceNaive(long piece){
        long answer = 0;

        answer += piece
                + (piece << 9)
                + (piece << 8)
                + (piece << 7)

                + (piece << 1)
                + (piece >>> 1)

                + (piece >>> 7)
                + (piece >>> 8)
                + (piece >>> 9)
        ;
        return answer;
    }


    private static long squareCentredOnIndexSmart(int x){
        long piece = newPieceOnSquare(x);
        return squareCentredOnPieceSmart(piece);
    }


    private static long squareCentredOnPieceSmart(long piece){
        long answer = 0;

        if ((boardWithoutEdges & piece) != 0){
            return squareCentredOnPieceNaive(piece);
        }
        else {
            throw new RuntimeException("unfinished");
        }

//        if ((RANK_EIGHT & piece) != 0) {
//            return ((piece << 1)
//                    + (piece >>> 1)
//
//                    + (piece >>> 7)
//                    + (piece >>> 8)
//                    + (piece >>> 9));
//        }
//        if ((RANK_ONE & piece) != 0) {
//            return ((piece << 1)
//                    + (piece >>> 1)
//                    + (piece << 9)
//                    + (piece << 8)
//                    + (piece << 7));
//                    
//        }

                
    }
    
}
