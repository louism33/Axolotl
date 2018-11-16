package javacode.chessengine.transpositiontable;

import javacode.chessengine.evaluation.Evaluator;
import javacode.chessprogram.chess.Move;

import java.util.HashMap;

public class TranspositionTable extends HashMap<Long, TranspositionTable.TableObject> {

    public TranspositionTable(){}
    
    public static class TableObject {
        private Move move;
        private int score;
        private int depth;
        private Flag flag;
        
        public enum Flag {
                EXACT, LOWERBOUND, UPPERBOUND
        }

        public TableObject(Move move, int score, int depth, Flag flag) {
            this.move = move;
            this.score = score;
            this.depth = depth;
            this.flag = flag;
        }

        public Move getMove() {
            return move;
        }

        public int getScore(int ply) {
            if (score > Evaluator.CHECKMATE_ENEMY_SCORE_MAX_PLY){
                return score - ply;
            }
            else if (score < Evaluator.IN_CHECKMATE_SCORE_MAX_PLY){
                return score + ply;
            }
            return score;
        }

        public Flag getFlag() {
            return flag;
        }

        public int getDepth() {
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
