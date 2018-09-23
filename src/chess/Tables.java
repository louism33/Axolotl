package chess;

import javafx.util.Pair;

class Tables { // class will include amalgams of Moves, will be entry point for eval and for (un)makeMove

    static long generatePseudoMoves(long[] pieces, boolean white){
        long ans;
        long pawnsAttackTables = allPawnsAttackTables(pieces, white);
        long attackTables = allPieceAttackTables(pieces);
        ans = pawnsAttackTables | attackTables;
        return ans;
    }

    static long generateLegalMoves(long[] pieces, boolean white){


//        printLong(attackTables);

//        printLong(pawnsAttackTables);

        return 0;

    }

    static long generateLegalCaptures(long[] pieces, boolean white){


        return 0;

    }



    private static Pair<Integer, Integer> extractCoords(int i){
        Pair<Integer, Integer> coords;
        int f = i, r = 0;
        while (f > 7){
            f -= 8;
            r++;
        }
        coords = new Pair<>(r, 7-f);
        return coords;
    }


    private static int extractPieceIndex(long pieces){
        long finder = pieces;
        int i = 0;
        while (finder % 2 != 1){
            finder >>= 1;
            i++;
        }
        return i;
    }

    private static int extractPieceIndexOrganiser(long pieces){
        if (pieces > 0) {
            return extractPieceIndex(pieces);
        }
        long pieceIndexHack = 0x8000000000000000L;

        if (pieces == pieceIndexHack) {
            return 63;
        }

        if (pieces < 0) {
            pieces ^= pieceIndexHack;
            return extractPieceIndex(pieces);
        }

        return -1;
    }

    private static long[] howManyPieces(int whichpiece){
        if (whichpiece == 0) return new long[8];
        if (whichpiece == 1 || whichpiece == 2 || whichpiece == 3) return new long[2];
        else return new long[1];
    }

    private static long[] pieceAttackTables(long[] pieces, int whichPiece){
        long[] allPieceAttackTables = howManyPieces(whichPiece);
        long tempPieces = pieces[whichPiece];

        int i = 0;
        while (tempPieces != 0) {
            int pieceIndex = extractPieceIndexOrganiser(tempPieces);
            double d = Math.pow(2, pieceIndex);
            long pieceRemover = (pieceIndex == 63) ? (long) d + 1 : (long) d;
            tempPieces ^= pieceRemover;
            Pair<Integer, Integer> coords = extractCoords(pieceIndex);
            int f = coords.getValue();
            int r = coords.getKey();
            long l = 0;
            switch (whichPiece) {
                case 1 : l = Moves.knightPseudoMoves(r, f); break;
                case 2 : l = Moves.bishopPseudoMoves(r, f); break;
                case 3 : l = Moves.rookPseudoMoves(r, f); break;
                case 4 : l = Moves.queenPseudoMoves(r, f); break;
                case 5 : l = Moves.kingPseudoMoves(r, f); break;
            }
            allPieceAttackTables[i] = l;
            i++;
        }
        return allPieceAttackTables;
    }

    private static long[] pawnAttackTables(long[] pieces, boolean white){
        long[] allPieceAttackTables = new long[8];
        long tempPieces = pieces[0];

        int i = 0;
        while (tempPieces != 0) {
            int pieceIndex = extractPieceIndexOrganiser(tempPieces);
            double d = Math.pow(2, pieceIndex);
            long pieceRemover = (pieceIndex == 63) ? (long) d + 1 : (long) d;
            tempPieces ^= pieceRemover;
            Pair<Integer, Integer> coords = extractCoords(pieceIndex);
            int f = coords.getValue();
            int r = coords.getKey();
            long l = Moves.pawnPseudoMoves(r, f, white);
            allPieceAttackTables[i] = l;
            i++;
        }
        return allPieceAttackTables;
    }

    private static long allPieceAttackTables(long[] pieces){
        long totalPiecesAttack = 0;
        for (int p = 1; p <= 5; p ++) {
            long[] tables = pieceAttackTables(pieces, p);
            for (int t = 0; t < tables.length; t ++){
                totalPiecesAttack |= tables[t];
            }
        }
        return totalPiecesAttack;
    }

    private static long allPawnsAttackTables(long[] pieces, boolean white){
        long totalPiecesAttack = 0;

        long[] tables = pawnAttackTables(pieces, white);
        for (int t = 0; t < tables.length; t ++){
            totalPiecesAttack |= tables[t];
        }
        return totalPiecesAttack;
    }
}
