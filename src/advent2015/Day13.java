package advent2015;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day13 extends Day {
  protected Day13() {
    super(13);
  }

  record Edge(Set<String> fromTo, int distance) {
    // Alice would lose 2 happiness units by sitting next to Bob.
    static Pattern EDGE = Pattern.compile("(.*) would (.*) (\\d+) happiness units by sitting next to (.*)\\.");

    static Edge fromLine(String line) {
      var result = EDGE.matcher(line).results().findFirst().orElseThrow();
      var first = result.group(1);
      var second = result.group(4);
      return new Edge(Set.of(first, second),
                      (result.group(2).equals("gain") ? 1 : -1) * Integer.parseInt(result.group(3)));
    }
  }

  record Graph(Map<Set<String>, Integer> adjacent) {
    static Graph from(List<String> input) {
      return new Graph(input.stream()
                            .map(Edge::fromLine)
                            .collect(Collectors.toMap(Edge::fromTo,
                                                      Edge::distance,
                                                      Integer::sum)));
    }

    Set<String> allNodes() {
      return adjacent.keySet().stream().flatMap(Collection::stream).collect(Collectors.toSet());
    }

    int happiness(List<String> strings) {
      return Stream.concat(Support.partition(strings, 2, 1)
                                  .map(pair -> Set.of(pair.get(0), pair.get(1))),
                           Stream.of(Set.of(strings.get(0), strings.get(strings.size() - 1))))
                   .mapToInt(adjacent::get)
                   .sum();
    }

    int optimal() {
      return Support.permutations(List.copyOf(allNodes()))
                    .stream()
                    .mapToInt(this::happiness)
                    .max()
                    .orElseThrow();
    }
  }

  @Override
  String part1() {
    return String.valueOf(Graph.from(input).optimal());
  }

  @Override
  String part2() {
    var graph = Graph.from(input);
    graph.allNodes().forEach(person -> graph.adjacent.put(Set.of(person, "me"), 0));
    return String.valueOf(graph.optimal());
  }

  public static void main(String[] args) {
    var day = new Day13() {
      @Override
      String getData(int day) {
        return """
            Alice would gain 54 happiness units by sitting next to Bob.
            Alice would lose 79 happiness units by sitting next to Carol.
            Alice would lose 2 happiness units by sitting next to David.
            Bob would gain 83 happiness units by sitting next to Alice.
            Bob would lose 7 happiness units by sitting next to Carol.
            Bob would lose 63 happiness units by sitting next to David.
            Carol would lose 62 happiness units by sitting next to Alice.
            Carol would gain 60 happiness units by sitting next to Bob.
            Carol would gain 55 happiness units by sitting next to David.
            David would gain 46 happiness units by sitting next to Alice.
            David would lose 7 happiness units by sitting next to Bob.
            David would gain 41 happiness units by sitting next to Carol.""";
      }
    };
    System.out.println(day.part1().equals("330"));
  }
}
