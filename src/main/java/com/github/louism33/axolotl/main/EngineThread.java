package com.github.louism33.axolotl.main;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.axolotl.search.SearchSpecs;
import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import static com.github.louism33.axolotl.main.UCIEntry.searching;
import static com.github.louism33.axolotl.main.UCIEntry.synchronizedObject;
import static com.github.louism33.axolotl.search.ChessThread.MASTER_THREAD;
import static com.github.louism33.axolotl.search.EngineSpecifications.MASTER_DEBUG;
import static com.github.louism33.axolotl.search.EngineSpecifications.sendBestMove;
import static com.github.louism33.chesscore.MoveConstants.MOVE_MASK_WITHOUT_CHECK;

@SuppressWarnings("ALL")
public final class EngineThread extends Thread {

    Chessboard board;
    UCIEntry uciEntry;

    EngineThread(UCIEntry uciEntry) {
        this.setName("EngineThread");
        this.uciEntry = uciEntry;
    }

    public void setBoard(Chessboard board) {
        this.board = board;
    }

    @Override
    public void run() {

        while (true) {
            try {
                synchronized (synchronizedObject) {
                    while (!searching) {
                        synchronizedObject.wait();
                    }
                }

                if (MASTER_DEBUG) {
                    Assert.assertTrue(searching == true);
                    Assert.assertTrue(SearchSpecs.searchType != SearchSpecs.SEARCH_TYPE.NONE);
                    Assert.assertEquals(0, Engine.threadsNumber.get());
                    Assert.assertTrue(Engine.weHavePanicked == false);
                }

                if (EngineSpecifications.DEBUG) {
                    System.out.println("info string -----> " + this.getName() + " starting engine main search");
                }

                uciEntry.engine.go(this.board);

                searching = false;

                if (EngineSpecifications.DEBUG) {
                    System.out.println("info string <----- " + this.getName() + " has completed engine main search");
                }

                Assert.assertTrue(Engine.running == false);
                Assert.assertEquals(0, Engine.threadsNumber.get());

                final int bestMove = Engine.rootMoves[MASTER_THREAD][0] & MOVE_MASK_WITHOUT_CHECK;

                if (sendBestMove) {
                    uciEntry.sendBestMove(bestMove);
                }

            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

    }
}
