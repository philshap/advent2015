package advent2015;

import java.util.List;
import java.util.regex.Pattern;

public class Day14 extends Day {
  protected Day14() {
    super(14);
  }

  static class Reindeer {
    final String name;
    final int fly;
    final int speed;
    final int rest;
    int flyRemaining;
    int restRemaining;
    boolean flying;
    int distance;
    int score;
    // Vixen can fly 19 km/s for 7 seconds, but then must rest for 124 seconds.
    static final Pattern REINDEER = Pattern.compile("(.*) can fly (\\d+) km/s for (\\d+) seconds, but then must rest for (\\d+) seconds");

    int distance() {
      return distance;
    }

    int score() {
      return score;
    }

    Reindeer(String line) {
      var result = REINDEER.matcher(line).results().findFirst().orElseThrow();
      name = result.group(1);
      speed = Integer.parseInt(result.group(2));
      fly = Integer.parseInt(result.group(3));
      rest = Integer.parseInt(result.group(4));
    }

    void tick() {
      if (flying) {
        if (flyRemaining == 0) {
          flying = false;
          restRemaining = rest - 1;
        } else {
          flyRemaining--;
          distance += speed;
        }
      } else {
        if (restRemaining == 0) {
          flying = true;
          flyRemaining = fly - 1;
          distance += speed;
        } else {
          restRemaining--;
        }
      }
    }
  }

  @Override
  String part1() {
    List<Reindeer> reindeer = input.stream().map(Reindeer::new).toList();
    for (int i = 0; i < 2503; i++) {
      reindeer.forEach(Reindeer::tick);
    }
    return String.valueOf(reindeer.stream().mapToInt(Reindeer::distance).max().orElseThrow());
  }

  @Override
  String part2() {
    List<Reindeer> reindeer = input.stream().map(Reindeer::new).toList();
    for (int i = 0; i < 2503; i++) {
      reindeer.forEach(Reindeer::tick);
      var lead = reindeer.stream().mapToInt(Reindeer::distance).max().orElseThrow();
      reindeer.stream().filter(r -> r.distance == lead).forEach(r -> r.score++);
    }
    return String.valueOf(reindeer.stream().mapToInt(Reindeer::score).max().orElseThrow());
  }

  public static void main(String[] args) {
    var day = new Day14() {
      @Override
      String getData() {
        return """
            Comet can fly 14 km/s for 10 seconds, but then must rest for 127 seconds.
            Dancer can fly 16 km/s for 11 seconds, but then must rest for 162 seconds.
            """;
      }
    };
    System.out.println(day.part1());
    System.out.println(day.part2());
  }
}
