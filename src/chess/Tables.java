//package chess;
//
//import javafx.util.Pair;
//
//import java.util.List;
//
//
//class Tables { // class will include amalgams of Moves, will be entry point for eval and for (un)makeMove
//
//
//    Tables (){
//        int r = 0, f = 3;
//        long w = pseudoMovesTables(BitBoards.WHITE_PIECES, true);
//        long b = pseudoMovesTables(BitBoards.BLACK_PIECES, false);
//
//
//    }
//
//
//    static List<String> availableMoves (boolean white){
//        long[] pieces = BitBoards.WHITE_PIECES;
//
//        return null;
//
//    }
//
//
//    static long longOutOfIntCoords(int rank, int file){
//        double d = Math.pow(2, rank * 8 + 7-file);
//        long s = (long) d;
//        return s;
//    }
//
//
//
//    static boolean isSquarePseudoThreatenedBy(int rank, int file, boolean whiteAttacks){
//        long ans = 0;
//        long square = longOutOfIntCoords(rank, file);
//        long attackedSquares = (whiteAttacks) ? pseudoMovesTables(BitBoards.WHITE_PIECES, true) : pseudoMovesTables(BitBoards.BLACK_PIECES, false);
//        long l = square & attackedSquares;
//        return !((l) == 0);
//    }
//
//    static long pseudoMovesTables(long[] pieces, boolean white){
//        long ans;
//        long pawnsAttackTables = allPawnsAttackTables(pieces, white);
//        long attackTables = allPieceAttackTables(pieces);
//        ans = pawnsAttackTables | attackTables;
//        return ans;
//    }
//
//    static long legalMovesTable(long[] pieces, boolean white){
//
//
////        printLong(attackTables);
//
////        printLong(pawnsAttackTables);
//
//        return 0;
//
//    }
//
//    static long pseudoCapturesTables(long[] pieces, boolean white){
//
//
//        return 0;
//
//    }
//
//    static long legalCapturesTables(long[] pieces, boolean white){
//
//
//        return 0;
//
//    }
//
//
//
//    static Pair<Integer, Integer> extractCoords(int i){
//        Pair<Integer, Integer> coords;
//        int f = i, r = 0;
//        while (f > 7){
//            f -= 8;
//            r++;
//        }
//        coords = new Pair<>(r, 7-f);
//        return coords;
//    }
//
//
//    static int extractPieceIndex(long pieces){
//        long finder = pieces;
//        int i = 0;
//        while (finder % 2 != 1){
//            finder >>= 1;
//            i++;
//        }
//        return i;
//    }
//
//    static int extractPieceIndexOrganiser(long pieces){
//        if (pieces > 0) {
//            return extractPieceIndex(pieces);
//        }
//        long pieceIndexHack = 0x8000000000000000L;
//
//        if (pieces == pieceIndexHack) {
//            return 63;
//        }
//
//        if (pieces < 0) {
//            pieces ^= pieceIndexHack;
//            return extractPieceIndex(pieces);
//        }
//        return -1;
//    }
//
//
//
//    static long allPieceAttackTables(long[] pieces){
//        long totalPiecesAttack = 0;
//        for (int p = 1; p <= 5; p ++) {
//            long[] tables = pieceAttackTables(pieces, p);
//            for (int t = 0; t < tables.length; t ++){
//                totalPiecesAttack |= tables[t];
//            }
//        }
//        return totalPiecesAttack;
//    }
//
//    static long[] pieceAttackTables(long[] pieces, int whichPiece){
//        long[] allPieceAttackTables = howManyPieces(whichPiece);
//        long tempPieces = pieces[whichPiece];
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
//            long l = 0;
//            switch (whichPiece) {
//                case 1 : l = Moves.knightPseudoMoves(r, f); break;
//                case 2 : l = Moves.bishopPseudoMoves(r, f); break;
//                case 3 : l = Moves.rookPseudoMoves(r, f); break;
//                case 4 : l = Moves.queenPseudoMoves(r, f); break;
////                case 5 : l = Moves.kingPseudoMoves(r, f); break;
//            }
//            allPieceAttackTables[i] = l;
//            i++;
//        }
//        return allPieceAttackTables;
//    }
//
//    static long allPawnsAttackTables(long[] pieces, boolean white){
//        long totalPiecesAttack = 0;
//
//        long[] tables = pawnAttackTables(pieces, white);
//        for (int t = 0; t < tables.length; t ++){
//            totalPiecesAttack |= tables[t];
//        }
//        return totalPiecesAttack;
//    }
//
////    private static long[] pawnAttackTables(long[] pieces, boolean white){
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
////            long l = Moves.pawnPseudoMoves(r, f, white);
////            allPieceAttackTables[i] = l;
////            i++;
////        }
////        return allPieceAttackTables;
////    }
//
//    static long[] howManyPieces(int whichpiece){
//        if (whichpiece == 0) return new long[8];
//        if (whichpiece == 1 || whichpiece == 2 || whichpiece == 3) return new long[2];
//        else return new long[1];
//    }
//
//    static void printLong(long l){
//        for (int y = 0; y < 8; y++) {
//            for (int i = 0; i < 8; i++) {
//                String s = Long.toBinaryString(l);
//                while (s.length() < 64) {
//                    s = "0"+s;
//                }
//                System.out.print(s.charAt(y * 8 + i));
//            }
//            System.out.println();
//        }
//    }
//
//    static void printArrayOfLongs(long[] board){
//        for (long l : board){
//            printLong(l);
//            System.out.println("---");
//        }
//    }
//
//
//
//
//}
