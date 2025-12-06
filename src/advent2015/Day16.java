package advent2015;

import java.util.Arrays;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day16 extends Day {
  public Day16() {
    super(16);
  }

  static final String TAPE = """
      children: 3
      cats: 7
      samoyeds: 2
      pomeranians: 3
      akitas: 0
      vizslas: 0
      goldfish: 5
      trees: 3
      cars: 2
      perfumes: 1""";

  // Sue 1: goldfish: 6, trees: 9, akitas: 0
  record Sue(Map<String, Integer> things) {
    Sue(String line) {
      var things = Arrays.stream(line.substring(line.indexOf(':') + 2).split(", "))
          .map(thing -> thing.split(": "))
          .collect(Collectors.toMap(thing -> thing[0], thing -> Integer.parseInt(thing[1])));
      this(things);
    }
    boolean matches(Sue target) {
      return target.things.entrySet().stream().allMatch(entry ->
          things.getOrDefault(entry.getKey(), entry.getValue()).equals(entry.getValue()));
    }

    boolean matches2(Sue target) {
      return target.things.entrySet().stream().allMatch(entry -> {
        if (things.containsKey(entry.getKey())) {
          return switch (entry.getKey()) {
            case "cats", "trees" -> entry.getValue() < things.get(entry.getKey());
            case "pomeranians", "goldfish" -> entry.getValue() > things.get(entry.getKey());
            default -> entry.getValue().equals(things.get(entry.getKey()));
          };
        }
        return true;
      });
    }
  }

  private int findMatch(BiFunction<Sue, Sue, Boolean> matches) {
    var sues = input.stream().map(Sue::new).toList();
    var target = new Sue("Sue 0: "  + TAPE.replaceAll("\n", ", "));
    var number = IntStream.range(0, sues.size()).filter(sue -> matches.apply(sues.get(sue), target)).findFirst().orElseThrow();
    return number + 1;
  }

  @Override
  String part1() {
    return String.valueOf(findMatch(Sue::matches));
  }

  @Override
  String part2() {
    return String.valueOf(findMatch(Sue::matches2));
  }

  static void main() {
    Day day = new Day16() {
      @Override
      String getData() {
        return """
            Sue 1: cars: 10, perfumes: 9, vizslas: 3
            Sue 2: pomeranians: 6, trees: 1, samoyeds: 4
            Sue 3: cars: 2, perfumes: 1, goldfish: 5
            Sue 4: goldfish: 2, cars: 8, pomeranians: 2
            Sue 5: akitas: 10, goldfish: 4, trees: 2
            Sue 6: trees: 8, perfumes: 1, cars: 2
            Sue 7: trees: 0, perfumes: 9, pomeranians: 10
            """;
      }
    };
    day.run("3", "6");
  }
}
