package javacode.chessprogram.miscAdmin;

import javacode.chessprogram.chess.Chessboard;
import javacode.graphicsandui.Art;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtendedPositionDescriptionParser {


    // todo
    // Currently no support for avoid moves
    // add castling 0-0 and 0-0-0
    
    ExtendedPositionDescriptionParser(){

        EDPObject edpObject1 = parseEDPPosition("rn1qkb1r/pp2pppp/5n2/3p1b2/3P4/2N1P3/PP3PPP/R1BQKBNR w KQkq - 0 1 id \"CCR01\"; bm Qb3;");
        EDPObject edpObject = parseEDPPosition("1rbq1rk1/p1b1nppp/1p2p3/8/1B1pN3/P2B4/1P3PPP/2RQ1R1K w - - bm Nf6+; id \"position 01\";");

        System.out.println(edpObject.getId());
        System.out.println(edpObject.bestMoveDestinationIndex);
        System.out.println(Art.boardArt(edpObject.getBoard()));
    }
    
    public static EDPObject parseEDPPosition(String edpPosition){
        System.out.println(edpPosition);
        
        String id = extractIDString(edpPosition);
        
        String bm = extractBestMove(edpPosition);

        Chessboard chessboard = FenParser.makeBoardBasedOnFEN(edpPosition);
        
        String boardFen = extractBoardFen(edpPosition);

        int destinationI = MoveParserFromAN.destinationIndex(chessboard, bm);

        return new EDPObject(chessboard, destinationI, id, boardFen);
    }

    private static String extractBoardFen(String edpPosition){
        String pattern = "[/|\\w]* ";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(edpPosition);

        String ans = "";

        if (m.find()){
            ans = m.group();
        }
        return ans;
    }

    private static String extractBestMove(String edpPosition){
        String pattern = "bm (\\w*)";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(edpPosition);

        String ans = "";
        
        if (m.find()){
            ans = m.group(1);
        }
        return ans;
    }
    
    private static String extractIDString(String edpPosition){
        String pattern = "id \\\"(\\w*[\\.+| +\\w]*)\\\"";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(edpPosition);

        String ans = "";

        if (m.find()){
            ans = m.group(1);
        }
        return ans;
    }
    
    
    
    
    
    
    public static class EDPObject{
        private final Chessboard board;
        private final int bestMoveDestinationIndex;
        private final String id;
        private final String boardFen;

        EDPObject(Chessboard board, int bestMoveDestinationIndex, String id, String boardFen) {
            this.board = board;
            this.bestMoveDestinationIndex = bestMoveDestinationIndex;
            this.id = id;
            this.boardFen = boardFen;
        }

        public Chessboard getBoard() {
            return board;
        }

        public int getBestMoveDestinationIndex() {
            return bestMoveDestinationIndex;
        }

        public String getId() {
            return id;
        }

        @Override
        public String toString() {
            return "EDPObject{" +
                    "bestMoveDestinationIndex=" + bestMoveDestinationIndex +
                    ", id='" + id + '\'' +
                    ", boardFen='" + boardFen + '\'' +
                    '}';
        }
    }
    
}
