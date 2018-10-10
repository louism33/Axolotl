package moveGeneration;

import chess.Chessboard;

import static bitboards.BitBoards.*;

public class PinnedManager {

    public static long whichPiecesArePinned(Chessboard board, boolean white, long squareOfInterest){
        if (squareOfInterest == 0) {
            return 0;
        }
        long pinnedPieces = 0;
        pinnedPieces |= diagonalPins(board, white, squareOfInterest);
        pinnedPieces |= cardinalPins(board, white, squareOfInterest);
        return pinnedPieces;
    }


    static long diagonalPins(Chessboard board, boolean white, long squareOfInterest) {
        long ALL_PIECES = board.ALL_WHITE_PIECES() | board.ALL_BLACK_PIECES();
        long myPieces = (white) ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES();
        long enemyPieces = (!white) ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES();
        long diagonalThreats = (white) ? (board.BLACK_BISHOPS | board.BLACK_QUEEN) : (board.WHITE_BISHOPS | board.WHITE_QUEEN);
        long diagonalPinnedPieces = 0;

        long temp = squareOfInterest;
        thisDirection:
        while (true) {
            if ((temp & NORTH_WEST) != 0) {
                break;
            }
            temp <<= 9;
            if ((temp & myPieces) != 0) {
                long possiblePin = temp;
                while (true) {
                    if ((temp & NORTH_WEST) != 0) {
                        break;
                    }
                    temp <<= 9;
                    if ((temp & diagonalThreats) != 0) {
                        diagonalPinnedPieces |= possiblePin;
                        break thisDirection;
                    }
                    // end loop if encounter a non diagonal / cardinal pinner piece, or Another friendly piece
                    if ((temp & enemyPieces) != 0) { // these two can be combined
                        break thisDirection;
                    }
                    if ((temp & myPieces) != 0) {
                        break thisDirection;
                    }
                }
            }
            // end loop if encounter a non diagonal / cardinal pinner piece
            if ((temp & enemyPieces) != 0) {
                break;
            }
        }

        temp = squareOfInterest;
        thisDirection:
        while (true) {
            if ((temp & NORTH_EAST) != 0) {
                break;
            }
            temp <<= 7;
            if ((temp & myPieces) != 0) {
                long possiblePin = temp;
                while (true) {
                    if ((temp & NORTH_EAST) != 0) {
                        break;
                    }
                    temp <<= 7;
                    if ((temp & diagonalThreats) != 0) {
                        diagonalPinnedPieces |= possiblePin;
                        break thisDirection;
                    }
                    if ((temp & enemyPieces) != 0) {
                        break thisDirection;
                    }
                    if ((temp & myPieces) != 0) {
                        break thisDirection;
                    }
                }
            }
            if ((temp & enemyPieces) != 0) {
                break;
            }
        }

        temp = squareOfInterest;
        thisDirection:
        while (true) {
            if ((temp & SOUTH_WEST) != 0) {
                break;
            }
            temp >>>= 7;
            if ((temp & myPieces) != 0) {
                long possiblePin = temp;
                while (true) {
                    if ((temp & SOUTH_WEST) != 0) {
                        break;
                    }
                    temp >>>= 7;
                    if ((temp & diagonalThreats) != 0) {
                        diagonalPinnedPieces |= possiblePin;
                        break thisDirection;
                    }
                    if ((temp & enemyPieces) != 0) {
                        break thisDirection;
                    }
                    if ((temp & myPieces) != 0) {
                        break thisDirection;
                    }
                }
            }
            if ((temp & enemyPieces) != 0) {
                break;
            }
        }


        temp = squareOfInterest;
        thisDirection:
        while (true) {
            if ((temp & SOUTH_EAST) != 0) {
                break;
            }
            temp >>>= 9;
            if ((temp & myPieces) != 0) {
                long possiblePin = temp;
                while (true) {
                    if ((temp & SOUTH_EAST) != 0) {
                        break;
                    }
                    temp >>>= 9;
                    if ((temp & diagonalThreats) != 0) {
                        diagonalPinnedPieces |= possiblePin;
                        break thisDirection;
                    }
                    if ((temp & enemyPieces) != 0) {
                        break thisDirection;
                    }
                    if ((temp & myPieces) != 0) {
                        break thisDirection;
                    }
                }
            }
            if ((temp & enemyPieces) != 0) {
                break;
            }
        }

        return diagonalPinnedPieces;
    }

    static long cardinalPins(Chessboard board, boolean white, long squareOfInterest) {
        long ALL_PIECES = board.ALL_WHITE_PIECES() | board.ALL_BLACK_PIECES();
        long myPieces = (white) ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES();
        long enemyPieces = (!white) ? board.ALL_WHITE_PIECES() : board.ALL_BLACK_PIECES();
        long cardinalThreats = (white) ? (board.BLACK_ROOKS | board.BLACK_QUEEN) : (board.WHITE_ROOKS | board.WHITE_QUEEN);

        long cardinalPinnedPieces = 0;

        long temp = squareOfInterest;
        thisDirection:
        while (true) {
            if ((temp & FILE_A) != 0) {
                break;
            }
            temp <<= 1;
            if ((temp & myPieces) != 0) {
                long possiblePin = temp;
                while (true) {
                    if ((temp & FILE_A) != 0) {
                        break;
                    }
                    temp <<= 1;
                    if ((temp & cardinalThreats) != 0) {
                        cardinalPinnedPieces |= possiblePin;
                        break thisDirection;
                    }
                    if ((temp & enemyPieces) != 0) {
                        break thisDirection;
                    }
                    if ((temp & myPieces) != 0) {
                        break thisDirection;
                    }
                }
            }
            if ((temp & enemyPieces) != 0) {
                break;
            }
        }

        temp = squareOfInterest;
        thisDirection:
        while (true) {
            if ((temp & RANK_EIGHT) != 0) {
                break;
            }
            temp <<= 8;
            if ((temp & myPieces) != 0) {
                long possiblePin = temp;
                while (true) {
                    if ((temp & RANK_EIGHT) != 0) break;
                    temp <<= 8;
                    if ((temp & cardinalThreats) != 0) {
                        cardinalPinnedPieces |= possiblePin;
                        break thisDirection;
                    }
                    if ((temp & enemyPieces) != 0) {
                        break thisDirection;
                    }
                    if ((temp & myPieces) != 0) {
                        break thisDirection;
                    }
                }
            }
            if ((temp & enemyPieces) != 0) {
                break;
            }
        }

        temp = squareOfInterest;
        thisDirection:
        while (true) {
            if ((temp & FILE_H) != 0) {
                break;
            }
            temp >>>= 1;
            if ((temp & myPieces) != 0) {
                long possiblePin = temp;
                while (true) {
                    if ((temp & FILE_H) != 0){
                        break;
                    }
                    temp >>>= 1;
                    if ((temp & cardinalThreats) != 0) {
                        cardinalPinnedPieces |= possiblePin;
                        break thisDirection;
                    }
                    if ((temp & enemyPieces) != 0){
                        break thisDirection;
                    }
                    if ((temp & myPieces) != 0) {
                        break thisDirection;
                    }
                }
            }
            if ((temp & enemyPieces) != 0){
                break;
            }
        }


        temp = squareOfInterest;
        thisDirection:
        while (true) {
            if ((temp & RANK_ONE) != 0) {
                break;
            }
            temp >>>= 8;
            if ((temp & myPieces) != 0) {
                long possiblePin = temp;
                while (true) {
                    if ((temp & RANK_ONE) != 0) {
                        break;
                    }
                    temp >>>= 8;
                    if ((temp & cardinalThreats) != 0) {
                        cardinalPinnedPieces |= possiblePin;
                        break thisDirection;
                    }
                    if ((temp & enemyPieces) != 0) {
                        break thisDirection;
                    }
                    if ((temp & myPieces) != 0) {
                        break thisDirection;
                    }
                }
            }
            if ((temp & enemyPieces) != 0) {
                break;
            }
        }

        return cardinalPinnedPieces;
    }


}
