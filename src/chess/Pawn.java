package chess;

import static bitboards.BitBoards.FILES;
import static bitboards.BitBoards.RANKS;

class Pawn {


    Pawn(){
        lazywriter();
        System.out.println("-----");

    }





    void lazywriter(){
        for (int r = 1; r < 9; r ++){
            for (int c = 1; c < 9; c++){
                long l = pawnPseudoMoves(r - 1, c - 1, true);
                System.out.println(
                        "static long "+((char)('A'-1+c)) +""+r+""+" = 0x" + Long.toHexString(l) +"L;"
                );
//                printLong(l);
            }
        }
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


}
