package standalone;

import com.github.louism33.axolotl.search.Engine;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;

import java.io.IOException;
import java.io.InputStreamReader;

class StandAlone {

    private static int totalMoves = 1;
    private static final long timeLimit = 5000;

    public static void main(String[] args) throws IOException {
        InputStreamReader stdin;

        stdin = new InputStreamReader(System.in);

        Chessboard board = new Chessboard();
        String command, prompt;
        int move;

        while(true) {
            while (true) {

                if (board.isWhiteTurn()){
                    prompt = "White"; 
                }
                else {
                    prompt = "Black";
                }

                System.out.println("\nPosition ("+prompt+" to move):\n" + board);

                int[] moves = board.generateLegalMoves();

                if (moves.length == 0) {
                    if (board.inCheck(board.isWhiteTurn())){
                        System.out.println("Checkmate");
                    }
                    else {
                        System.out.println("Stalemate");
                    }
                    break;
                }

                System.out.println("Moves:");
                System.out.print("   ");

                final String[] niceMoves = MoveParser.toString(moves);
                for (int i = 0; i< niceMoves.length; i++) {
                    if ((i % 10) == 0 && i>0) System.out.print("\n   ");
                    System.out.print(niceMoves[i]+", ");
                }

                System.out.println();
                System.out.println(MoveParser.numberOfRealMoves(moves) +  " moves in total.");
                System.out.println();

                label:
                while (true) {
                    System.out.print(prompt + " move (or \"go\" or \"quit\")> ");
                    command = readCommand(stdin);
                    System.out.println("This is move number " + totalMoves + '.');

                    switch (command) {
                        case "go":
                            move = Engine.searchFixedTime(board, timeLimit);
                            break label;
                        case "quit":
                            System.out.println("QUIT.\n");
                            System.exit(1);
                        default:
                            move = 0;
                            for (int m : moves) {
                                if (command.equals(MoveParser.toString(m))) {
                                    move = m;
                                    break;
                                }
                            }
                            if (move != 0) break label;
                            System.out.println('"' + command + "\" is not a legal move");
                            break;
                    }
                }

                board.makeMoveAndFlipTurn(move);
                totalMoves++;
                System.out.println(prompt + " made move " + MoveParser.toString(move));
            }



            while(true) {
                System.out.print("Play again? (y/n):");
                command = readCommand(stdin);
                if (command.equals("n")) System.exit(1);
                if (command.equals("y")) {
                    totalMoves = 0;
                    break;
                }
            }
        }
    }
    private static String readCommand(InputStreamReader stdin) throws IOException {
        int MAX = 100;
        int len = 0;
        char[] cbuf = new char[MAX];
        //len = stdin.read(cbuf, 0, MAX);
        for(int i=0; i<cbuf.length; i++){

            cbuf[i] = (char)stdin.read();
            len++;
            if(cbuf[i] == '\n')
                break;
            if(cbuf[i] == -1){
                System.out.println("An error occurred reading input");
                System.exit(1);
            }
        }

		/*if (len == -1){
            System.out.println("An error occurred reading input");
            System.exit(1);
        }*/
        return new String(cbuf, 0, len).trim();  /* trim() removes \n in unix and \r\n in windows */
    }
}