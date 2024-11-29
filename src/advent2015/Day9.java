package advent2015;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Day9 extends Day {

  Day9() {
    super(9);
  }

  record Edge(Pair<String, String> fromTo, int distance) {
    static Pattern EDGE = Pattern.compile("(.+) to (.+) = (\\d+)");

    static Stream<Edge> fromLine(String line) {
      return EDGE.matcher(line).results().flatMap(
          edge ->
              // Edges are bidirectional.
              Stream.of(new Edge(Pair.of(edge.group(1), edge.group(2)), Integer.parseInt(edge.group(3))),
                        new Edge(Pair.of(edge.group(2), edge.group(1)), Integer.parseInt(edge.group(3))))
      );
    }
  }

  record Graph(Map<Pair<String, String>, Edge> adjacent) {
    static Graph from(List<String> input) {
      return new Graph(input.stream()
                            .flatMap(Edge::fromLine)
                            .collect(Collectors.toMap(Edge::fromTo, Function.identity())));
    }

    Set<String> allNodes() {
      return adjacent.keySet().stream().map(Pair::r).collect(Collectors.toSet());
    }
  }

  record Path(String last, int distance, Set<String> remaining) {
    static Set<String> without(Set<String> set, String entry) {
      return set.stream().filter(node -> !node.equals(entry)).collect(Collectors.toSet());
    }

    Path(String start, Set<String> allNodes) {
      this(start, 0, without(allNodes, start));
    }

    List<Path> extend(Graph graph) {
      return remaining.stream()
                      .map(next -> graph.adjacent.get(Pair.of(last, next)))
                      .filter(Objects::nonNull)
                      .map(edge -> new Path(edge.fromTo.r(), distance + edge.distance, without(remaining, edge.fromTo.r())))
                      .toList();
    }
  }

  Path findShortest(PriorityQueue<Path> paths, Graph graph) {
    while (true) {
      Path path = paths.poll();
      if (path.remaining.isEmpty()) {
        return path;
      }
      paths.addAll(path.extend(graph));
    }
  }

  @Override
  public String part1() {
    Graph graph = Graph.from(input);
    Set<String> allNodes = graph.allNodes();
    var paths = new PriorityQueue<>(Comparator.comparingInt(Path::distance));
    allNodes.forEach(node -> paths.add(new Path(node, allNodes)));
    return String.valueOf(findShortest(paths, graph).distance);
  }

  Path findLongest(Queue<Path> paths, Graph graph) {
    Path longest = new Path("", 0, Set.of());
    while (!paths.isEmpty()) {
      Path path = paths.poll();
      if (path.remaining.isEmpty()) {
        if (path.distance > longest.distance) {
          longest = path;
        }
      }
      paths.addAll(path.extend(graph));
    }
    return longest;
  }

  @Override
  public String part2() {
    Graph graph = Graph.from(input);
    Set<String> allNodes = graph.allNodes();
    var paths = new LinkedList<Path>();
    allNodes.forEach(node -> paths.add(new Path(node, allNodes)));
    return String.valueOf(findLongest(paths, graph).distance);
  }
}
