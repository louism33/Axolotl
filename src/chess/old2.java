package chess;

import static chess.BitBoards.FILES;
import static chess.BitBoards.RANKS;

public class old2 {



    static long allPawnsPseudoMove (long pawns, boolean white){
        if (white) return BitBoards.northOne(pawns);
        else return  BitBoards.southOne(pawns);

    }



    static long pawnLegalMoves(int rank, int file, boolean white){ // en passant block
        long pawnPseudoMoves = pawnPseudoMoves(rank, file, white);
        long allPieces = BitBoards.ALL_WHITE_PIECES() | BitBoards.ALL_BLACK_PIECES();
        return (pawnPseudoMoves | allPieces) ^ allPieces;
    }

    static long pawnPseudoMoves(int rank, int file, boolean white){
        long pawn = RANKS[rank] & FILES[file];
        long ans = 0;
        if (white){
            if (rank == 1){
                ans |= RANKS[rank+2];
            }
            ans |= RANKS[rank+1];
        }
        if (!white){
            if (rank == 6){
                ans |= RANKS[rank-2];
            }
            ans |= RANKS[rank-1];
        }
        ans &= FILES[file];
        return ans;
    }



//    static long pawnLegalCaptures(int rank, int file, boolean white){
//        long ans = 0;
//        long pawnPseudoCaptures = pawnPseudoCaptures(rank, file, white);
//        long enemies = (white) ? BitBoards.ALL_BLACK_PIECES() : BitBoards.ALL_WHITE_PIECES();
//        return pawnPseudoCaptures & enemies;
//    }
//
//    static long pawnPseudoCaptures(int rank, int file, boolean white){
//        long pawn = RANKS[rank] & FILES[file];
//        if (rank == 0 | rank == 7) System.out.println("Pawn on Final Rank Error?");
//        long ans, r = 0;
//        if (file >= 1) r |= FILES[file-1];
//        if (file <= 6) r |= FILES[file+1];
//        long l = (white) ? r & RANKS[rank+1] : r & RANKS[rank-1];
//        return l;
//    }
//
//
//
//    static long[] pawnAttackTables(long[] pieces, boolean white){
//        long[] allPieceAttackTables = new long[8];
//        long tempPieces = pieces[0];
//
//        int i = 0;
//        while (tempPieces != 0) {
//            int pieceIndex = extractPieceIndexOrganiser(tempPieces);
//            double d = Math.pow(2, pieceIndex);
//            long pieceRemover = (pieceIndex == 63) ? (long) d + 1 : (long) d;
//            tempPieces ^= pieceRemover;
//            Pair<Integer, Integer> coords = extractCoords(pieceIndex);
//            int f = coords.getValue();
//            int r = coords.getKey();
//            long l = pawnPseudoMoves(r, f, white);
//            allPieceAttackTables[i] = l;
//            i++;
//        }
//        return allPieceAttackTables;
//    }

}
