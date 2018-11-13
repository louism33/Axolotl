# Axolotl
## Though she be but little, she is fierce
UCI compliant chess engine written in Java. Can be played on the command line, but best connected to a GUI, such as arena.
Uses bitboards to represent the main aspects on the game. Performs fully legal move generation, and encodes moves as integers. 
Also includes various Parsers for FEN notation, EDP and Algebraic notation. Various tests are also included, both for the game logic (perft tests) and the engine itself (WAC tests, Strategic Test Suite etc). 
Many options for improvement and speedup exist, this is only version one. 
As external libraries, JCPI is used for the UCI protocol.

The JAR file is targetted to Java 8, but the project is written under Java 10. 

For information about the UCI protocol, please refer to: 
  http://wbec-ridderkerk.nl/html/UCIProtocol.html
  https://chess.stackexchange.com/questions/12580/working-with-uci-protocol-coding
  
For connection to Arena assistance, in particular how to make the Axolotl_jar executable, this might be useful:
  https://coderwall.com/p/ssuaxa/how-to-make-a-jar-file-linux-executable
  
You are free to use Axolotl however you see fit. If you have any questions, simply open an issue and I will try to get back to you.

Louis
 
  
