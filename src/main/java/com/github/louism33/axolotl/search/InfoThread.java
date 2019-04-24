package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.main.UCIEntry;
import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import static com.github.louism33.axolotl.search.EngineSpecifications.PRINT_PV;

public final class InfoThread extends Thread {

    InfoThread infoThread = null;
    private UCIEntry uciEntry;
    Chessboard board;

    public InfoThread getInstance(UCIEntry uciEntry, Chessboard board) {
        if (infoThread == null) {
            infoThread = new InfoThread();
        }

        infoThread.uciEntry = uciEntry;
        infoThread.board = board;

        return infoThread;
    }

    private InfoThread() {
    }


    @Override
    public void run() {
        Assert.assertTrue(PRINT_PV);
//        uciEntry.send(board);

    }
}
