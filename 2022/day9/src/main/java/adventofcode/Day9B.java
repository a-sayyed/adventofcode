package adventofcode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class Day9B {

  private static final Head HEAD = new Head();

  public static void main(String[] args) throws IOException, InterruptedException {
    URL resource = Day9B.class.getClassLoader().getResource("input.txt");

    Files.newBufferedReader(new File(resource.getPath()).toPath()) //
        .lines() //
        .forEach(Day9B::moveHead);

    System.out.println(HEAD.getTail().visitedPositions.size());
  }

  private static void moveHead(String line) {
    var positionCalculator =
        switch (line.split(" ")[0]) {
          case "U" -> getPositionSupplier(-1, 0);
          case "D" -> getPositionSupplier(1, 0);
          case "L" -> getPositionSupplier(0, -1);
          case "R" -> getPositionSupplier(0, 1);
          default -> throw new IllegalStateException();
        };

    IntStream.range(0, Integer.parseInt(line.split(" ")[1]))
        .forEach(value -> HEAD.moveTo(positionCalculator.get()));
  }

  private static Supplier<Position> getPositionSupplier(int xDelta, int yDelta) {
    return () -> new Position(HEAD.position.x + xDelta, HEAD.position.y + yDelta);
  }

  static final class Node {

    private Set<Position> visitedPositions = new HashSet<>();
    private Position position = new Position(0, 0);

    private final Node nextNode;

    public Node(int nNodesInSeries) {
      this.nextNode = nNodesInSeries == 1 ? null : new Node(nNodesInSeries - 1);
      visitedPositions.add(position);
    }

    private void move(Position headPosition) {
      if (headPosition.isAdjacent(position)) {
        return;
      }

      if (headPosition.y == position.y) {
        int newX = (headPosition.x - position.x) < 0 ? position.x - 1 : position.x + 1;
        position = new Position(newX, position.y);
      } else if (headPosition.x == position.x) {
        int newY = (headPosition.y - position.y) < 0 ? position.y - 1 : position.y + 1;
        position = new Position(headPosition.x, newY);
      } else {
        int newX = (headPosition.x - position.x) < 0 ? position.x - 1 : position.x + 1;
        int newY = (headPosition.y - position.y) < 0 ? position.y - 1 : position.y + 1;
        position = new Position(newX, newY);
      }
      visitedPositions.add(position);
      Optional.ofNullable(nextNode).ifPresent(n -> n.move(position));
    }
  }

  static class Head {

    public Node node = new Node(9);
    private Position position = new Position(0, 0);

    public void moveTo(Position newPosition) {
      this.position = newPosition;
      node.move(newPosition);
    }

    public Node getTail() {
      return getTail(node);
    }
    private static Node getTail(Node node) {
      return node.nextNode == null ? node : getTail(node.nextNode);
    }
  }

  record Position(int x, int y) {

    public boolean isAdjacent(Position position) {
      return (x == position.x && Math.abs(y - position.y) == 1)
          || (y == position.y && Math.abs(x - position.x) == 1)
          || (Math.abs(x - position.x) == 1 && Math.abs(y - position.y) == 1)
          || (x == position.x && y == position.y);
    }
  }
}
