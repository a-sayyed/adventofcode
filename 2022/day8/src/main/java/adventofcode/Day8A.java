package adventofcode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Day8A {

  private static final Map<Integer, List<Tree>> TREES_IN_A_ROW = new HashMap<>();
  private static final Map<Integer, List<Tree>> TREES_IN_A_COLUMN = new HashMap<>();
  private static int ROW = 0;

  public static void main(String[] args) throws IOException {
    URL resource = Day8A.class.getClassLoader().getResource("input.txt");
    Files.newBufferedReader(new File(resource.getPath()).toPath()).lines().forEach(Day8A::parse);
    var count = countVisibleTrees();
    System.out.println(count);
  }

  private static void parse(String line) {
    char[] trees = line.toCharArray();
    for (int i = 0; i < trees.length; i++) {
      var height = Integer.parseInt(String.valueOf(trees[i]));
      var tree = new Tree(ROW, i, height);
      TREES_IN_A_ROW.computeIfAbsent(ROW, integer -> new ArrayList<>()).add(tree);
      TREES_IN_A_COLUMN.computeIfAbsent(i, integer -> new ArrayList<>()).add(tree);
    }
    ROW++;
  }

  private static int countVisibleTrees() {
    var inReverse = true;
    var visible = new HashSet<Tree>();

    count(!inReverse, TREES_IN_A_ROW, visible);
    count(inReverse, TREES_IN_A_ROW, visible);
    count(!inReverse, TREES_IN_A_COLUMN, visible);
    count(inReverse, TREES_IN_A_COLUMN, visible);

    return visible.size();
  }

  private static void count(
      boolean inReverse, Map<Integer, List<Tree>> treesToCount, Set<Tree> visible) {
    treesToCount.forEach(
        (povIndex, trees) -> {
          var iterator = trees.listIterator(inReverse ? trees.size() : 0);
          var visited = new HashSet<Tree>();

          while (!inReverse && iterator.hasNext() || inReverse && iterator.hasPrevious()) {
            var tree = inReverse ? iterator.previous() : iterator.next();
            if (visited.stream().allMatch(t -> t.height < tree.height)) {
              visible.add(tree);
            }
            visited.add(tree);
          }
        });
  }

  record Tree(int row, int column, int height) {}
}
