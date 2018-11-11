package javacode.chessengine;

import javacode.chessprogram.chess.Move;
import javacode.evaluation.Evaluator;

import java.util.HashMap;

public class TranspositionTable extends HashMap<Long, TranspositionTable.TableObject> {
    
    TranspositionTable(){}
    
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

        int getScore(int ply) {
            if (score > Evaluator.CHECKMATE_ENEMY_SCORE_MAX_PLY){
                return score - ply;
            }
            else if (score < Evaluator.IN_CHECKMATE_SCORE_MAX_PLY){
                return score + ply;
            }
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
