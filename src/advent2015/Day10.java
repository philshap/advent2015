package advent2015;

public class Day10 extends Day {
  protected Day10() {
    super(10);
  }

  String lookAndSay(String sequence) {
    StringBuilder buf = new StringBuilder();
    char digit = 0;
    int count = 0;
    for (int index = 0; index < sequence.length(); index++) {
      char ch = sequence.charAt(index);
      if (digit != ch) {
        if (count != 0) {
          buf.append(count).append(digit);
        }
        digit = ch;
        count = 1;
      } else {
        count++;
      }
    }
    buf.append(count).append(digit);
    return buf.toString();
  }

  @Override
  String part1() {
    int times = 40;
    String sequence = input.get(0);
    while (times-- != 0) {
      sequence = lookAndSay(sequence);
    }
    return String.valueOf(sequence.length());
  }

  @Override
  String part2() {
    int times = 50;
    String sequence = input.get(0);
    while (times-- != 0) {
      sequence = lookAndSay(sequence);
    }
    return String.valueOf(sequence.length());
  }
}
