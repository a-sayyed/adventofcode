package adventofcode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

public class Day2B {
  public static void main(String[] args) throws IOException {
    URL resource = Day2B.class.getClassLoader().getResource("input.txt");

    int score =
        Files.newBufferedReader(new File(resource.getPath()).toPath())
            .lines()
            .mapToInt(Day2B::playRockPaperScissors)
            .sum();

    System.out.println(score);
  }

  private static int playRockPaperScissors(String round) {
    Choice theirChoice = Choice.of(round.split(" ")[0]);
    Outcome outcome = Outcome.of(round.split(" ")[1]);
    Choice myChoice = outcome.decideChoice(theirChoice);

    if (outcome == Outcome.WIN) {
      return 6 + myChoice.score;
    } else if (outcome == Outcome.DRAW) {
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
      return ROCK.characters.contains(character.toUpperCase())
          ? ROCK
          : PAPER.characters.contains(character.toUpperCase()) ? PAPER : SCISSORS;
    }
  }

  enum Outcome {
    WIN("Z"),
    DRAW("Y"),
    LOSE("X");

    private final String symbol;

    Outcome(String symbol) {
      this.symbol = symbol;
    }

    static Outcome of(String symbol) {
      return WIN.symbol.equals(symbol.toUpperCase())
          ? WIN
          : DRAW.symbol.equals(symbol.toUpperCase()) ? DRAW : LOSE;
    }

    public Choice decideChoice(Choice theirChoice) {
      if (this == WIN) {
        return theirChoice == Choice.ROCK
            ? Choice.PAPER
            : theirChoice == Choice.PAPER ? Choice.SCISSORS : Choice.ROCK;
      }
      if (this == DRAW) {
        return theirChoice;
      }
      return theirChoice == Choice.ROCK
          ? Choice.SCISSORS
          : theirChoice == Choice.PAPER ? Choice.ROCK : Choice.PAPER;
    }
  }
}
