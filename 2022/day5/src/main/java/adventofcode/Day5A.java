package adventofcode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.temporal.ValueRange;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day5A {

  // 1 = reading initial state, 2 = reading instructions
  private static int MODE = 1;

  private static List<Stack> STACKS = new ArrayList<>();

  private static final Pattern INSTRUCTION_MATCHER =
      Pattern.compile("^move (?<count>\\d+) from (?<from>\\d+) to (?<to>\\d+)");

  public static void main(String[] args) throws IOException {
    URL resource = Day5A.class.getClassLoader().getResource("input.txt");
    Files.newBufferedReader(new File(resource.getPath()).toPath()).lines().forEach(Day5A::parse);

    System.out.println(
        STACKS.stream()
            .map(s -> s.value.peekFirst())
            .map(a -> Character.toString(a))
            .collect(Collectors.joining()));
  }

  private static void parse(String line) {
    if (line.isBlank()) {
      MODE = 2;
      return;
    }

    if (MODE == 1) {
      parseInitialState(line);
    } else {
      parseInstruction(line);
    }
  }

  private static void parseInitialState(String line) {
    ensureStacksArePopulated(line.length());

    char[] chars = line.toCharArray();
    for (int i = 0; i < chars.length; i++) {
      if (Character.isAlphabetic(chars[i])) {
        final int position = i;
        STACKS.stream()
            .filter(stack -> stack.range.isValidValue(position))
            .findFirst()
            .ifPresent(stack -> stack.value.addLast(chars[position]));
      }
    }
  }

  private static void ensureStacksArePopulated(int length) {
    if (!STACKS.isEmpty()) {
      return;
    }
    var nStacks = length / 4;

    var start = 0;
    for (int i = 0; i <= nStacks; i++) {
      STACKS.add(new Stack(ValueRange.of(start, start + 2)));
      start += 4;
    }
  }

  private static void parseInstruction(String line) {
    var matcher = INSTRUCTION_MATCHER.matcher(line);
    matcher.find();
    var count = Integer.parseInt(matcher.group("count"));
    var from = Integer.parseInt(matcher.group("from")) - 1;
    var to = Integer.parseInt(matcher.group("to")) - 1;

    while (count != 0) {
      Character character = STACKS.get(from).value.pollFirst();
      STACKS.get(to).value.addFirst(character);
      count--;
    }
  }

  private static class Stack {
    private final ValueRange range;
    private final Deque<Character> value = new ArrayDeque<>();

    private Stack(ValueRange range) {
      this.range = range;
    }
  }
}
