package adventofcode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayDeque;
import java.util.Comparator;
import java.util.Deque;
import java.util.Optional;

public class Day1B {
  public static void main(String[] args) throws IOException {
    URL resource = Day1B.class.getClassLoader().getResource("input.txt");
    assert resource != null;

    Deque<Elf> elves = new ArrayDeque<>();

    Files.newBufferedReader(new File(resource.getPath()).toPath())
        .lines()
        .forEach(
            l -> {
              if (l.isBlank()) {
                elves.add(new Elf());
                return;
              }
              nextElf(elves).carry(Integer.parseInt(l));
            });
    int totalCalories = elves.stream().sorted(Comparator.comparing(Elf::getTotalCalories).reversed())
        .limit(3).mapToInt(Elf::getTotalCalories).sum();

    System.out.println(totalCalories);
  }

  private static Elf nextElf(Deque<Elf> elves) {
    return Optional.ofNullable(elves.peekLast())
        .orElseGet(
            () -> {
              elves.addLast(new Elf());
              return elves.peekLast();
            });
  }
}
