package advent2015;

import java.util.stream.Stream;

public class Day11 extends Day {
  protected Day11() {
    super(11);
  }

  static String next(String s) {
    char c = s.charAt(s.length() - 1);
    String leading = s.substring(0, s.length() - 1);
    if (c != 'z') {
      return leading + (char) (c + 1);
    }
    return next(leading) + 'a';
  }

  static boolean valid(String s) {
    boolean straight = false;
    char pair1 = 0;
    char pair2 = 0;
    char first = 0;
    for (int i = 0; i < s.length(); i++) {
      char ch = s.charAt(i);
      if (ch == 'i' || ch == 'o' || ch == 'l') {
        return false;
      }
      if (!straight && i < s.length() - 2) {
        straight = ch + 1 == s.charAt(i + 1) && ch + 2 == s.charAt(i + 2);
      }
      if (pair1 == 0) {
        if (first == 0) {
          first = ch;
        } else if (first == ch) {
          pair1 = ch;
          first = 0;
        } else {
          first = ch;
        }
      } else if (pair2 == 0 && pair1 != ch) {
        if (first == 0) {
          first = ch;
        } else if (first == ch) {
          pair2 = ch;
          first = 0;
        } else {
          first = ch;
        }
      }
    }
    return straight && pair1 != 0 && pair2 != 0;
  }

  @Override
  String part1() {
    return Stream.iterate(input.get(0), Day11::next)
                 .skip(1)
                 .filter(Day11::valid)
                 .findFirst()
                 .orElseThrow();
  }

  @Override
  String part2() {
    return Stream.iterate(input.get(0), Day11::next)
                 .skip(1)
                 .filter(Day11::valid)
                 .skip(1)
                 .findFirst()
                 .orElseThrow();
  }
}
