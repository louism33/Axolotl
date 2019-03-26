# Axolotl
## Though she be but little, she is fierce
UCI compliant chess engine written in Java. Can be played on the command line through the UCI protocol, but best connected to a GUI, such as arena.
Uses chesscore https://github.com/louism33/ChessCore for legal move generation, through maven.
As external libraries, in addition to my chesscore, JCPI is used for the UCI protocol.

You will need the latest JRE, JRE 11 to run axolotl.
    
You are free to use Axolotl however you see fit. If you have any questions, simply open an issue and I will try to get back to you.

Louis
  
PS: also available as a maven import
 
 ```
<repository>
    <id>com.github.louism33</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots/com/github/louism33/chesscore/</url>
</repository>
```
```
<dependency>
  <groupId>com.github.louism33</groupId>
  <artifactId>axolotl</artifactId>
  <version>1.4-SNAPSHOT</version>
</dependency>
```
