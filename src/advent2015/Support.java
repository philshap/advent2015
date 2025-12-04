package advent2015;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public interface Support {
  Pattern NUMBER = Pattern.compile("(-?\\d+)");

  static <T> Stream<List<T>> partition(List<T> source, int length) {
    return partition(source, length, 0);
  }

  static <T> Stream<List<T>> partition(List<T> source, int length, int overlap) {
    return IntStream.range(0, (source.size() - length) / (length - overlap) + 1)
        .mapToObj(n -> source.subList(n * (length - overlap), n * (length - overlap) + length));
  }

  static <T> List<List<T>> permutations(List<T> list) {
    List<List<T>> result = new ArrayList<>();
    backtrack(result, new ArrayList<>(), list);
    return result;
  }

  static <T> void backtrack(List<List<T>> result, List<T> tempList, List<T> list) {
    if (tempList.size() == list.size()) {
      result.add(new ArrayList<>(tempList));
    } else {
      for (int i = 0; i < list.size(); i++) {
        if (tempList.contains(list.get(i))) {
          // element already exists, skip
          continue;
        }
        tempList.add(list.get(i));
        backtrack(result, tempList, list);
        tempList.remove(tempList.size() - 1);
      }
    }
  }

  static IntStream integers(String input) {
    return NUMBER.matcher(input).results().map(MatchResult::group).mapToInt(Integer::parseInt);
  }

  static List<String> splitInput(String input) {
    return Arrays.asList(input.split("\n"));
  }
}
