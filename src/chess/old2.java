//package chess;
//
//import bitboards.BitBoards;
//
//import static bitboards.BitBoards.FILES;
//import static bitboards.BitBoards.RANKS;
//
//public class old2 {
//
//
//

////
////
////
////    static long[] pawnAttackTables(long[] pieces, boolean white){
////        long[] allPieceAttackTables = new long[8];
////        long tempPieces = pieces[0];
////
////        int i = 0;
////        while (tempPieces != 0) {
////            int pieceIndex = extractPieceIndexOrganiser(tempPieces);
////            double d = Math.pow(2, pieceIndex);
////            long pieceRemover = (pieceIndex == 63) ? (long) d + 1 : (long) d;
////            tempPieces ^= pieceRemover;
////            Pair<Integer, Integer> coords = extractCoords(pieceIndex);
////            int f = coords.getValue();
////            int r = coords.getKey();
////            long l = pawnPseudoMoves(r, f, white);
////            allPieceAttackTables[i] = l;
////            i++;
////        }
////        return allPieceAttackTables;
////    }
//
//}
