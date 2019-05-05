package com.github.louism33.axolotl.main;

import static com.github.louism33.axolotl.main.UCIEntry.searching;
import static com.github.louism33.axolotl.main.UCIEntry.synchronizedObject;

public final class EngineThread extends Thread {


    UCIEntry uciEntry;

    EngineThread(UCIEntry uciEntry) {
        this.uciEntry = uciEntry;
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
                uciEntry.engine.go();
                searching = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
