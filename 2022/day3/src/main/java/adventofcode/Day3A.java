package adventofcode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Set;

public class Day3A {
  public static void main(String[] args) throws IOException {
    URL resource = Day3A.class.getClassLoader().getResource("input.txt");

    int score =
        Files.newBufferedReader(new File(resource.getPath()).toPath())
            .lines()
            .map(Rucksack::new)
            .mapToInt(Rucksack::findItemPriority)
            .sum();

    System.out.println(score);
  }

  private static class Rucksack {
    private final Compartment left;
    private final Compartment right;

    private Rucksack(String items) {
      left = new Compartment(items.substring(0, (items.length() / 2)));
      right = new Compartment(items.substring((items.length() / 2)));
    }

    public int findItemPriority() {
      Character overlap = left.compareTo(right);
      String alphabet = "abcdefghijklmnopqrstuvwxyz";

      if (Character.isLowerCase(overlap)) {
        return 1 + getOrderInAlphabet(overlap, alphabet);
      }

      return 27 + getOrderInAlphabet(overlap, alphabet.toUpperCase());
    }

    private int getOrderInAlphabet(Character character, String alphabet) {
      char[] chars = alphabet.toCharArray();
      for (int i = 0; i < chars.length; i++) {
        if (character.equals(chars[i])) {
          return i;
        }
      }
      throw new IllegalStateException("Execution should not reach this point!");
    }
  }

  private static class Compartment {
    private final Set<Character> items = new HashSet<>();

    public Compartment(String compartment) {
      compartment.chars().forEach(c -> items.add(((char) c)));
    }

    public Character compareTo(Compartment other) {
      return other.items.stream().filter(items::contains).findFirst().get();
    }
  }
}
