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

  record Lights(Map<Pos, Character> grid, Set<Pos> alwaysOn) {
    Lights(List<String> input, Set<Pos> alwaysOn) {
      this(Pos.collectByPos(input), alwaysOn);
    }
    int neighbors(Pos pos) {
      return (int) Pos.EDGES.stream().map(pos::plus).filter(p -> grid.getOrDefault(p, '.') == '#').count();
    }
    Lights next() {
      Map<Pos, Character> next = new HashMap<>();
      grid.forEach((pos, ch) -> {
        int neighbors = neighbors(pos);
        boolean isOn;
        if (alwaysOn.contains(pos)) {
          isOn = true;
        } else if (ch == '#') {
          isOn = neighbors == 2 || neighbors == 3;
        } else {
          isOn = neighbors == 3;
        }
        next.put(pos, isOn ? '#' : '.');
      });
      return new Lights(next, alwaysOn);
    }
  }

  private long countLights(Lights initial) {
    Lights lights = Stream.iterate(initial, Lights::next).skip(steps).findFirst().orElseThrow();
    return lights.grid.values().stream().filter(ch -> ch == '#').count();
  }

  @Override
  String part1() {
    long count = countLights(new Lights(input, Set.of()));
    return String.valueOf(count);
  }

  @Override
  String part2() {
    int maxX = input.getFirst().length() - 1;
    int maxY = input.size() - 1;
    Lights initial = new Lights(input, Set.of(new Pos(0, 0), new Pos(0, maxY), new Pos(maxX, 0), new Pos(maxX, maxY)));
    long count = countLights(initial);
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
