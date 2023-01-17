package adventofcode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class Day10B {

  private static int VALUE = 1;
  private static CRT CRT = new CRT();
  private static final Timer TIMER = new Timer(cycle -> {
//    System.out.println("Cycle " + cycle + " Value " + VALUE);
    CRT.draw(cycle);
  });

  public static void main(String[] args) throws IOException, InterruptedException {
    URL resource = Day10B.class.getClassLoader().getResource("input.txt");
    Files.newBufferedReader(new File(resource.getPath()).toPath()) //
        .lines() //
        .forEach(Day10B::parse);
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

  private static class CRT {

    private Set<Integer> alreadyDrewn = new HashSet<>();

    public void draw(Integer cycle) {
      if (alreadyDrewn.contains(cycle)) {
        return;
      }

      if (alreadyDrewn.size() % 40 == 0) {
        System.out.println();
      }

      System.out.print(withinSprite(cycle) ? "#" : ".");
      alreadyDrewn.add(cycle);
    }

    private boolean withinSprite(Integer cycle) {
      int sanitized = ((cycle-1) % 40);
      return Math.abs(sanitized - VALUE) <= 1;
    }
  }
}
