package adventofcode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class Day8B {

  private static final Map<Integer, List<Tree>> TREES_IN_A_ROW = new HashMap<>();
  private static final Map<Integer, List<Tree>> TREES_IN_A_COLUMN = new HashMap<>();
  private static int ROW = 0;

  public static void main(String[] args) throws IOException {
    URL resource = Day8B.class.getClassLoader().getResource("input.txt");
    Files.newBufferedReader(new File(resource.getPath()).toPath()) //
        .lines() //
        .forEach(Day8B::parse);
    var count = countVisibleTrees();
    System.out.println(count);
  }

  private static void parse(String line) {
    char[] trees = line.toCharArray();
    for (int column = 0; column < trees.length; column++) {
      var height = Integer.parseInt(String.valueOf(trees[column]));
      var tree = new Tree(height);
      TREES_IN_A_ROW.computeIfAbsent(ROW, integer -> new ArrayList<>()).add(tree);
      TREES_IN_A_COLUMN.computeIfAbsent(column, integer -> new ArrayList<>()).add(tree);
    }
    ROW++;
  }

  private static int countVisibleTrees() {
    TREES_IN_A_ROW.forEach(
        (row, trees) -> {
          for (int i = 0; i < trees.size(); i++) {
            Tree tree = trees.get(i);
            tree.addVisibility(trees, i);
          }
        });

    TREES_IN_A_COLUMN.forEach(
        (column, trees) -> {
          for (int i = 0; i < trees.size(); i++) {
            Tree tree = trees.get(i);
            tree.addVisibility(trees, i);
          }
        });

    return TREES_IN_A_ROW.values().stream()
        .flatMap(List::stream)
        .mapToInt(t -> t.visibility)
        .max()
        .getAsInt();
  }

  static final class Tree {

    private final int height;
    private int visibility = -1;

    Tree(int height) {
      this.height = height;
    }

    public void addVisibility(List<Tree> series, int index) {
      List<Tree> before = series.subList(0, index);
      if (before.isEmpty()) {
        visibility = 0;
        return;
      }
      ListIterator<Tree> itBefore = before.listIterator(before.size());
      addVisibility(itBefore::hasPrevious, itBefore::previous);

      if (index != series.size() - 1) {
        List<Tree> after = series.subList(index + 1, series.size());
        ListIterator<Tree> itAfter = after.listIterator();
        addVisibility(itAfter::hasNext, itAfter::next);
      }
    }

    private void addVisibility(Supplier<Boolean> hasElements, Supplier<Tree> getElement) {
      var currentVisibility = -1;
      while (hasElements.get()) {
        Tree otherTree = getElement.get();
        currentVisibility = (currentVisibility == -1) ? 1 : (currentVisibility + 1);
        if (otherTree.height >= this.height) {
          break;
        }
      }
      visibility = visibility == -1 ? currentVisibility : visibility * currentVisibility;
    }
  }
}
