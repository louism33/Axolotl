package com.github.louism33.axolotl.main;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.EngineSpecifications;
import com.github.louism33.axolotl.search.SearchSpecs;
import com.github.louism33.chesscore.Chessboard;
import org.junit.Assert;

import static com.github.louism33.axolotl.main.UCIEntry.searching;
import static com.github.louism33.axolotl.main.UCIEntry.synchronizedObject;

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

                Assert.assertTrue(searching == false);
                Assert.assertTrue(Engine.stopNow == false);
                Assert.assertTrue(SearchSpecs.searchType != SearchSpecs.SEARCH_TYPE.NONE);
                Assert.assertEquals(0, Engine.threadsNumber.get());
                
                if (EngineSpecifications.DEBUG) {
                    System.out.println(this.getName() + " starting engine main search");
                }

                uciEntry.engine.go(this.board);

                if (EngineSpecifications.DEBUG) {
                    System.out.println(this.getName() + " has completed engine main search");
                }
                
                searching = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
