package advent2015;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day23 extends Day {
  public Day23() {
    super(23);
  }

  record Computer(List<String[]> instructions) {
    static Computer fromInput(List<String> input) {
      // jio b, +2
      return new Computer(input.stream().map(line -> line.split(",?\\s+")).toList());
    }

    Map<String, Integer> run(int initialA) {
      Map<String, Integer> registers = new HashMap<>();
      registers.put("a", initialA);
      registers.put("b", 0);
      int pc = 0;
      while (pc < instructions.size()) {
        String[] inst = instructions.get(pc);
        switch (inst[0]) {
          case "hlf" -> registers.put(inst[1], registers.get(inst[1]) / 2);
          case "tpl" -> registers.put(inst[1], registers.get(inst[1]) * 3);
          case "inc" -> registers.merge(inst[1], 1, Integer::sum);
          case "jmp" -> pc += Integer.parseInt(inst[1]);
          case "jie", "jio" -> {
            boolean check = inst[0].charAt(2) == 'e'
                ? registers.get(inst[1]) % 2 == 0
                : registers.get(inst[1]) == 1;
            if (check) {
              pc += Integer.parseInt(inst[2]);
            } else {
              pc++;
            }
          }
        }
        if (inst[0].charAt(0) != 'j') {
          pc++;
        }
      }
      return registers;
    }
  }

  @Override
  String part1() {
    Computer computer = Computer.fromInput(input);
    return String.valueOf(computer.run(0).get("b"));
  }

  @Override
  String part2() {
    Computer computer = Computer.fromInput(input);
    return String.valueOf(computer.run(1).get("b"));
  }

  static void main() {
    Day day = new Day23() {
      @Override
      String getData() {
        return """
            inc b
            jio b, +2
            tpl b
            inc b
            inc a
            jie a, -3
            """;
      }
    };
    day.run("2", "7");
  }
}
