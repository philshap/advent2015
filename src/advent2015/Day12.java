package advent2015;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Day12 extends Day {
  protected Day12() {
    super(12);
  }

  @Override
  String part1() {
    var sum = input.stream().mapToInt(line -> Support.integers(line).sum()).sum();
    return String.valueOf(sum);
  }

  static Gson gson = new Gson();

  static boolean isRedValue(JsonElement element) {
    return element.isJsonPrimitive() && element.getAsString().equals("red");
  }

  static int jsonValue(JsonElement element) {
    if (element.isJsonArray()) {
      int sum = 0;
      for (JsonElement jsonElement : element.getAsJsonArray()) {
        sum += jsonValue(jsonElement);
      }
      return sum;
    }
    if (element.isJsonObject()) {
      var object = element.getAsJsonObject();
      int sum = 0;
      for (var entry : object.entrySet()) {
        var value = entry.getValue();
        if (isRedValue(value)) {
          return 0;
        }
        sum += jsonValue(value);
      }
      return sum;
    }
    if (element.isJsonPrimitive()) {
      var primitive = element.getAsJsonPrimitive();
      if (primitive.isNumber()) {
        return primitive.getAsInt();
      } else {
        return 0;
      }
    }
    return 0;
  }

  static int jsonSum(String line) {
    return jsonValue(gson.fromJson(line, JsonElement.class));
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
