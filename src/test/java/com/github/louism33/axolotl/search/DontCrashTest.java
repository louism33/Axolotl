package com.github.louism33.axolotl.search;

import com.github.louism33.axolotl.util.Util;
import com.github.louism33.chesscore.Chessboard;
import com.github.louism33.chesscore.MoveParser;
import com.github.louism33.utils.MoveParserFromAN;
import com.github.louism33.utils.PGNParser;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class DontCrashTest {
    private Engine engine = new Engine();


    @BeforeAll
    static void setup() {
        Util.reset();
    }

    @AfterAll
    static void reset() {
        Util.reset();
    }


    @Test
    void crashedTest() {
        String pgn = "" +
                "1. e4 {book} e6 {book} 2. d4 {0.70s} d5 {0.95s} 3. e5 {0.69s} c5 {0.93s}\n" +
                "4. Nf3 {0.67s} cxd4 {0.91s} 5. Nxd4 {0.66s} Bc5 {0.90s} 6. Nb3 {0.65s}\n" +
                "Bb6 {0.88s} 7. Nc3 {0.63s} Nc6 {0.86s} 8. Bb5 {0.62s} Ne7 {0.85s}\n" +
                "9. Bxc6+ {0.61s} Nxc6 {0.83s} 10. O-O {0.59s} Nxe5 {0.81s} 11. Bf4 {0.58s}\n" +
                "Ng6 {0.80s} 12. Be3 {0.57s} O-O {0.78s} 13. a4 {0.56s} Bd7 {0.77s}\n" +
                "14. Qd3 {0.55s} Qc7 {0.76s} 15. Bxb6 {0.54s} axb6 {0.74s} 16. Qe3 {0.53s}\n" +
                "Qd6 {0.72s} 17. f4 {0.52s} Rfc8 {0.71s} 18. Nd4 {0.51s} Qb4 {0.70s} 19. f5\n" +
                "exf5 {0.69s} 20. Nxd5 Qd6 {0.67s} 21. Nc3 Re8 {0.66s} 22. Qf2 f4 {0.65s}\n" +
                "23. Nde2 Bf5 {0.64s} 24. Nd4 Bg4 {0.62s} 25. Ndb5 Qc6 {0.61s} 26. Rfe1\n" +
                "Red8 {0.60s} 27. Ne4 Kh8 {0.59s} 28. Nd4 Qc4 {0.58s} 29. Nb5 Rd7 {0.56s}\n" +
                "30. Qxb6 Qxc2 {0.56s} 31. Nf2 Bh5 {0.54s} 32. Nd6 Qc7 {0.54s} 33. Qxc7\n" +
                "Rxc7 {0.52s} 34. Re8+ Rxe8 {0.51s} 35. Nxe8 Rc6 {0.51s} 36. b4 Ne5 37. b5 Re6\n" +
                "38. Nc7 Rd6 39. a5 Rg6 40. Nd5 f3 {1.5s} 41. g3 {1.1s} Rd6 {1.5s} 42. a6 {1.1s}\n" +
                "bxa6 {1.5s} 43. bxa6 {1.1s} Nc6 {1.4s} 44. Nf4 {1.1s} g6 {1.4s} 45. a7 {1.0s}\n" +
                "Nxa7 {1.4s} 46. Rxa7 {1.0s} Kg8 {1.4s} 47. Ne4 {0.99s} Rb6 {1.3s}\n" +
                "48. Nxh5 {0.97s} gxh5 {1.3s} 49. Ng5 {0.95s} Kg7 {1.3s} 50. Nxf7 {0.93s}\n" +
                "Kf6 {1.3s} 51. h4 {0.91s} Rb2 {1.2s} 52. Ng5 {0.90s} Kf5 {1.2s} 53. Nxf3 {0.88s}\n" +
                "Kg4 {1.2s} 54. Nd4 {0.86s} Kxg3 {1.2s} 55. Rg7+ {0.84s} Kf4 {1.1s}\n" +
                "56. Rxh7 {0.83s} Ke4 {1.1s} 57. Nc6 {0.81s} Rb6 {1.1s} 58. Rh6 {0.79s}\n" +
                "Ke3 {1.1s} 59. Kf1 {0.78s} Rb1+ {1.1s} 60. Kg2 Rb5 {1.1s} 61. Re6+ {0.76s}\n" +
                "Kf4 {1.0s} 62. Nd4 {0.75s} Rb4 {1.0s} 63. Rf6+ {0.73s} Ke4 {0.99s}\n" +
                "64. Nf3 {0.72s} Ke3 {0.97s} 65. Re6+ {0.70s} Kf4 {0.95s} 66. Re2 {0.69s}\n" +
                "Rc4 {0.94s} 67. Kf2 {0.68s} Rc3 {0.92s} 68. Ng5 {0.66s} Kg4 {0.90s}\n" +
                "69. Re4+ {0.65s} Kf5 70. Rb4 {0.64s} Rd3 {0.87s} 71. Ne4 {0.62s} Rd5 {0.85s}\n" +
                "72. Ng3+ {0.61s} Ke6 {0.83s} 73. Rb6+ {0.60s} Ke5 {0.82s} 74. Nxh5 {0.59s}\n" +
                "Rd4 {0.80s} 75. Rb5+ {0.57s} Kd6 {0.79s} 76. Kg3 {0.56s} Kc6 {0.77s}\n" +
                "77. Rb2 {0.55s} Kd5 {0.76s} 78. Nf4+ {0.54s} Ke4 {0.74s} 79. h5 {0.53s}\n" +
                "Ra4 {0.73s} 80. h6 {0.52s} Ra7 {1.8s} 81. Rb4+ {1.3s} Kf5 {1.7s} 82. Rb5+ {1.3s}\n" +
                "Ke4 {1.7s} 83. Rh5 {1.3s} Rh7 {1.7s} 84. Ne6 {1.2s} Ke3 {1.6s} 85. Ng5 {1.2s}\n" +
                "Rh8 {1.6s} 86. h7 {1.2s} Kd3 {1.6s} 87. Nf7 {1.2s} Rxh7 {1.6s} 88. Rxh7 {1.1s}\n" +
                "Ke4 {1.5s} 89. Rh5 {1.1s} Ke3 {1.5s} 90. Re5+ {1.1s} Kd3 {1.5s} 91. Kf4 {1.1s}\n" +
                "Kd4 {1.4s} 92. Rh5 {1.0s} Kc3 {1.4s} 93. Rd5 {1.0s} Kc4 {1.4s} 94. Ke4 {1.0s}\n" +
                "Kb3 {1.4s} 95. Kd3 Kb4 {1.4s} 96. Ne5 Kb3 {0.68s} 97. Rd4 Kb2 {1.4s} 98. Rd5\n" +
                "Kb3 {1.4s} 99. Rd4" +
                "";

        List<String> pgns = new ArrayList<>();
        pgns.add(pgn);
        try {
            for (int p = 0; p < pgns.size(); p++) {
                List<String> s = PGNParser.parsePGNSimple(pgns.get(p));

                Chessboard board = new Chessboard();
                for (int i = 0; i < s.size(); i++) {
                    String move = s.get(i);

                    move = move.trim();

                    int move1 = 0;
                    try {
                        move1 = MoveParserFromAN.buildMoveFromANWithOO(board, move);
                    } catch (Exception | Error e) {
                        System.out.println(s);
                        System.out.println(board);
                        System.out.println(board.zobristHash);
                        System.out.println(move);
                        System.out.println(MoveParser.toString(move1));
                        e.printStackTrace();
                    }
                    board.makeMoveAndFlipTurn(move1);
                }
                engine.receiveSearchSpecs(board, true, 5000);
                int move = engine.simpleSearch();
            }
        } catch (Exception | Error e) {
            throw new AssertionError("failed on stress test");
        }
    }

    @Test
    void crashed2Test() {
        String pgn = "" +
                "1. e4 {book} c5 {book} 2. Nf3 {book} d6 {book} 3. d4 {book} cxd4 {book}\n" +
                "4. Nxd4 {book} Nf6 {book} 5. Nc3 {book} a6 {book} 6. Be2 {book} e5 {book}\n" +
                "7. Nb3 {book} Be7 {book} 8. O-O {book} O-O {book} 9. Be3 {+0.42/11 0.24s}\n" +
                "Nc6 {0.57s} 10. f4 {+0.33/10 0.24s} Bd7 {0.57s} 11. f5 {+0.19/9 0.25s}\n" +
                "Qc7 {0.57s} 12. a3 {+0.53/10 0.34s} b5 {0.57s} 13. Qd2 {+0.69/10 0.36s}\n" +
                "Rac8 {0.57s} 14. Bf3 {+0.87/10 0.38s} Qb7 {0.57s} 15. Rad1 {+1.33/9 0.39s}\n" +
                "Rfe8 {0.57s} 16. Qf2 {+1.22/9 0.39s} a5 {0.57s} 17. a4 {+1.37/11 0.40s}\n" +
                "bxa4 {0.57s} 18. Nxa4 {-0.70/11 0.41s} Nd4 {0.57s} 19. Bxd4 {-0.62/12 0.41s}\n" +
                "Bxa4 {0.57s} 20. Bc3 {-1.00/11 0.42s} Nxe4 {0.57s} 21. Ra1 {-0.65/10 0.42s}\n" +
                "Bxb3 {0.57s} 22. Bxe4 {-0.70/12 0.43s} Qxe4 {0.57s} 23. cxb3 {-0.60/11 0.43s}\n" +
                "Qb7 {0.57s} 24. Qg3 {-1.00/10 0.44s} Qb6+ {0.57s} 25. Kh1 {-0.34/11 0.44s}\n" +
                "Rc5 {0.57s} 26. f6 {+0.50/11 0.44s} Bf8 {0.57s} 27. Bd2 {+0.26/12 0.45s}\n" +
                "d5 {0.57s} 28. fxg7 {+0.03/12 0.45s} Bxg7 {0.57s} 29. Qf2 {-0.24/14 0.45s}\n" +
                "Rf8 {0.57s} 30. Bxa5 {+0.01/12 0.46s} Qb5 {0.57s} 31. Kg1 {-0.49/11 0.46s}\n" +
                "Rc6 {0.57s} 32. b4 {-0.04/9 0.46s} e4 {0.57s} 33. Rfc1 {-0.54/10 0.46s}\n" +
                "Re6 {0.57s} 34. Rc5 {-2.82/8 0.47s} Qd3 {0.57s} 35. Qe1 {-2.86/12 0.47s}\n" +
                "Bd4+ {0.57s} 36. Kh1 {-2.76/13 0.47s} Bxc5 {0.57s} 37. bxc5 {-2.76/11 0.47s}\n" +
                "Ra8 {0.57s} 38. Ra3 {-2.50/10 0.47s} Qc4 {0.57s} 39. Rg3+ {-2.53/9 0.48s}\n" +
                "Kf8 {0.57s} 40. b4 {-2.41/10 0.48s} d4 {0.57s} 41. Kg1 {-2.71/8 0.48s}\n" +
                "e3 {0.57s} 42. h3 {-4.10/9 0.48s} Qd5 {0.57s} 43. Bc7 {-6.23/7 0.48s} d3 {0.57s}\n" +
                "44. Bd6+ {-10.60/10 0.48s} Rxd6 {0.57s} 45. Rxe3 {-10.64/12 0.48s} d2 {0.57s}\n" +
                "46. Qd1 {-10.74/14 0.48s} Ra1 {0.57s} 47. Qxa1 {-12.05/13 0.48s} d1=Q+ {0.57s}\n" +
                "48. Qxd1 {-12.31/13 0.49s} Qxd1+ {0.57s} 49. Kh2 {-12.37/13 0.49s} Rd4 {0.57s}\n" +
                "50. Rf3 {-12.33/10 0.49s} Qc2 {0.57s} 51. Re3 {-13.16/11 0.49s} Rxb4 {0.57s}\n" +
                "52. Re5 {-14.15/10 0.49s} Qf2 {0.57s} 53. Rg5 {-M14/12 0.49s} Qf4+\n" +
                "54. Rg3 {-M12/13 0.49s} Rb3" +
                "";
        String pgn2 = "" +
                "1. e4 {book} c5 {book} 2. Nc3 {book} Nc6 {book} 3. g3 {book} g6 {book}\n" +
                "4. Bg2 {book} Bg7 {book} 5. d3 {book} d6 {book} 6. f4 {book} e6 {book}\n" +
                "7. Nf3 {book} Nge7 {book} 8. O-O {book} O-O {book} 9. a4 {+0.21/10 0.24s}\n" +
                "Bd7 {0.57s} 10. Nb5 {+0.42/9 0.24s} d5 {0.57s} 11. Nd6 {+0.37/10 0.25s}\n" +
                "Qb6 {0.57s} 12. e5 {-0.11/9 0.34s} f6 {0.57s} 13. a5 {-0.26/8 0.36s}\n" +
                "Nxa5 {0.57s} 14. Qe2 {-0.28/8 0.38s} Nac6 {0.57s} 15. c3 {-0.55/8 0.39s}\n" +
                "Nxe5 {0.57s} 16. fxe5 {-0.49/11 0.39s} fxe5 {0.57s} 17. Nxe5 {-0.23/11 0.40s}\n" +
                "Rxf1+ {0.57s} 18. Qxf1 {+0.08/12 0.41s} c4+ {0.57s} 19. d4 {+0.88/11 0.41s}\n" +
                "Rf8 {0.57s} 20. Qe2 {-0.51/10 0.42s} Qxd6 {0.57s} 21. Rxa7 {-0.45/11 0.42s}\n" +
                "Bxe5 {0.57s} 22. Qxe5 {-0.04/14 0.43s} Qxe5 {0.57s} 23. dxe5 {+0.20/14 0.43s}\n" +
                "Nc6 {0.57s} 24. Rxb7 {+0.46/15 0.44s} Nxe5 {0.57s} 25. Bf4 {+0.40/15 0.44s}\n" +
                "Bc8 {0.57s} 26. Rc7 {+0.33/14 0.44s} Nd7 {0.57s} 27. Bd6 {+0.35/15 0.45s}\n" +
                "Re8 {0.57s} 28. Ra7 {+0.31/16 0.45s} Nf6 {0.57s} 29. Bh3 {+0.17/15 0.45s}\n" +
                "g5 {0.57s} 30. Be5 {+0.08/16 0.46s} Nd7 {0.57s} 31. Bd4 {+0.08/16 0.46s}\n" +
                "Kf7 {0.57s} 32. Be3 {+0.42/11 0.46s} Rg8 {0.57s} 33. Bg4 {+0.32/12 0.46s}\n" +
                "Kf6 {0.57s} 34. h3 {+0.54/12 0.47s} Ke5 {0.57s} 35. Kg2 {+0.78/12 0.47s}\n" +
                "Kd6 {0.57s} 36. Bf2 {0.00/9 0.47s} Nf6 {0.57s} 37. Bf3 {-0.41/12 0.47s}\n" +
                "Bd7 {0.57s} 38. Bb6 {0.00/10 0.47s} Rc8 {0.57s} 39. Ba5 {-0.17/11 0.48s}\n" +
                "h5 {0.57s} 40. Kg1 {-0.07/10 0.48s} g4 {0.57s} 41. hxg4 {-0.52/11 0.48s}\n" +
                "hxg4 {0.57s} 42. Bd1 {-0.53/12 0.48s} e5 {0.57s} 43. Bc2 {-1.00/11 0.48s}\n" +
                "e4 {0.57s} 44. Bb6 {0.00/11 0.48s} Bc6 {0.57s} 45. Be3 {-0.22/9 0.48s}\n" +
                "Ke6 {0.57s} 46. Bd4 {-0.27/10 0.48s} Nd7 {0.57s} 47. Kf1 {-0.48/11 0.49s}\n" +
                "Ne5 {0.57s} 48. Rh7 {-0.69/11 0.49s} Nf7 {0.57s} 49. Bd1 {+0.38/11 0.49s}\n" +
                "Rg8 {0.57s} 50. Kg2 {+0.09/11 0.49s} Be8 {0.57s} 51. Rh5 {+0.15/10 0.49s}\n" +
                "Bd7 {0.57s} 52. Be2 {+0.18/11 0.49s} Kd6 {0.57s} 53. Be3 {0.00/12 0.49s}\n" +
                "Kc6 {0.57s} 54. Bf4 {-0.05/11 0.49s} Be6 {0.57s} 55. Rh7 {0.00/12 0.49s}\n" +
                "Rg6 {0.57s} 56. Bd1 {+0.24/11 0.49s} Kd7 {0.57s} 57. Rh5 {+0.18/11 0.49s}\n" +
                "Rg8 {0.57s} 58. Ba4+ {0.00/11 0.49s} Ke7 {0.57s} 59. Bd1 {-0.40/12 0.49s}\n" +
                "Kf6 {0.57s} 60. Rh4 {-0.52/12 0.49s} Ne5 {0.57s} 61. Rh6+ {-0.35/10 0.49s}\n" +
                "Ng6 {0.57s} 62. Bc1 {-0.54/11 0.49s} Rb8 {0.57s} 63. Rh5 {-0.95/11 0.49s}\n" +
                "Bf5 {0.57s} 64. Bg5+ {-1.00/12 0.49s} Ke6 {0.57s} 65. Bc1 {-1.39/13 0.50s}\n" +
                "Ra8 {0.57s} 66. Bc2 {-0.49/10 0.50s} Rc8 {0.57s} 67. Ba4 {-0.26/12 0.50s}\n" +
                "Ne5 {0.57s} 68. Rh6+ {-0.31/12 0.50s} Kf7 {0.57s} 69. Rh5 {-0.55/12 0.50s}\n" +
                "Kf6 {0.57s} 70. Rh6+" +
                "";

        String pgn3 = "" +
                "1. f4 {book} d5 {book} 2. Nf3 {book} Nf6 {book} 3. b3 {book} g6 {book}\n" +
                "4. Bb2 {book} Bg7 {book} 5. e3 {book} O-O {book} 6. Be2 {book} c5 {book}\n" +
                "7. O-O {book} Nc6 {book} 8. Ne5 {book} Bd7 9. Nc3 {-0.20/2 0.25s} d4\n" +
                "10. exd4 {-0.27/4 0.26s} Nxd4 11. Nxd7 {+0.02/3 0.27s} Qxd7\n" +
                "12. Bd3 {-0.28/5 0.26s} Rad8 13. Re1 {-0.50/7 0.27s} Ne6 14. Ne4 {-0.50/7 0.28s}\n" +
                "Nxf4 15. Bf1 {-1.35/8 0.28s} Nxe4 16. Bxg7 {-1.70/9 0.28s} Kxg7\n" +
                "17. Rxe4 {-1.72/10 0.28s} e5 18. g3 {-1.67/10 0.29s} Ne6\n" +
                "19. Rxe5 {-1.57/11 0.29s} Qd4+ 20. Re3 {-1.52/12 0.30s} Qxd2\n" +
                "21. Qxd2 {-1.24/14 0.30s} Rxd2 22. c3 {-1.30/13 0.28s} Rfd8\n" +
                "23. Bh3 {-1.32/12 0.29s} Ng5 24. Bg2 {-1.48/12 0.29s} b6 25. a4 {-1.45/12 0.30s}\n" +
                "h6 26. a5 {-1.64/10 0.30s} Rd1+ 27. Rxd1 {-1.75/13 0.30s} Rxd1+\n" +
                "28. Kf2 {-1.84/14 0.31s} Rd2+ 29. Re2 {-1.06/15 0.31s} Rxe2+\n" +
                "30. Kxe2 {-1.24/16 0.32s} bxa5 31. Kd3 {-1.00/15 0.32s} f5\n" +
                "32. Kc4 {-1.17/15 0.32s} Ne4 33. Kb5 {-1.26/15 0.33s} Nxc3+\n" +
                "34. Kxa5 {-1.28/15 0.33s} Kf6 35. Ka6 {-1.35/15 0.33s} g5\n" +
                "36. Kxa7 {-1.25/14 0.34s} Ke5 37. Kb6 {-1.23/14 0.34s} Kd6\n" +
                "38. Bf3 {-2.00/13 0.34s} Ne4 39. Be2 {-2.82/12 0.35s} Nd2\n" +
                "40. Bd1 {-3.35/14 0.35s} f4 41. gxf4 {-3.16/15 0.35s} gxf4\n" +
                "42. Kb5 {-3.34/15 0.36s} f3 43. b4 {-3.39/16 0.36s} cxb4\n" +
                "44. Kxb4 {-3.59/17 0.36s} f2 45. Be2 {-3.59/17 0.36s} f1=Q\n" +
                "46. Bxf1 {-4.01/18 0.37s} Nxf1 47. h3 {-4.50/17 0.37s} Ng3\n" +
                "48. Kb5 {-4.88/17 0.37s} h5 49. Kc4 {-5.49/19 0.38s} Ne2\n" +
                "50. Kd3 {-5.21/19 0.38s} Nf4+ 51. Ke3 {-6.25/20 0.38s} Nxh3\n" +
                "52. Kf3 {-6.91/19 0.39s} Ke5 53. Kg3 {-7.13/21 0.38s} Nf4\n" +
                "54. Kh4 {-7.13/19 0.39s} Kf5 55. Kg3 Ng6 56. Kg2 {-16.52/17 0.40s} Kg4\n" +
                "57. Kf2 {-16.67/16 0.40s} h4 58. Ke3 {-16.67/14 0.40s} h3\n" +
                "59. Kd4 {-16.67/13 0.41s} h2 60. Kc5 {-16.67/12 0.41s} h1=Q\n" +
                "61. Kb5 {-M42/10 0.41s} Qd5+ 62. Kb4 {-M52/11 0.41s} Ne5 63. Kc3 {-M14/10 0.41s}\n" +
                "Qc4+ 64. Kd2 {-M6/126 0.26s} Qd3+ 65. Ke1 {-M6/126 0.32s} Nf3+ 66. Kf2\n" +
                "Qd2+ " +
                "";
        List<String> pgns = new ArrayList<>();
        pgns.add(pgn);
        pgns.add(pgn2);
        pgns.add(pgn3);

        try {
            for (int p = 0; p < pgns.size(); p++) {
                List<String> s = PGNParser.parsePGNSimple(pgns.get(p));

                Util.reset();

                Chessboard board = new Chessboard();
                for (int i = 0; i < s.size(); i++) {
                    String move = s.get(i);

                    move = move.trim();

                    int move1 = 0;
                    try {
                        move1 = MoveParserFromAN.buildMoveFromANWithOO(board, move);
                    } catch (Exception | Error e) {
                        System.out.println(s);
                        System.out.println(board);
                        System.out.println(board.zobristHash);
                        System.out.println(move);
                        System.out.println(MoveParser.toString(move1));
                        e.printStackTrace();
                    }
                    board.makeMoveAndFlipTurn(move1);
                }
                engine.receiveSearchSpecs(board, true, 5000);
                int move = engine.simpleSearch();
            }
        } catch (Exception | Error e) {
            e.printStackTrace();
            throw new AssertionError("failed on stress test");
        }
    }
}
