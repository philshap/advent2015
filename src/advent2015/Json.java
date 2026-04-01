package advent2015;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Json {
  static class Element {
    final int end;

    public Element(int end) {
      this.end = end;
    }

    static Element fromJson(String data) {
      return fromText(data, 0);
    }

    static Element fromText(String data, int offset) {
      return switch (data.charAt(offset)) {
        case '{' -> Object.fromText(data, offset);
        case '[' -> Array.fromText(data, offset);
        default -> Primitive.fromText(data, offset);
      };
    }

    public String getAsString() {
      return null;
    }

    public List<Element> getAsJsonArray() {
      return null;
    }

    public Object getAsJsonObject() {
      return null;
    }

    public Primitive getAsJsonPrimitive() {
      return null;
    }
  }

  static class Object extends Element {
    final Map<String, Element> map;
    public Object(int end, Map<String, Element> map) {
      super(end);
      this.map = map;
    }

    public Map<String, Element> getMap() {
      return map;
    }

    public Object getAsJsonObject() {
      return this;
    }

    static Object fromText(String data, int start) {
      Map<String, Element> map = new HashMap<>();
      int end = start + 1;
      do {
        Primitive key = Primitive.fromText(data, end);
        // +1 skip ':'
        Element value = Element.fromText(data, key.end + 1);
        map.put(key.getAsString(), value);
        end = value.end;
      } while (data.charAt(end++) != '}');
      return new Object(end, map);
    }
  }

  static class Array extends Element {
    final List<Element> values;
    Array(int end, List<Element> values) {
      super(end);
      this.values = values;
    }

    public List<Element> getAsJsonArray() {
      return values;
    }

    static Array fromText(String data, int start) {
      List<Element> values = new ArrayList<>();
      int end = start + 1;
      do {
        Element value = Element.fromText(data, end);
        values.add(value);
        end = value.end;
      } while (data.charAt(end++) != ']');
      return new Array(end, values);
    }
  }

  static class Primitive extends Element {
    final Integer number;
    final String string;

    public Primitive(String data, int start, int end, boolean isNumber) {
      super(end);
      if (isNumber) {
        number = Integer.parseInt(data.substring(start, end));
        string = null;
      } else {
        number = null;
        string = data.substring(start + 1, end - 1);
      }
    }

    static Primitive fromText(String data, int start) {
      int end = start + 1;
      boolean isNumber;
      if (data.charAt(start) == '"') {
        isNumber = false;
        while (data.charAt(end++) != '"');
      } else {
        isNumber = true;
        char ch;
        do {
          ch = data.charAt(end++);
        } while ('0' <= ch && ch <= '9');
        end--;
      }
      return new Primitive(data, start, end, isNumber);
    }

    @Override
    public Primitive getAsJsonPrimitive() {
      return this;
    }

    public boolean isNumber() {
      return number != null;
    }

    public int getAsInt() {
      return number;
    }

    public String getAsString() {
      return string;
    }
  }
}
