package uk.co.bbc.fabric.common.bean.factory.config;

import static java.lang.System.getProperty;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;

public class EnvironmentPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

 private static final String PASSWORD = "password";
 private static final String DEFAULT_ENVIRONMENT = "";
 private static final String DEFAULT_APPLICATION_CONTEXT = "";
 private final static String SYSTEM_PROPERTY_ENV = "fabric.env.conf";
 private String environment;
 private String appcontext;
 private final Map<String, String> runtimePropertiesMappings = new HashMap<String, String>();

 
 public EnvironmentPropertyPlaceholderConfigurer() {
  environment = getProperty(SYSTEM_PROPERTY_ENV);
  if (environment == null) {
   logger.info("The system property '" + SYSTEM_PROPERTY_ENV
     + "' was null. Default environment will be applied.");
   environment = DEFAULT_ENVIRONMENT;
  }
  logger.debug("Environmental property has been initialised to : " + environment);
  setupRuntimeMappings();
 }

 /**
  * Allow multiple list locations
  * 
  * @param locations
  */
 public void setLocations(Resource[][] locations) {
  Set<Resource> marshalledLocations = new LinkedHashSet<Resource>();
  for (Resource[] resources : locations) {
   for (Resource resource : resources) {
    marshalledLocations.add(resource);
   }
  }
  super.setLocations(marshalledLocations.toArray(new Resource[marshalledLocations.size()]));
 }

 /**
  * Allow each application to have is own configuration context
  * 
  * @param applicationContext
  */
 public void setAppcontext(String appcontext) {
  this.appcontext = appcontext;
 }
 
 public String getAppcontext() {
  if (null == this.appcontext){
   return DEFAULT_APPLICATION_CONTEXT;
  }
  return this.appcontext;
 }
 
 @Override
 protected String resolvePlaceholder(String placeholder, Properties props) {
  final String envAppContextPlaceholder = environment + "." + getAppcontext() + "." + placeholder;
  final String appContextPlaceholder = getAppcontext() + "." + placeholder;
  final String envPlaceholder = environment + "." + placeholder;

  final String[] contextOrder = { envAppContextPlaceholder, appContextPlaceholder, envPlaceholder, placeholder };

  String result = null;
  String propertyNameUsed = null;

  for (String context : contextOrder) {
   logger.debug("Resolving property " + context);
   if (props.containsKey(context)) {
    propertyNameUsed = context;
    result = filterRuntimePropertyValue(props.getProperty(context));
    break;
   }
  }

  if (null == propertyNameUsed) {
   propertyNameUsed = placeholder;
  }
  
  if (propertyNameUsed.contains(PASSWORD)) {
   logger.debug("Property value " + propertyNameUsed + " = XXXXXXX");
  } else {
   logger.debug("Property value " + propertyNameUsed + " = " + result);
  }
  return result;
 }
 
 

 /**
  * Replaces any runtime placeholders with required values.
  * 
  * @param propertyValue
  * @return
  */
 private String filterRuntimePropertyValue(String propertyValue) {
  if (propertyValue != null) {
   for (String runtimePropertyName : runtimePropertiesMappings.keySet()) {
    propertyValue = propertyValue.replaceAll(runtimePropertyName,
      runtimePropertiesMappings.get(runtimePropertyName));
   }
  }
  return propertyValue;
 }

 private void setupRuntimeMappings() {
  String fabricTempFolder = new String("/" + System.getProperty("java.io.tmpdir")).replaceAll("\\\\", "/")
    .replaceAll("/{2}", "/");
  runtimePropertiesMappings.put("@fabric_temp_folder@", fabricTempFolder);
  logger.debug("Setting value fo @fabric_temp_folder@ to: " + fabricTempFolder);
 }

 public Map<Object, Object> getMappings() throws IOException {

  return mergeProperties();
 }
}
