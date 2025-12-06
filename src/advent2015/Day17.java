package advent2015;

import java.util.List;

public class Day17 extends Day {
  public Day17() {
    super(17);
  }

  int target = 150;

  @Override
  String part1() {
    List<Integer> containers = Support.integers(data).boxed().toList();
    long total = 0;
    for (int i = 1; i < containers.size(); i++) {
      total += Support.combinations(containers, i).filter(l -> l.stream().mapToInt(Integer::intValue).sum() == target).count();
    }
    return String.valueOf(total);
  }

  @Override
  String part2() {
    List<Integer> containers = Support.integers(data).boxed().toList();
    long total = 0;
    for (int i = 1; i < containers.size(); i++) {
      total = Support.combinations(containers, i).filter(l -> l.stream().mapToInt(Integer::intValue).sum() == target).count();
      if (total != 0) {
        break;
      }
    }
    return String.valueOf(total);
  }

  static void main() {
    Day17 day = new Day17() {
      @Override
      String getData() {
        return "20, 15, 10, 5, and 5";
      }
    };
    day.target = 25;
    day.run("4", "3");
  }
}
