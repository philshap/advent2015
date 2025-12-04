package advent2015;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {

  private static Stream<String> days() {
    return IntStream.range(1, 25).mapToObj("advent2015.Day%s"::formatted);
  }

  private void runDays() {
    days()
        .map(name -> {
          try {
            return (Day) Class.forName(name).getDeclaredConstructors()[0].newInstance();
          } catch (Exception e) {
            return null;
          }
        })
        .filter(Objects::nonNull)
        .forEach(Day::run);
  }

  private void testDays() {
    days()
        .forEach(name -> {
          try {
            Method main = Class.forName(name).getDeclaredMethod("main");
            main.setAccessible(true);
            main.invoke(null);
          } catch (Exception e) {
            // ignore errors
          }
        });
  }

  void main(String[] args) {
    if (args.length == 1 && args[0].equals("test")) {
      testDays();
    } else {
      runDays();
    }
  }
}
