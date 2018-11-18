# Axolotl
## Though she be but little, she is fierce
UCI compliant chess engine written in Java. Can be played on the command line through the UCI protocol, but best connected to a GUI, such as arena. There is also a standalone class, which is not contained in the JAR file.
Uses bitboards to represent the main aspects on the game. Performs fully legal move generation, and encodes moves as integers. 
Also includes various Parsers for FEN notation, EDP and Algebraic notation. Various tests are also included, both for the game logic (perft tests) and the engine itself (WAC tests, Strategic Test Suite etc). 
Many options for improvement and speedup exist, this is only version one. 
As external libraries, JCPI is used for the UCI protocol.

There is support for Blitz tournaments, fixed time per move, and "infinite" depth. Other, less common modes will be implemented in later versions.

You will need the latest JRE, JRE 11 to run Axolotl.

For information about the UCI protocol, please refer to: 
  http://wbec-ridderkerk.nl/html/UCIProtocol.html
  https://chess.stackexchange.com/questions/12580/working-with-uci-protocol-coding
    
You are free to use Axolotl however you see fit. If you have any questions, simply open an issue and I will try to get back to you.

Louis
 
