package adventofcode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.IntStream;

public class Day6B {

  public static void main(String[] args) throws IOException {
    URL resource = Day6B.class.getClassLoader().getResource("input.txt");
    var result =
        Files.newBufferedReader(new File(resource.getPath()).toPath())
            .lines()
            .mapToInt(Day6B::parse)
            .sum();
    System.out.println(result);
  }

  private static int parse(String line) {
    Deque<Character> characters = new ArrayDeque<>();
    line.chars().forEach(c -> characters.addLast((char) c));

    var count = 0;
    while (characters.size() >= 14) {
      var elements = new ArrayList<Character>();
      IntStream.range(0, 14).forEach(unused -> elements.add(characters.removeFirst()));

      if (isMessageSignal(elements)) {
        return count + 14;
      }
      ListIterator<Character> backwardsIterator = elements
          .subList(1, elements.size())
          .listIterator(elements.size() - 1);
      while (backwardsIterator.hasPrevious()) characters.addFirst(backwardsIterator.previous());
      count++;
    }
    throw new IllegalStateException();
  }

  private static boolean isMessageSignal(List<Character> elements) {
    return new HashSet<>(elements).size() == 14;
  }
}
