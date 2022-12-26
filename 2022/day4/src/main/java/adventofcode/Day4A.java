package adventofcode;

import static java.lang.Integer.parseInt;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.temporal.ValueRange;
import java.util.Arrays;
import java.util.stream.Stream;

public class Day4A {
  public static void main(String[] args) throws IOException {
    URL resource = Day3A.class.getClassLoader().getResource("input.txt");
    int nDuplicates =
        Files.newBufferedReader(new File(resource.getPath()).toPath()) //
            .lines()
            .mapToInt(Day4A::checkDuplicateAssignment)
            .sum();
    System.out.println(nDuplicates);
  }

  private static int checkDuplicateAssignment(String l) {
    String[] a = l.split(",");
    ValueRange first = ValueRange.of(parseInt(a[0].split("-")[0]), parseInt(a[0].split("-")[1]));
    ValueRange second = ValueRange.of(parseInt(a[1].split("-")[0]), parseInt(a[1].split("-")[1]));

    boolean isDuplicate =
        (first.isValidValue(second.getMinimum()) && first.isValidValue(second.getMaximum()))
            || (second.isValidValue(first.getMinimum()) && second.isValidValue(first.getMaximum()));

    return isDuplicate ? 1 : 0;
  }
}
