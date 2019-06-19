package config4j;

import java.net.URI;
import java.net.URISyntaxException;

import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonString;

public class JsonToUri {

  public static URI build(JsonObject json) throws URISyntaxException{
    return new URI("http://"
        +JSON.get(json, "/ip", JsonString.class).getString()
        +":"
        +JSON.get(json, "/port", JsonNumber.class).longValue()
        +JSON.get(json, "/uri", JsonString.class).getString());
  }
}
