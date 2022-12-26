package adventofcode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

public class Day2A {
  public static void main(String[] args) throws IOException {
    URL resource = Day3A.class.getClassLoader().getResource("input.txt");

    int score =
        Files.newBufferedReader(new File(resource.getPath()).toPath())
            .lines()
            .mapToInt(Day2A::playRockPaperScissors)
            .sum();

    System.out.println(score);
  }

  private static int playRockPaperScissors(String round) {
    Choice theirChoice = Choice.of(round.split(" ")[0]);
    Choice myChoice = Choice.of(round.split(" ")[1]);

    if (myChoice.beats(theirChoice)) {
      return 6 + myChoice.score;
    } else if (myChoice == theirChoice) {
      return 3 + myChoice.score;
    } else {
      return myChoice.score;
    }
  }

  enum Choice {
    ROCK(List.of("A", "X"), 1),
    PAPER(List.of("B", "Y"), 2),
    SCISSORS(List.of("C", "Z"), 3);

    private final List<String> characters;
    private final int score;

    Choice(List<String> characters, int score) {
      this.characters = characters;
      this.score = score;
    }

    static Choice of(String character) {
      return ROCK.characters.contains(character.toUpperCase()) ? ROCK //
          : PAPER.characters.contains(character.toUpperCase()) ? PAPER : SCISSORS;
    }

    public boolean beats(Choice other) {
      return (this == ROCK && other == SCISSORS) //
          || (this == PAPER && other == ROCK) //
          || (this == SCISSORS && other == PAPER);
    }
  }
}
