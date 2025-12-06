package advent2015;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class Day18 extends Day {
  public Day18() {
    super(18);
  }

  int steps = 100;

  record Lights(Map<Pos, Character> grid) {
    Lights(List<String> input) {
      this(Pos.collectByPos(input));
    }
    int neighbors(Pos pos) {
      return (int) Pos.EDGES.stream().map(pos::plus).filter(p -> grid.getOrDefault(p, '.') == '#').count();
    }
    Lights next(Set<Pos> alwaysOn) {
      Map<Pos, Character> next = new HashMap<>();
      grid.forEach((pos, character) -> {
        int neighbors = neighbors(pos);
        boolean isOn;
        if (alwaysOn.contains(pos)) {
          isOn = true;
        } else if (character == '#') {
          isOn = neighbors == 2 || neighbors == 3;
        } else {
          isOn = neighbors == 3;
        }
        next.put(pos, isOn ? '#' : '.');
      });
      return new Lights(next);
    }
  }

  @Override
  String part1() {
    Lights lights =
        Stream.iterate(new Lights(input), l -> l.next(Set.of()))
        .skip(steps)
        .findFirst().orElseThrow();
    long count = lights.grid.values().stream().filter(ch -> ch == '#').count();
    return String.valueOf(count);
  }

  @Override
  String part2() {
    Lights initial = new Lights(input);
    int maxX = initial.grid.keySet().stream().map(Pos::x).max(Integer::compareTo).orElseThrow();
    int maxY = initial.grid.keySet().stream().map(Pos::y).max(Integer::compareTo).orElseThrow();
    Set<Pos> alwaysOn = Set.of(new Pos(0, 0), new Pos(0, maxY), new Pos(maxX, 0), new Pos(maxX, maxY));
    Lights lights =
        Stream.iterate(initial, l -> l.next(alwaysOn))
            .skip(steps)
            .findFirst().orElseThrow();
    long count = lights.grid.values().stream().filter(ch -> ch == '#').count();
    return String.valueOf(count);
  }

  static void main() {
    Day18 day = new Day18() {
      @Override
      String getData() {
        return """
            .#.#.#
            ...##.
            #....#
            ..#...
            #.#..#
            ####..""";
      }
    };
    day.steps = 4;
    day.run("4", "14");
  }
}
