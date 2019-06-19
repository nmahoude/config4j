package config4j;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonString;

import org.junit.Test;

public class JSONTest {


  @Test(expected=IllegalArgumentException.class)
  public void pathNotFound_throws_exception() throws Exception {
    JsonObject json = Json.createObjectBuilder()
        .add("value", "one")
        .build();
    
    JsonString value = JSON.get(json, "anotherValue", JsonString.class);
  }

  @Test
  public void getJsonValueFromRoot() throws Exception {
    JsonObject json = Json.createObjectBuilder()
        .add("value", "one")
        .build();
    
    JsonString value = JSON.get(json, "value", JsonString.class);
    
    assertThat(value.getString(), is ("one"));
  }
  
  @Test
  public void nestedValueIsFoundWithDotNotation() throws Exception {
    JsonObject json = Json.createObjectBuilder()
        .add("value", Json.createObjectBuilder()
            .add("nested", "theValue")
            .build())
        .build();
    
    JsonString value = JSON.get(json, "value.nested", JsonString.class);
    
    assertThat(value.getString(), is ("theValue"));
  }
  
  
  @Test
  public void accessArrayDirectly_returns_jsonArray() throws Exception {
    JsonArray subArray = Json.createArrayBuilder()
        .add("1")
        .add("2")
        .build();
    
    JsonObject json = Json.createObjectBuilder()
        .add("array", subArray)
        .build();
    
    JsonArray value = JSON.get(json, "array", JsonArray.class);
    assertThat(value, is (subArray));
  }

  @Test
  public void accessValueInArrayByIndex_return_value() throws Exception {
    JsonObject json = Json.createObjectBuilder()
        .add("array", Json.createArrayBuilder()
            .add("value1")
            .add("value2")
            .build())
        .build();
    
    JsonString value = JSON.get(json, "array[0]", JsonString.class);
    assertThat(value.getString(), is ("value1"));
    
    value = JSON.get(json, "array[1]", JsonString.class);
    assertThat(value.getString(), is ("value2"));
  }
  
  @Test
  public void accessArrayIn2Steps_is_valid() {
    JsonObject json = Json.createObjectBuilder()
        .add("array", Json.createArrayBuilder()
            .add("value1")
            .add("value2")
            .build())
        .build();
    
    JsonString value = JSON.get(json, "array.[0]", JsonString.class);
    assertThat(value.getString(), is ("value1"));
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void readOutOfBoundArrayIndexThrowsException() throws Exception {
    JsonObject json = Json.createObjectBuilder()
        .add("array", Json.createArrayBuilder()
            .add("1")
            .add("2")
            .build())
        .build();
    
    JSON.get(json, "array[2]", JsonString.class);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void errorIfMalformedArrayQuery() throws Exception {
    JsonObject json = Json.createObjectBuilder()
        .add("array", Json.createArrayBuilder()
            .add("1")
            .add("2")
            .build())
        .build();
    
    JSON.get(json, "array[0)", JsonString.class);
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void unknownSubValueThrowsException() throws Exception {
    JsonObject json = Json.createObjectBuilder()
        .add("value", "one")
        .build();
    
    JsonString value = JSON.get(json, "value.isNotExistent", JsonString.class);
    
    assertThat(value.getString(), is (nullValue()));
  }
}
