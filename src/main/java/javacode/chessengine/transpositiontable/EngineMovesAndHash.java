package javacode.chessengine.transpositiontable;

import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;

import static javacode.chessprogram.moveMaking.MoveOrganiser.flipTurn;
import static javacode.chessprogram.moveMaking.MoveOrganiser.makeMoveMaster;
import static javacode.chessprogram.moveMaking.MoveUnmaker.unMakeMoveMaster;

public class EngineMovesAndHash {
    
    public static void makeMoveAndHashUpdate(Chessboard board, Move move, ZobristHash zobristHash){
        zobristHash.zobristStack.push(zobristHash.getBoardHash());
        zobristHash.updateHashPreMove(board, move);
        makeMoveMaster(board, move);
        flipTurn(board);
        zobristHash.updateHashPostMove(board, move);
    }

    public static void UnMakeMoveAndHashUpdate(Chessboard board, ZobristHash zobristHash){
        zobristHash.setBoardHash(zobristHash.zobristStack.pop());
        unMakeMoveMaster(board);
    }

    public static void makeNullMove(Chessboard board, ZobristHash zobristHash){
        zobristHash.zobristStack.push(zobristHash.getBoardHash());
        if (board.moveStack.size() > 0) {
            zobristHash.updateWithEPFlags(board);
        }
        zobristHash.setBoardHash(zobristHash.getBoardHash() ^ ZobristHash.zobristHashColourBlack);
        flipTurn(board);
    }

    public static void unMakeNullMove(Chessboard board, ZobristHash zobristHash){
        zobristHash.setBoardHash(zobristHash.zobristStack.pop());
        flipTurn(board);
    }
}
