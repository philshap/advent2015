package advent2015;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day15 extends Day {
  protected Day15() {
    super(15);
  }

  static void findCombinations(int remaining, int target,
                               List<Integer> current, List<List<Integer>> result) {
    // Base case: last number
    if (remaining == 1) {
      if (target > 0) {
        List<Integer> combo = new ArrayList<>(current);
        combo.add(target);
        result.add(combo);
      }
      return;
    }

    // Recursive case: try all valid values for current position
    for (int i = 1; i < target - (remaining - 1); i++) {
      current.add(i);
      findCombinations(remaining - 1, target - i, current, result);
      current.removeLast();
    }
  }

  static List<List<Integer>> amountCombos(int n) {
    List<List<Integer>> result = new ArrayList<>();
    findCombinations(n, 100, new ArrayList<>(), result);
    return result;
  }

  record Ingredient(List<Integer> contents) {
    int score() {
      return contents.subList(0, contents.size() - 1).stream().reduce(1, (x, y) -> x * y);
    }
    int calories() {
      return contents.getLast();
    }
  }

  Stream<Ingredient> mix(List<Ingredient> ingredients, List<Integer> amounts) {
    List<Integer> mixed = new ArrayList<>();
    for (int j = 0; j < ingredients.getFirst().contents.size(); j++) {
      int finalJ = j;
      mixed.add(IntStream.range(0, ingredients.size()).map(i -> ingredients.get(i).contents.get(finalJ) * amounts.get(i)).sum());
    }
    if (mixed.stream().anyMatch(l -> l < 0)) {
      return Stream.of();
    }
    return Stream.of(new Ingredient(mixed));
  }

  List<Ingredient> getIngredients() {
    return Support.partition(Support.integers(data).boxed().toList(), 5).map(Ingredient::new).toList();
  }

  @Override
  String part1() {
    List<Ingredient> ingredients = getIngredients();
    int bestCookie =
        amountCombos(ingredients.size()).stream()
            .flatMap(amounts -> mix(ingredients, amounts))
            .mapToInt(Ingredient::score)
            .max()
            .orElseThrow();

    return String.valueOf(bestCookie);
  }

  @Override
  String part2() {
    List<Ingredient> ingredients = getIngredients();
    int bestCookie = amountCombos(ingredients.size()).stream()
            .flatMap(amounts -> mix(ingredients, amounts))
            .filter(mix -> mix.calories() == 500)
            .mapToInt(Ingredient::score)
            .max()
            .orElseThrow();

    return String.valueOf(bestCookie);
  }

  static void main() {
    Day day = new Day15() {
      @Override
      String getData() {
        return """
            Butterscotch: capacity -1, durability -2, flavor 6, texture 3, calories 8
            Cinnamon: capacity 2, durability 3, flavor -2, texture -1, calories 3""";
      }
    };
    System.out.println(day.part1());
    System.out.println(day.part2());
  }
}
