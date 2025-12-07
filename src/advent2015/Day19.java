package advent2015;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Day19 extends Day {
  public Day19() {
    super(19);
  }

  Stream<String> replaceEvery(Pair<String, String> replacement, String molecule) {
    return Pattern.compile(replacement.l()).matcher(molecule)
        .results()
        .map(result -> molecule.substring(0, result.start()) + replacement.r() + molecule.substring(result.end()));
  }

  @Override
  String part1() {
    String[] split = data.split("\n\n");
    var replacements = Support.splitInput(split[0]).stream()
        .map(line -> line.split(" => "))
        .map(line -> Pair.of(line[0], line[1]))
        .toList();
    String molecule = split[1];

    long count = replacements.stream().flatMap(pair -> replaceEvery(pair, molecule)).distinct().count();
    return String.valueOf(count);
  }

  record Medicine(String molecule, int iter) {
    int score() {
      return molecule.length();
    }
  }

  @Override
  String part2() {
    String[] split = data.split("\n\n");
    // replace in reverse; sort longest replacements first.
    var replacements = Support.splitInput(split[0]).stream()
        .map(line -> line.split(" => "))
        .map(line -> Pair.of(line[1], line[0]))
        .sorted(Comparator.comparingInt(p -> p.r().length()))
        .toList();
    String molecule = split[1];
    // Sort queue with shortest molecules first.
    PriorityQueue<Medicine> queue = new PriorityQueue<>(Comparator.comparingInt(Medicine::score));
    queue.add(new Medicine(molecule, 0));
    while (!queue.peek().molecule.equals("e")) {
      var med = queue.remove();
      replacements.stream().flatMap(pair -> replaceEvery(pair, med.molecule))
          .map(m -> new Medicine(m, med.iter + 1))
          .forEach(queue::add);
    }
    return String.valueOf(queue.remove().iter);
  }

  static void main() {
    Day day = new Day19() {
      @Override
      String getData() {
        return """
            e => H
            e => O
            H => HO
            H => OH
            O => HH

            HOH""";
      }
    };
    day.run("4", "3");
  }
}
