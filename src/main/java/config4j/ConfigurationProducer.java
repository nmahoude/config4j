package config4j;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.json.JsonObject;

import config4j.annotation.Configuration;

@ApplicationScoped
public class ConfigurationProducer implements Serializable {
	private static final long serialVersionUID = 1L;
	
  Map<String, String> configs = new HashMap<>();
  private JsonObject jsonConfiguration;
  
  @PostConstruct
  public void setup() {
    jsonConfiguration = new ConfigReader().read();
  }

  private String getKey(InjectionPoint ip) {
    Configuration configuration = ip.getAnnotated().getAnnotation(Configuration.class);
    String key = configuration.name();
    if (key.isEmpty()) {
      throw new IllegalArgumentException("Configuration : no name is set @"+ip.toString());
    }
    return key;
  }
  
  @Produces
  @Configuration
  public String getString(InjectionPoint ip) {
    return JSON.getString(jsonConfiguration, getKey(ip));
  }
  
  @Produces 
  @Configuration
  public JsonObject getJson(InjectionPoint ip) {
    return JSON.get(jsonConfiguration,getKey(ip), JsonObject.class);
  }
  
  @Produces
  @Configuration
  public URI produceConfigurationURI(InjectionPoint ip) {
    try {
      return JsonToUri.build(getJson(ip));
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("Error : "+ getJson(ip).toString()+" is not a URI!");
    }
  }
}
