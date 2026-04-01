package advent2015;

public class Day12 extends Day {
  protected Day12() {
    super(12);
  }

  @Override
  String part1() {
    var sum = input.stream().mapToInt(line -> Support.integers(line).sum()).sum();
    return String.valueOf(sum);
  }

  static boolean isRedValue(Json.Element element) {
    return "red".equals(element.getAsString());
  }

  static int jsonValue(Json.Element element) {
    var array = element.getAsJsonArray();
    if (array != null) {
      return array.stream().mapToInt(Day12::jsonValue).sum();
    }
    var object = element.getAsJsonObject();
    if (object != null) {
      var values = object.getMap().values();
      if (values.stream().anyMatch(Day12::isRedValue)) {
        return 0;
      }
      return values.stream().mapToInt(Day12::jsonValue).sum();
    }
    var primitive = element.getAsJsonPrimitive();
    if (primitive != null && primitive.isNumber()) {
      return primitive.getAsInt();
    }
    return 0;
  }

  static int jsonSum(String line) {
    return jsonValue(Json.Element.fromJson(line));
  }

  @Override
  String part2() {
    var sum = input.stream().mapToInt(Day12::jsonSum).sum();
    return String.valueOf(sum);
  }

  static void main() {
    Day day = new Day12() {
      @Override
      String getData() {
        return """
            [1,2,3]
            [1,{"c":"red","b":2},3]
            {"d":"red","e":[1,2,3,4],"f":5}
            [1,"red",5]""";
      }
    };
    day.run("33", "16");
  }
}
