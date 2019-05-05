package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.main.UCIEntry;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MaterialHashUtil;
import org.junit.Assert;

import static com.github.louism33.axolotl.search.Engine.search;
import static com.github.louism33.axolotl.search.EngineSpecifications.MASTER_DEBUG;

public final class ChessThread extends Thread {
    public static final int MASTER_THREAD = 0;

    private UCIEntry uciEntry = null;
    private int whichThread;
    Chessboard board;

    ChessThread(int whichThread, Chessboard board) {
        Assert.assertTrue(whichThread != MASTER_THREAD);
        this.whichThread = whichThread;
        this.board = board;
    }

    ChessThread(UCIEntry uciEntry, Chessboard board) {
        this.uciEntry = uciEntry;
        this.whichThread = MASTER_THREAD;
        this.board = board;
    }


    @Override
    public void run() {
        if (EngineSpecifications.DEBUG) {
            System.out.println("    start run of " + this.getName() + " with thread number " + whichThread);
        }

        if (MASTER_DEBUG) {
            Assert.assertEquals(MaterialHashUtil.makeMaterialHash(board), board.materialHash);
            Assert.assertEquals(MaterialHashUtil.typeOfEndgame(board), board.typeOfGameIAmIn);
        }

        search(board, uciEntry, whichThread);

        
        if (EngineSpecifications.DEBUG) {
            System.out.println("    stop run of " + this.getName() + " with thread number " + whichThread);
        }

    }
    
    
    
}
