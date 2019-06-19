package config4j;

import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonStructure;
import javax.json.JsonValue;

public class JSON {
  private static final String VALUE_SPLITTER = "\\.";

  public static String getString(JsonObject jsonConfiguration, String key) {
    String value = get(jsonConfiguration, key, JsonValue.class).toString();
    return value.substring(1, value.length()-1);
  }

  
  public static <T extends JsonValue> T get(JsonStructure structure, String path, Class<T> type) {

    String segments[] = path.split(VALUE_SPLITTER);
    JsonValue currentValue = structure;
    for (String segment : segments) {

      if (segment.length() == 0) {
        continue;
      }

      if (currentValue instanceof JsonObject) {
        JsonObject currentObject = (JsonObject) currentValue;
        if (segment.contains("[")) {
          currentValue = currentObject.getJsonArray(segment.substring(0, segment.indexOf("[")));
          if (segment.charAt(segment.length() - 1) != ']') {
            throw illegalArray(path);
          }
          int index = Integer.parseInt(segment.substring(segment.indexOf("[") + 1, segment.length() - 1));
          if (index >= ((JsonArray) currentValue).size()) {
            throw new IllegalArgumentException(String.format("trying to access outOfBound array %s with path %s : index %d in %s", structure.toString(), path, index, currentValue.toString()));
          }
          currentValue = ((JsonArray) currentValue).get(index);
        } else {
          currentValue = currentObject.get(segment);
        }
      } else if (currentValue instanceof JsonArray) {
        if (segment.startsWith("[") && segment.endsWith("]")) {
          int index = Integer.parseInt(segment.substring(1, segment.length() - 1));
          currentValue = ((JsonArray) currentValue).get(index);
        } else {
          throw illegalArray(segment);
        }
      } else {
        throw illegalPath(path);
      }

    }

    if (currentValue == null) {
      throw illegalPath(path);
    }
    return type.cast(currentValue);

  }

  private static IllegalArgumentException illegalPath(String path) {
    return new IllegalArgumentException(String.format("unknown path %s", path));
  }
  
  private static IllegalArgumentException illegalArray(String segment) {
    return new IllegalArgumentException(String.format("Array type requires key of the form [n], was", segment));
  }

}
