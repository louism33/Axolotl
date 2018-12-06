package com.github.louism33.axolotl.transpositiontable;

import java.util.HashMap;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.CHECKMATE_ENEMY_SCORE_MAX_PLY;
import static com.github.louism33.axolotl.evaluation.EvaluationConstants.IN_CHECKMATE_SCORE_MAX_PLY;

public class TranspositionTable extends HashMap<Long, TranspositionTable.TableObject> {

    public TranspositionTable(){}
    
    public static class TableObject {
        private final int move;
        private final int score;
        private final int depth;
        private final Flag flag;
        
        public enum Flag {
                EXACT, LOWERBOUND, UPPERBOUND
        }

        public TableObject(int move, int score, int depth, Flag flag) {
            this.move = move;
            this.score = score;
            this.depth = depth;
            this.flag = flag;
        }

        public int getMove() {
            return move;
        }

        public int getScore(int ply) {
            if (score > CHECKMATE_ENEMY_SCORE_MAX_PLY){
                return score - ply;
            }
            else if (score < IN_CHECKMATE_SCORE_MAX_PLY){
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
