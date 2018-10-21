package javacode.chessengine;

import javacode.chessprogram.chess.Move;

import java.util.HashMap;

class TranspositionTable extends HashMap<Long, TranspositionTable.TableObject> {
    
    private static TranspositionTable table = new TranspositionTable();
    
    private TranspositionTable(){}
    
    /*
    Transposition Table:
    a singleton hashmap which gets populated by our search
     */
    static TranspositionTable getInstance(){
        if (table == null){
            table = new TranspositionTable();
        }
        return table;
    }

    public static class TableObject {
        private final Move move;
        private final int score;
        private final int depth;
        private final Flag flag;
        
        public enum Flag {
                EXACT, LOWERBOUND, UPPERBOUND
        }

        TableObject(Move move, int score, int depth, Flag flag) {
            this.move = move;
            this.score = score;
            this.depth = depth;
            this.flag = flag;
        }

        public Move getMove() {
            return move;
        }

        int getScore() {
            return score;
        }

        Flag getFlag() {
            return flag;
        }

        int getDepth() {
            return depth;
        }

        @Override
        public String toString() {
            return "TableObject{" +
                    "move=" + move +
                    ", score=" + score +
                    ", depth=" + depth +
                    ", flag=" + flag +
                    '}';
        }
    }
}
