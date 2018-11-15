package javacode.chessprogram.miscAdmin;

import javacode.chessengine.Engine;
import javacode.chessprogram.chess.Chessboard;
import javacode.chessprogram.chess.Move;
import javacode.chessprogram.moveMaking.MoveOrganiser;
import javacode.graphicsandui.Art;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtendedPositionDescriptionParser {
    
    public static EPDObject parseEDPPosition(String edpPosition){
        
        String id = extractIDString(edpPosition);
        
        String[] bms = extractBestMoves(edpPosition);

        Chessboard chessboard = FenParser.makeBoardBasedOnFEN(edpPosition);
        
        String boardFen = extractBoardFen(edpPosition);

        List<Integer> goodDestinations = new ArrayList<>();
        for (String bm : bms) {
            goodDestinations.add(MoveParserFromAN.destinationIndex(chessboard, bm));
        }


        String[] ams = extractAvoidMoves(edpPosition);
        List<Integer> badDestinations = new ArrayList<>();
        for (String am : ams) {
            badDestinations.add(MoveParserFromAN.destinationIndex(chessboard, am));
        }

        return new EPDObject(chessboard, goodDestinations, id, boardFen, badDestinations);
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

    private static String[] extractBestMoves(String edpPosition){
        String pattern = "bm ([\\w| |+]*);";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(edpPosition);

        String ans = "";

        if (m.find()){
            ans = m.group(1);
        }

        String[] s = ans.split(" ");
        return s;
    }

    private static String[] extractAvoidMoves(String edpPosition){
        String bool = "am .*";
        Pattern p1 = Pattern.compile(bool);
        Matcher m1 = p1.matcher(edpPosition);
        
        
        if (!m1.find()){
            return new String[0];
        }

        
        String pattern = "am ([\\w| |+]*);";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(edpPosition);

        String ans = "";

        if (m.find()){
            ans = m.group(1);
        }

        String[] s = ans.split(" ");
        return s;
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
    
    public static class EPDObject {
        private final Chessboard board;
        private final List<Integer> bestMoveDestinationIndex;
        private final List<Integer> avoidMoveDestinationIndex;
        private final String id;
        private final String boardFen;

        EPDObject(Chessboard board, List<Integer> bestMoveDestinationIndex, String id,
                  String boardFen, List<Integer> avoidMoveDestinationIndex) {
            this.board = board;
            this.bestMoveDestinationIndex = bestMoveDestinationIndex;
            this.id = id;
            this.boardFen = boardFen;
            this.avoidMoveDestinationIndex = avoidMoveDestinationIndex;
        }

        public Chessboard getBoard() {
            return board;
        }

        public List<Integer> getBestMoveDestinationIndex() {
            return bestMoveDestinationIndex;
        }

        public String getId() {
            return id;
        }

        public List<Integer> getAvoidMoveDestinationIndex() {
            return avoidMoveDestinationIndex;
        }

        public String getBoardFen() {
            return boardFen;
        }

        @Override
        public String toString() {
            return "EPDObject{" +
                    "bestMoveDestinationIndex=" + bestMoveDestinationIndex +
                    ", id='" + id + '\'' +
                    ", boardFen='" + boardFen + '\'' +
                    '}';
        }
    }
    
}
