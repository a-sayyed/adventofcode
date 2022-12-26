package adventofcode;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;

public class Day6A {

  public static void main(String[] args) throws IOException {
    URL resource = Day6A.class.getClassLoader().getResource("input.txt");
    var result =
        Files.newBufferedReader(new File(resource.getPath()).toPath())
            .lines()
            .mapToInt(Day6A::parse)
            .sum();
    System.out.println(result);
  }

  private static int parse(String line) {
    Deque<Character> characters = new ArrayDeque<>();
    line.chars().forEach(c -> characters.addLast((char) c));

    var count = 0;
    while (characters.size() >= 4) {
      var first = characters.removeFirst();
      var second = characters.removeFirst();
      var third = characters.removeFirst();
      var fourth = characters.removeFirst();

      if (isMarkerSignal(first, second, third, fourth)) {
        return count + 4;
      }

      characters.addFirst(fourth);
      characters.addFirst(third);
      characters.addFirst(second);
      count++;
    }
    throw new IllegalStateException();
  }

  private static boolean isMarkerSignal(
      Character first, Character second, Character third, Character fourth) {
    return new HashSet<>(List.of(first, second, third, fourth)).size() == 4;
  }
}
