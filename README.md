# Axolotl
## Though she be but little, she is fierce
UCI compliant chess engine written in Java. Can be played on the command line, but best connected to a GUI, such as arena.
Uses bitboards to represent the main aspects on the game. Performs fully legal move generation, and encodes moves as integers. 
Also includes various Parsers for FEN notation, EDP and Algebraic notation. Various tests are also included, both for the game logic (perft tests) and the engine itself (WAC tests, Strategic Test Suite etc). 
Many options for improvement and speedup exist, this is only version one. 
As external libraries, JCPI is used for the UCI protocol.
