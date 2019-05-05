package com.github.louism33.axolotl.main;

import com.github.louism33.axolotl.evaluation.Evaluator;
import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.axolotl.search.SearchUtils;
import com.github.louism33.axolotl.transpositiontable.TranspositionTable;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.Perft;
import org.junit.Assert;

import java.io.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.github.louism33.axolotl.evaluation.EvaluationConstants.*;
import static com.github.louism33.axolotl.search.ChessThread.MASTER_THREAD;
import static com.github.louism33.axolotl.search.Engine.sendBestMove;
import static com.github.louism33.axolotl.search.EngineSpecifications.*;
import static com.github.louism33.chesscore.BoardConstants.BLACK;
import static com.github.louism33.chesscore.BoardConstants.WHITE;
import static com.github.louism33.utils.MoveParserFromAN.buildMoveFromLAN;

public final class UCIEntry {

    public Chessboard board = new Chessboard();
    public Chessboard[] boards = {new Chessboard()};
    public Engine engine;
    private int[] searchMoves = new int[8];

    private int lastMoveMade = 0;

    private final BufferedReader input;
    private final PrintStream output;
    private boolean protocolReady = false;
    private boolean inStartPos = true;
    private boolean fenSet = false;
    private int indexOfMoves = 0;

    private final Matcher optionMatcher = Pattern.compile("setoption name (\\w+) value (\\w+)").matcher("");

    public UCIEntry(Engine engine) {
        input = new BufferedReader(new InputStreamReader(System.in));
        output = new PrintStream(System.out);
        this.engine = engine;
        engine.uciEntry = this;
    }

    public UCIEntry() {
        engineThread.start();
        input = new BufferedReader(new InputStreamReader(System.in));
        output = new PrintStream(System.out);
        this.engine = new Engine();
        engine.uciEntry = this;
    }

    private void loop() throws IOException {
        while (true) {
            String line = input.readLine();

            if (line != null) {
                // Try to parse the command.
                String[] tokens = line.trim().split("\\s", 2);

                if (DEBUG) {
                    output.println("info string received " + line);
                }

                while (tokens.length > 0) {

                    final String token = tokens[0];

                    if (token.equalsIgnoreCase("uci")) {
                        output.println("id name axolotl_v1.7");
                        output.println("id author Louis James Mackenzie-Smith");
                        output.println();
                        output.println("option name Hash type spin default 128 min 1 max 1024");
                        output.println("option name Threads type spin default 1 min 1 max " + MAX_THREADS);

                        if (SPSA) {
                            // futility
                            output.println("option name futility1 type spin default " + SearchUtils.futilityMargin[1]
                                    + " min " + (SearchUtils.futilityMargin[1] / 2) + " max " + (SearchUtils.futilityMargin[1] * 2));
                            output.println("option name futility2 type spin default " + SearchUtils.futilityMargin[2]
                                    + " min " + (SearchUtils.futilityMargin[2] / 2) + " max " + (SearchUtils.futilityMargin[2] * 2));
                            output.println("option name futility3 type spin default " + SearchUtils.futilityMargin[3]
                                    + " min " + (SearchUtils.futilityMargin[3] / 2) + " max " + (SearchUtils.futilityMargin[3] * 2));
                            output.println("option name futility4 type spin default " + SearchUtils.futilityMargin[4]
                                    + " min " + (SearchUtils.futilityMargin[4] / 2) + " max " + (SearchUtils.futilityMargin[4] * 2));
                            output.println("option name futility5 type spin default " + SearchUtils.futilityMargin[5]
                                    + " min " + (SearchUtils.futilityMargin[5] / 2) + " max " + (SearchUtils.futilityMargin[5] * 2));

                            //alpha razor
                            output.println("option name alphaRazor1 type spin default " + SearchUtils.alphaRazorMargin[1]
                                    + " min " + (SearchUtils.alphaRazorMargin[1] / 2) + " max " + (SearchUtils.alphaRazorMargin[1] * 2));
                            output.println("option name alphaRazor2 type spin default " + SearchUtils.alphaRazorMargin[2]
                                    + " min " + (SearchUtils.alphaRazorMargin[2] / 2) + " max " + (SearchUtils.alphaRazorMargin[2] * 2));
                            output.println("option name alphaRazor3 type spin default " + SearchUtils.alphaRazorMargin[3]
                                    + " min " + (SearchUtils.alphaRazorMargin[3] / 2) + " max " + (SearchUtils.alphaRazorMargin[3] * 2));

                            // beta razor
                            output.println("option name betaRazor1 type spin default " + SearchUtils.betaRazorMargin[1]
                                    + " min " + (SearchUtils.betaRazorMargin[1] / 2) + " max " + (SearchUtils.betaRazorMargin[1] * 2));
                            output.println("option name betaRazor2 type spin default " + SearchUtils.betaRazorMargin[2]
                                    + " min " + (SearchUtils.betaRazorMargin[2] / 2) + " max " + (SearchUtils.betaRazorMargin[2] * 2));
                            output.println("option name betaRazor3 type spin default " + SearchUtils.betaRazorMargin[3]
                                    + " min " + (SearchUtils.betaRazorMargin[3] / 2) + " max " + (SearchUtils.betaRazorMargin[3] * 2));
                            output.println("option name betaRazor4 type spin default " + SearchUtils.betaRazorMargin[4]
                                    + " min " + (SearchUtils.betaRazorMargin[4] / 2) + " max " + (SearchUtils.betaRazorMargin[4] * 2));
                            output.println("option name betaRazor5 type spin default " + SearchUtils.betaRazorMargin[5]
                                    + " min " + (SearchUtils.betaRazorMargin[5] / 2) + " max " + (SearchUtils.betaRazorMargin[5] * 2));

                            output.println("option name betaRazor6 type spin default " + SearchUtils.betaRazorMargin[6]
                                    + " min " + (SearchUtils.betaRazorMargin[6] / 2) + " max " + (SearchUtils.betaRazorMargin[6] * 2));
                        }

                        output.println("uciok");
                        protocolReady = true;
                    } else if (token.equalsIgnoreCase("debug")) {
                        if (tokens[1].equalsIgnoreCase("on")) {
                            DEBUG = true;
                            if (DEBUG) {
                                output.println("received debug command with value " + DEBUG);
                            }
                        } else {
                            DEBUG = false;
                        }
                        break;

                    } else if (token.equalsIgnoreCase("isready")) {
                        output.println("readyok");
                        break;

                    } else if (token.equalsIgnoreCase("setoption")) {
                        String nameToken = "";
                        String valueToken = "";
                        optionMatcher.reset(line);
                        if (optionMatcher.find()) {
                            nameToken = optionMatcher.group(1);
                            valueToken = optionMatcher.group(2);
                        }

                        if (nameToken.equalsIgnoreCase("Hash")) {
                            int size = DEFAULT_TABLE_SIZE_MB;

                            try {
                                size = Integer.parseInt(valueToken);
                            } catch (Exception | Error e) {
                                output.println("could not read hash value, sticking to " + DEFAULT_TABLE_SIZE_MB + "mb");
                            }

                            if (DEBUG) {
                                output.println("received option Hash with parsed value " + size);
                            }

                            if (size >= MIN_TABLE_SIZE_MB && size <= MAX_TABLE_SIZE_MB) {
                                TABLE_SIZE_MB = size;
                            } else if (size > MAX_TABLE_SIZE_MB) {
                                TABLE_SIZE_MB = MAX_TABLE_SIZE_MB;
                            } else {
                                TABLE_SIZE_MB = MIN_TABLE_SIZE_MB;
                            }

                            TranspositionTable.initTableMegaByte(TABLE_SIZE_MB);

                            // setoption name Threads value 2
                        } else if (nameToken.equalsIgnoreCase("Threads")) {

                            int number = DEFAULT_THREAD_NUMBER;
                            try {
                                number = Integer.parseInt(valueToken);
                            } catch (Exception | Error e) {
                                output.println("could not read threads value, sticking to " + DEFAULT_THREAD_NUMBER + "mb");
                            }

                            if (DEBUG) {
                                output.println("received option Hash with parsed value " + number);
                            }

                            Engine.setThreads(number);
                            boards = new Chessboard[NUMBER_OF_THREADS];

                        } else if (SPSA) {
                            if (nameToken.equalsIgnoreCase("futility1")) {
                                SearchUtils.futilityMargin[1] = Integer.parseInt(valueToken);
                            } else if (nameToken.equalsIgnoreCase("futility2")) {
                                SearchUtils.futilityMargin[2] = Integer.parseInt(valueToken);
                            } else if (nameToken.equalsIgnoreCase("futility3")) {
                                SearchUtils.futilityMargin[3] = Integer.parseInt(valueToken);
                            } else if (nameToken.equalsIgnoreCase("futility4")) {
                                SearchUtils.futilityMargin[4] = Integer.parseInt(valueToken);
                            } else if (nameToken.equalsIgnoreCase("futility5")) {
                                SearchUtils.futilityMargin[5] = Integer.parseInt(valueToken);
                            } else if (nameToken.equalsIgnoreCase("alphaRazor1")) {
                                SearchUtils.alphaRazorMargin[1] = Integer.parseInt(valueToken);
                            } else if (nameToken.equalsIgnoreCase("alphaRazor2")) {
                                SearchUtils.alphaRazorMargin[2] = Integer.parseInt(valueToken);
                            } else if (nameToken.equalsIgnoreCase("alphaRazor3")) {
                                SearchUtils.alphaRazorMargin[3] = Integer.parseInt(valueToken);
                            } else if (nameToken.equalsIgnoreCase("betaRazor1")) {
                                SearchUtils.betaRazorMargin[1] = Integer.parseInt(valueToken);
                            } else if (nameToken.equalsIgnoreCase("betaRazor2")) {
                                SearchUtils.betaRazorMargin[2] = Integer.parseInt(valueToken);
                            } else if (nameToken.equalsIgnoreCase("betaRazor3")) {
                                SearchUtils.betaRazorMargin[3] = Integer.parseInt(valueToken);
                            } else if (nameToken.equalsIgnoreCase("betaRazor4")) {
                                SearchUtils.betaRazorMargin[4] = Integer.parseInt(valueToken);
                            } else if (nameToken.equalsIgnoreCase("betaRazor5")) {
                                SearchUtils.betaRazorMargin[5] = Integer.parseInt(valueToken);
                            } else if (nameToken.equalsIgnoreCase("betaRazor6")) {
                                SearchUtils.betaRazorMargin[6] = Integer.parseInt(valueToken);
                            }
                        }
                        break;
                    } else if (token.equalsIgnoreCase("register")) {
                        break;

                    } else if (token.equalsIgnoreCase("ucinewgame")) {
                        board = new Chessboard();
                        lastMoveMade = 0;
                        Engine.resetFull();
                        inStartPos = true;
                        fenSet = false;
                        break;

                    } else if (token.equalsIgnoreCase("position")) {

                        String[] list = (tokens[1]).split("\\s");
                        final int length = list.length;
                        if (list[0].equalsIgnoreCase("startpos")) {
                            if (!inStartPos) {
                                board = new Chessboard();
                                inStartPos = true;
                                lastMoveMade = 0;
                            }
                            // position startpos moves e2e4 e7e5 d2d4
                            // todo add trim for extra spaces which currently break everything
                            if (length != 1 && list[1].equalsIgnoreCase("moves")) {
                                for (int s = 2 + lastMoveMade; s < length; s++) {
                                    final int move = buildMoveFromLAN(board, list[s].trim());
                                    board.makeMoveAndFlipTurn(move);
                                    lastMoveMade++;
                                }
                            }

                        }
                        // position fen rnbqkbnr/pp1ppp1p/6p1/2p5/1P6/1QP5/P2PPPPP/RNB1KBNR b KQkq - 0 1;
                        // position fen 3rk2r/1pR2p2/b2BpPp1/p2p4/8/1P6/P4PPP/4R1K1 w - - 1 0 moves h2h3
                        // position fen 3rk2r/1pR2p2/b2BpPp1/p2p4/8/1P6/P4PPP/4R1K1 w - - 1 0 moves h2h3 h8h7 a2a3 d8c8
                        // position fen rnbqkb1r/1ppppppp/7n/p7/P7/6PP/1PPPPP2/RNBQKBNR b KQkq - 0 1 moves  d7d5 g1f3 b8c6 d2d4 h6f5 b1c3
                        // position fen rnbqkbnr/ppp2ppp/3p4/4p3/8/3P2P1/PPPBPP1P/RN1QKBNR b KQkq - 0 1 moves  b8c6 g1f3 d6d5 e2e4 d5e4 

                        else if (list[0].equalsIgnoreCase("fen")) {
                            inStartPos = false;
                            if (!fenSet) {
                                StringBuilder fen = new StringBuilder();
                                for (int i = 1; i < list.length; i++) {
                                    final String str = list[i];
                                    if (str.equalsIgnoreCase("moves")) {
                                        indexOfMoves = i;
                                        break;
                                    }
                                    fen.append(str).append(" ");
                                }

                                board = new Chessboard(fen.toString().trim());
                                fenSet = true;
                                lastMoveMade = 0;
                            } else {
                                for (int i = 1; i < list.length; i++) {
                                    final String str = list[i];
                                    if (str.equalsIgnoreCase("moves")) {
                                        indexOfMoves = i;
                                        break;
                                    }
                                }
                            }

                            if (length != indexOfMoves && list[indexOfMoves].equalsIgnoreCase("moves")) {
                                for (int s = indexOfMoves + 1 + lastMoveMade; s < length; s++) {
                                    if (list[s].trim().equals("")) {
                                        continue;
                                    }
                                    int move = 0;

                                    try {
                                        move = buildMoveFromLAN(board, list[s].trim());
                                    } catch (Exception | Error e) {
                                        System.err.println("LAN PROBLEM: ");
                                        System.err.println(board);
                                        System.err.println("all received: ");
                                        System.err.println(line);
                                        System.err.println("and i try to parse");
                                        System.err.println(Arrays.toString(list));
                                        System.err.println(list[s]);
                                        System.err.println(list[s].trim());
                                    }
                                    board.makeMoveAndFlipTurn(move);
                                    lastMoveMade++;
                                }
                            }
                        }

                        Assert.assertNotNull(boards);
                        Assert.assertEquals(boards.length, NUMBER_OF_THREADS);
                        for (int i = 0; i < boards.length; i++) { // todo, cheaper to increment all, or clone all?
                            boards[i] = new Chessboard(board);
                        }

                        if (DEBUG) {
                            output.println("board: ");
                            output.println(board);
                        }


                        break;
                    } else if (token.equalsIgnoreCase("go")) {
                        String[] list = (tokens[1]).split("\\s");
                        final int length = list.length;

                        int depth = 0;
                        boolean infinite = false;
                        boolean mate = false;
                        int nodes = 0;

                        long moveTime = 0;

                        long myTime = 0;
                        long enemyTime = 0;
                        long myinc = 0;
                        long enemyinc = 0;
                        int movestogo = 0;

                        final int turn = board.turn;
                        for (int s = 0; s < length; s++) {
                            String nextToken = list[s];

                            nextToken = nextToken.trim();
                            if (nextToken.equalsIgnoreCase("searchmoves")) {
                                s++;
                                final int numberOfMoves = length - s;
                                if (searchMoves.length < numberOfMoves + 1) {
                                    searchMoves = new int[numberOfMoves + 1];
                                }

                                for (int i = 0; i < numberOfMoves; i++) {
                                    searchMoves[i] = buildMoveFromLAN(board, list[i + s]);
                                }
                                searchMoves[searchMoves.length - 1] = numberOfMoves;

                                s += numberOfMoves;
                            } else if (nextToken.equalsIgnoreCase("wtime")) {
                                if (turn == WHITE) {
                                    myTime = Long.parseLong(list[s + 1]);
                                } else {
                                    enemyTime = Long.parseLong(list[s + 1]);
                                }
                            } else if (nextToken.equalsIgnoreCase("btime")) {
                                if (turn == BLACK) {
                                    myTime = Long.parseLong(list[s + 1]);
                                } else {
                                    enemyTime = Long.parseLong(list[s + 1]);
                                }
                            } else if (nextToken.equalsIgnoreCase("winc")) {
                                if (turn == WHITE) {
                                    myinc = Long.parseLong(list[s + 1]);
                                } else {
                                    enemyinc = Long.parseLong(list[s + 1]);
                                }
                            } else if (nextToken.equalsIgnoreCase("binc")) {
                                if (turn == BLACK) {
                                    myinc = Long.parseLong(list[s + 1]);
                                } else {
                                    enemyinc = Long.parseLong(list[s + 1]);
                                }
                            } else if (nextToken.equalsIgnoreCase("movetime")) {
                                moveTime = Long.parseLong(list[s + 1]);
                            } else if (nextToken.equalsIgnoreCase("movestogo")) {
                                movestogo = Integer.parseInt(list[s + 1]);
                            } else if (nextToken.equalsIgnoreCase("depth")) {
                                depth = Integer.parseInt(list[s + 1]);
                            } else if (nextToken.equalsIgnoreCase("nodes")) {
                                nodes = Integer.parseInt(list[s + 1]);
                            } else if (nextToken.equalsIgnoreCase("mate")) {
                                mate = true;
                            } else if (nextToken.equalsIgnoreCase("infinite")) {
                                infinite = true;
                            }

                        }

                        // go movetime 1000 searchmoves e2e4 d2d4 a2a3
                        // go wtime 919 btime 825 winc 50 binc 50

                        MAX_DEPTH = ABSOLUTE_MAX_DEPTH;

                        Engine.quitOnSingleMove = true;
                        if (searchMoves[0] != 0) {
                            Engine.quitOnSingleMove = false;
                            Engine.computeMoves = false;
                            Engine.rootMoves[MASTER_THREAD] = searchMoves;
                        }

                        // hack to avoid needing to type in "position startpos" everytime
                        if (boards == null) {
                            boards = new Chessboard[NUMBER_OF_THREADS];
                        }
                        if (boards[MASTER_THREAD] == null) {
                            for (int i = 0; i < NUMBER_OF_THREADS; i++) {
                                boards[i] = new Chessboard(board);
                            }
                        }

                        if (depth != 0) {
                            engine.receiveSearchSpecs(boards, depth, 0, false, 0, 0, 0, 0);
                        } else if (nodes != 0) {
//                            engine.receiveSearchSpecs(boards, depth, 0, false, 0, 0, 0, 0);
                        } else if (mate) {
                            engine.receiveSearchSpecs(boards, MAX_DEPTH, 0, false, 0, 0, 0, 0);
                        } else if (infinite) {
                            engine.receiveSearchSpecs(boards, MAX_DEPTH, 0, false, 0, 0, 0, 0);
                        } else if (moveTime != 0) {
                            engine.receiveSearchSpecs(boards, 0, moveTime, false, 0, 0, 0, 0);
                        } else {
                            engine.receiveSearchSpecs(boards, 0, 0, true, myTime, enemyTime, myinc, movestogo);
                        }

                        if (DEBUG) {
                            output.println("info string engine go command for board: ");
                            output.println(board);
                        }

                        sendBestMove = true;

                        searching = true;
//                        engine.go();
                        synchronized (synchronizedObject) {
                            synchronizedObject.notifyAll();
                        }

                        break;

                    } else if (token.equalsIgnoreCase("stop")) {
                        int aiMove = Engine.getAiMove();
                        if (aiMove != 0) {
                            output.println("bestmove " + MoveParser.toString(aiMove)); // + ponder
                        }
                        reset();
                        Engine.stopNow = true;
                        break;
                    } else if (token.equalsIgnoreCase("ponderhit")) {
                        output.println("I don't know how to ponder yet sorry");
                        break;
                    } else if (token.equalsIgnoreCase("quit")) {
                        System.exit(1);
                        break;
                    } else if (token.equalsIgnoreCase("e")) {
                        output.println(Evaluator.stringEval(board, board.turn));
                    } else if (token.equalsIgnoreCase("d")) {
                        output.println(board);
                    } else if (token.equalsIgnoreCase("tofen")) {
                        output.println(board.toFenString());
                    } else if (token.equalsIgnoreCase("perft")) {
                        try {
                            final int d = Integer.parseInt(tokens[1]);
                            output.println("Performing perft to depth " + d);
                            long t1 = System.currentTimeMillis();
                            long nodes = Perft.perftTest(board, d);
                            long t2 = System.currentTimeMillis();
                            long t = t2 - t1;
                            output.println(board);
                            output.println("depth: " + d + ", total leaf nodes: " + nodes);
                            if (t > 1000) {
                                output.println("Time: " + t + " millis, NPS: " + ((nodes / t) * 1000));
                            }

                        } catch (Exception | Error e) {
                            output.println("didn't understand perft command");
                        }
                    }

                    if (tokens.length > 1) {
                        tokens = tokens[1].trim().split("\\s", 2);
                    } else {
                        break;
                    }
                }

            } else {
                // Something's wrong with the communication channel
                throw new EOFException();
            }
        }
    }

    private void reset() {
        Arrays.fill(searchMoves, 0);
        Engine.quitOnSingleMove = true;
        Engine.computeMoves = true;
        MAX_DEPTH = ABSOLUTE_MAX_DEPTH;
    }

    public void sendNoMove() {
        output.println("bestmove (none)");
        reset();
    }

    public void sendBestMove(int aiMove) {
        output.println("bestmove " + MoveParser.toString(aiMove));
        reset();
    }

    private static boolean mateFound(int score) {
        return score >= CHECKMATE_ENEMY_SCORE_MAX_PLY || score <= IN_CHECKMATE_SCORE_MAX_PLY;
    }

    private static int distanceToMate(int score) {
        System.out.println(score);
        return score > 0 ? (CHECKMATE_ENEMY_SCORE - score + 1) / 2 : (IN_CHECKMATE_SCORE - score) / 2;
    }

    public void send(Chessboard board, int aiMoveScore, int depth, int seldepth, long time, long nodes) {
        String infoCommand = "info";

        if (depth != 0) {
            infoCommand += " depth " + depth;
        }

        infoCommand += " seldepth " + seldepth;

        infoCommand += " multipv 1";

        if (mateFound(aiMoveScore)) {
            infoCommand += " score mate " + distanceToMate(aiMoveScore);
        } else {
            infoCommand += " score cp " + aiMoveScore;
        }

        Engine.calculateNPS();
        infoCommand += " nodes " + Engine.totalMovesMade;
        infoCommand += " nps " + Engine.nps;
        infoCommand += " tbhits 0";
        infoCommand += " time " + time;

        infoCommand += " pv ";
        final int[] pv = PVLine.getPV(board);
        infoCommand += MoveParser.toPVString(pv);

        output.println(infoCommand);
    }


    public final static Object synchronizedObject = new Object();
    public final EngineThread engineThread = new EngineThread(this);
    public static boolean searching = false;


    public static void main(String[] args) throws IOException {
        System.out.println("axolotl v1.7 by Louis James Mackenzie-Smith");
        UCIEntry uci = new UCIEntry();
        uci.loop();
    }


}
