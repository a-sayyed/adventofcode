package adventofcode;

import static java.lang.Integer.parseInt;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.temporal.ValueRange;

public class Day4B {
  public static void main(String[] args) throws IOException {
    URL resource = Day3A.class.getClassLoader().getResource("input.txt");
    int nOverlaps =
        Files.newBufferedReader(new File(resource.getPath()).toPath()) //
            .lines()
            .mapToInt(Day4B::checkOverlap)
            .sum();
    System.out.println(nOverlaps);
  }

  private static int checkOverlap(String l) {
    String[] a = l.split(",");
    ValueRange first = ValueRange.of(parseInt(a[0].split("-")[0]), parseInt(a[0].split("-")[1]));
    ValueRange second = ValueRange.of(parseInt(a[1].split("-")[0]), parseInt(a[1].split("-")[1]));

    boolean overlaps =
        first.isValidValue(second.getMinimum())
            || first.isValidValue(second.getMaximum())
            || second.isValidValue(first.getMinimum())
            || second.isValidValue(first.getMaximum());

    return overlaps ? 1 : 0;
  }
}
