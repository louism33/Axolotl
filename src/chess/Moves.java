package chess;


class Moves {

    static void printLong(long l){
        for (int y = 0; y < 8; y++) {
            for (int i = 0; i < 8; i++) {
                String s = Long.toBinaryString(l);
                while (s.length() < 64) {
                    s = "0"+s;
                }
                System.out.print(s.charAt(y * 8 + i));
            }
            System.out.println();
        }
    }

    static void printArrayOfLongs(long[] board){
        for (long l : board){
            printLong(l);
            System.out.println("---");
        }
    }

    private static long[] RANKS = BitBoards.RANKS;
    private static long[] FILES = BitBoards.FILES;

    Moves(){

        long bishopPseudoMoves = bishopPseudoMoves(3, 3);

        printLong(bishopPseudoMoves);

    }

    static long pawnLegalMoves(int rank, int file, boolean white){
        long knightPseudoMoves = pawnPseudoMoves(rank, file, white);
        long allPieces = BitBoards.ALL_WHITE_PIECES() | BitBoards.ALL_BLACK_PIECES();
        return (knightPseudoMoves | allPieces) ^ allPieces;
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

    static long pawnLegalCaptures(int rank, int file, boolean white){
        long ans = 0;
        long pawnPseudoCaptures = pawnPseudoCaptures(rank, file, white);
        long enemies = (white) ? BitBoards.ALL_BLACK_PIECES() : BitBoards.ALL_WHITE_PIECES();
        return pawnPseudoCaptures & enemies;
    }

    static long pawnPseudoCaptures(int rank, int file, boolean white){
        long pawn = RANKS[rank] & FILES[file];
        if (rank == 0 | rank == 7) System.out.println("Pawn on Final Rank Error?");
        long ans, r = 0;
        if (file >= 1) r |= FILES[file-1];
        if (file <= 6) r |= FILES[file+1];
        long l = (white) ? r & RANKS[rank+1] : r & RANKS[rank-1];
        return l;
    }


    static long knightLegalMoves(int rank, int file, boolean white){
        long knightPseudoMoves = knightPseudoMoves(rank, file);
        long myPieces = (white) ? BitBoards.ALL_WHITE_PIECES() : BitBoards.ALL_BLACK_PIECES();
        return (knightPseudoMoves | myPieces) ^ myPieces;
    }

    static long knightPseudoMoves(int rank, int file){
        long knight = RANKS[rank] & FILES[file];
        long ans, r1 = 0, r2 = 0, c1 = 0, c2 = 0;
        if (rank >= 1) r1 |= RANKS[rank-1];
        if (rank <= 6) r1 |= RANKS[rank+1];
        if (rank >= 2) r2 |= RANKS[rank-2];
        if (rank <= 5) r2 |= RANKS[rank+2];
        if (file >= 1) c1 |= FILES[file-1];
        if (file <= 6) c1 |= FILES[file+1];
        if (file >= 2) c2 |= FILES[file-2];
        if (file <= 5) c2 |= FILES[file+2];
        ans = r1 & c2 | r2 & c1;
        return ans;
    }




    static long bishopLegalMoves(int rank, int file, boolean white){

        return 0;
    }

    static long bishopPseudoMoves(int rank, int file){
        long ans = 0;

        int r = rank+1, f = file+1;
        while (r < 8 && f < 8){
            ans |= (RANKS[r] & FILES[f]);
            r++; f++;
        }

        int rr = rank-1, ff = file-1;
        while (rr >= 0 && ff >= 0){
            ans |= (RANKS[rr] & FILES[ff]);
            rr--; ff--;
        }
        int rrr = rank+1, fff = file-1;
        while (rrr < 8 && fff >= 0){
            ans |= (RANKS[rrr] & FILES[fff]);
            rrr++; fff--;
        }

        int rrrr = rank-1, ffff = file+1;
        while (rrrr >= 0 && ffff < 8){
            ans |= (RANKS[rrrr] & FILES[ffff]);
            rrrr--; ffff++;
        }
        return ans;
    }


    static long rookLegalMoves(int rank, int file, boolean white){

        return 0;
    }
    static long rookPseudoMoves(int rank, int file){ // todo: blocker
        long piece = RANKS[rank] & FILES[file];
        long range = RANKS[rank] | FILES[file];
        return piece ^ range;
    }


    static long queenLegalMoves(int rank, int file, boolean white){

        return 0;
    }
    static long queenPseudoMoves(int rank, int file){
        long ans = 0;
        ans |= bishopPseudoMoves(rank, file);
        ans |= rookPseudoMoves(rank, file);
        return ans;
    }

    static long kingLegalMoves(int rank, int file, boolean white){

        return 0;
    }

    static long kingPseudoMoves(int rank, int file){
        long ans, r = RANKS[rank], c = FILES[file];
        if (rank >= 1) r |= RANKS[rank-1];
        if (rank <= 6) r |= RANKS[rank+1];
        if (file >= 1) c |= FILES[file-1];
        if (file <= 6) c |= FILES[file+1];
        ans = r & c;
        ans ^= RANKS[rank] & FILES[file];
        return ans;
    }


}
