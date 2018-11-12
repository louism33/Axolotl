package javacode.chessengine;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import java.util.ArrayList;
import java.util.List;

import static javacode.chessengine.EngineMovesAndHash.makeMoveAndHashUpdate;

public class PVLine {
    
    private int score;
    private List<Move> pvMoves;

    public PVLine(int score, List<Move> pvMoves) {
        this.score = score;
        this.pvMoves = pvMoves;
    }

    @Override
    public String toString() {
        return "PVLine{" +
                "score=" + score +
                ", pvMoves=" + pvMoves +
                '}';
    }

    public static PVLine retrievePVfromTable(Chessboard board, TranspositionTable table){
        List<Move> moves = new ArrayList<>();
        int i = 1;
        ZobristHash zobristHash = new ZobristHash(board);
        int nodeScore = 0;

        while(i < 50) {
            TranspositionTable.TableObject tableObject = table.get(zobristHash.getBoardHash());
            if (tableObject == null) {
                break;
            }
            int score = tableObject.getScore(i-1);
            if (i == 1){
                nodeScore = score;
            }
            Move move = tableObject.getMove();
            moves.add(move);
            makeMoveAndHashUpdate(board, move, zobristHash);
            i++;
        }

        for (int x = 0; x < i-1; x++){
            EngineMovesAndHash.UnMakeMoveAndHashUpdate(board, zobristHash);
        }

        return new PVLine(nodeScore, moves);
    }


    public int getScore() {
        return score;
    }

    public List<Move> getPvMoves() {
        return pvMoves;
    }
}
