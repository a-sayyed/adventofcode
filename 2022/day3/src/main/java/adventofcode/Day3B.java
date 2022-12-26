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
import java.util.Set;
import java.util.stream.Collectors;

public class Day3B {
  public static void main(String[] args) throws IOException {
    URL resource = Day3B.class.getClassLoader().getResource("input.txt");

    Deque<Group> groups = new ArrayDeque<>();

    Files.newBufferedReader(new File(resource.getPath()).toPath())
        .lines()
        .forEachOrdered(
            l -> {
              if (groups.isEmpty() || groups.peekLast().isComplete()) {
                groups.addLast(new Group());
              }
              groups.peekLast().add(l);
            });

    System.out.println(groups.stream().mapToInt(Group::findItemPriority).sum());
  }

  private static class Group {
    private int elfCount = 0;
    private final Set<Character> items = new HashSet<>();

    public boolean isComplete() {
      return elfCount == 3;
    }

    public void add(String items) {
      elfCount++;

      if (this.items.isEmpty()) {
        this.items.addAll(items.chars().mapToObj(i -> (char) i).toList());
        return;
      }

      Set<Character> commonItems =
          items
              .chars()
              .mapToObj(i -> (char) i)
              .filter(this.items::contains)
              .collect(Collectors.toSet());
      this.items.clear();
      this.items.addAll(commonItems);
    }

    public int findItemPriority() {
      Character overlap = this.items.stream().findFirst().get();
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
}
