package adventofcode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Day10A {

  private static int VALUE = 1;
  private static Map<Integer, Integer> SIGNAL_STRENGTHS = new HashMap<>();

  private static final Timer TIMER =
      new Timer(
          cycle -> {
            System.out.println("Cycle " + cycle + " Value " + VALUE + " Signal " + cycle * VALUE);
            if (Set.of(20, 60, 100, 140, 180, 220).contains(cycle)) {
              SIGNAL_STRENGTHS.putIfAbsent(cycle, cycle * VALUE);
            }
          });

  public static void main(String[] args) throws IOException, InterruptedException {
    URL resource = Day10A.class.getClassLoader().getResource("input.txt");
    Files.newBufferedReader(new File(resource.getPath()).toPath()) //
        .lines() //
        .forEach(Day10A::parse);

    System.out.println(SIGNAL_STRENGTHS.values().stream().mapToInt(v -> v).sum());
  }

  private static void parse(String line) {
    switch (line.split(" ")[0]) {
      case "addx" -> TIMER.submit(
          2,
          () -> {
            var valueAsInt = Integer.parseInt(line.split(" ")[1]);
            VALUE += valueAsInt;
          });
      case "noop" -> TIMER.submit(1, () -> {});
      default -> throw new IllegalStateException();
    }
  }

  private static class Timer {
    private int cycle = 1;

    private final Consumer<Integer> cycleConsumer;

    private Timer(Consumer<Integer> cycleConsumer) {
      this.cycleConsumer = cycleConsumer;
    }

    public void submit(int cycles, Runnable callable) {
      while (cycles != 0) {
        cycleConsumer.accept(cycle);
        cycle++;
        cycles--;
      }
      callable.run();
      cycleConsumer.accept(cycle);
    }
  }
}
