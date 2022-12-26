package adventofcode;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day7A {

  private static final Directory ROOT = new Directory(null, "/");
  private static Directory WD = ROOT;

  private static final Pattern CD_PATTERN = Pattern.compile("^\\$ cd (?<path>.*)");
  private static final Pattern FILE_PATTERN = Pattern.compile("^(?<size>\\d+) (?<name>.*)");
  private static final Pattern DIR_PATTERN = Pattern.compile("^dir (?<name>.*)");

  public static void main(String[] args) throws IOException {
    URL resource = Day7A.class.getClassLoader().getResource("input.txt");
    Files.newBufferedReader(new File(resource.getPath()).toPath()).lines().forEach(Day7A::parse);

    Set<Directory> dirs = new HashSet<>();
    traverse(ROOT, dirs);

    System.out.println(dirs.stream().filter(d -> d.getSize() <= 100000).mapToInt(Directory::getSize).sum());
  }

  static void traverse(Directory directory, Set<Directory> dirs) {
    if (dirs.contains(directory)) {
      return;
    }

    directory.directories.values().forEach(d -> traverse(d, dirs));
    dirs.add(directory);
  }

  private static void parse(String line) {
    if (CD_PATTERN.matcher(line).matches()) {
      handleCD(line);
    } else if (FILE_PATTERN.matcher(line).matches()) {
      handleFileInfo(line);
    } else if (DIR_PATTERN.matcher(line).matches()) {
      handleDirectoryInfo(line);
    }
  }

  private static void handleCD(String line) {
    Matcher matcher = CD_PATTERN.matcher(line);
    matcher.find();
    String path = matcher.group("path");
    if (path.equals("..")) {
      WD = WD.getParent();
    } else if (path.startsWith("/")) {
      WD = ROOT.getChild(path);
    } else {
      WD = WD.getChild(path);
    }
  }

  private static void handleDirectoryInfo(String line) {
    var dirMatcher = DIR_PATTERN.matcher(line);
    dirMatcher.find();
    String name = dirMatcher.group("name");
    WD.getChild(name);
  }

  private static void handleFileInfo(String line) {
    var fileMatcher = FILE_PATTERN.matcher(line);
    fileMatcher.find();
    WD.addFile(fileMatcher.group("name"), fileMatcher.group("size"));
  }

  private static class Directory {
    private final Map<String, Directory> directories = new HashMap<>();
    private final Map<String, Integer> files = new HashMap<>();
    private final Directory parent;

    private final String name;

    private Directory(Directory parent, String name) {
      this.parent = parent;
      this.name = name;
    }

    int getSize() {
      return files.values().stream().mapToInt(o -> o).sum()
          + directories.values().stream().mapToInt(Directory::getSize).sum();
    }

    public Directory getParent() {
      return parent;
    }

    public Directory getChild(String path) {
      if (path.equals("/")) return this;

      var directoryName = path.contains("/") ? path.split("/")[0] : path;
      var directory = directories.computeIfAbsent(directoryName, unused -> new Directory(this, directoryName));

      return path.contains("/") ? directory.getChild(path.split("/")[1]) : directory;
    }

    public void addFile(String name, String size) {
      files.putIfAbsent(name, Integer.parseInt(size));
    }
  }
}
