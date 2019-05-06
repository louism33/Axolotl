package com.github.louism33.axolotl.main;

import com.github.louism33.chesscore.Chessboard;

import static com.github.louism33.axolotl.main.UCIEntry.searching;
import static com.github.louism33.axolotl.main.UCIEntry.synchronizedObject;

public final class EngineThread extends Thread {

    Chessboard board;
    UCIEntry uciEntry;

    EngineThread(UCIEntry uciEntry) {
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
                
                uciEntry.engine.go(this.board);
                
                searching = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
