# Axolotl
## Though she be but little, she is fierce
UCI compliant chess engine written in Java. Can be played on the command line through the UCI protocol, but best connected to a GUI, such as arena or cutechess.
Uses chesscore https://github.com/louism33/ChessCore for legal move generation, through maven.

You will need the latest JRE 11 to run axolotl.

You may use up to 8 threads to run axolotl, the default is one.

Version 1.9 is about 2300 elo. If you run any games with axolotl, I would be very happy to hear the results.
    
You are free to use Axolotl however you see fit. If you have any questions, simply open an issue and I will try to get back to you.

Louis
 
 
Also available as a maven import
 
 ```

<repository>
    <id>com.github.louism33</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots/com/github/louism33/axolotl/</url>
</repository>
```
```

<dependency>
  <groupId>com.github.louism33</groupId>
  <artifactId>axolotl</artifactId>
  <version>1.9</version>
</dependency>
```
