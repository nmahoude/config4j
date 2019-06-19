package config4j;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class ConfigReader {
  private static final String CONFIG4J_LOCATION = "CONFIG4J";
  private static final String CONFIG4J_DEFAULT_FILENAME = "config4j.json";
  
  public JsonObject read() {
    String filename = System.getenv(CONFIG4J_LOCATION);
    if (filename == null) {
      Logger.getAnonymousLogger().log(Level.INFO, CONFIG4J_LOCATION+" is not set, using default value "+CONFIG4J_DEFAULT_FILENAME);
      filename = CONFIG4J_DEFAULT_FILENAME;
    }

    return readConfigFromFilename(filename);
  }

  private JsonObject readConfigFromFilename(String filename) {
    try {
      JsonReader reader = Json.createReader(new FileReader(filename));
      return reader.readObject();
    } catch (FileNotFoundException e) {
      Logger.getAnonymousLogger().log(Level.SEVERE, "Can't find config file "+filename);
      throw new IllegalArgumentException("Can't find config file "+filename);
    }
  }
}
