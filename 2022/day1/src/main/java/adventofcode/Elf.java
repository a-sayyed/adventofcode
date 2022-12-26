package adventofcode;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class Elf {

  Set<Integer> carrying = new HashSet<>();

  public void carry(int calories) {
    carrying.add(calories);
  }

  public int getTotalCalories() {
    return carrying.stream().mapToInt(value -> value).sum();
  }
}
