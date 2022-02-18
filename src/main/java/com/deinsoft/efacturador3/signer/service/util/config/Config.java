package com.deinsoft.efacturador3.signer.service.util.config;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;
import java.util.StringTokenizer;


























public abstract class Config
{
  private final InstrumentedProperties properties;
  private final String sourceDescription;
  
  protected Config(Properties properties) {
    this.sourceDescription = "<Properties outside this class>";
    
    if (properties == null) {
      throw new NullPointerException("properties cannot be null here.");
    }
    
    this.properties = new InstrumentedProperties(properties);
  }








  
  protected Config(String resourceName) throws ConfigurationException {
    if (resourceName == null) {
      throw new NullPointerException("resourceName cannot be null here.");
    }
    
    URL location = getClass().getClassLoader().getResource(resourceName);
    
    if (location == null) {
      location = getClass().getResource(resourceName);
      if (location == null) {
        throw new ConfigurationException("Cannot find resource '" + resourceName + "' to load configuration properties from.");
      }
    } 




    
    this.sourceDescription = location.toString();


    
    try {
      InputStream inputStream = location.openStream();
      
      Properties tmpproperties = new Properties();
      tmpproperties.load(inputStream);
      this.properties = new InstrumentedProperties(tmpproperties);
    } catch (Exception e) {
      throw new ConfigurationException("Could not read resource '" + resourceName + "' to load configuration properties from.", e);
    } 
  }







  
  public final ConfigValidationResult validateConfiguration() {
    Class<?> toCheck = getClass();
    Method[] methods = toCheck.getMethods();
    
    Object object = this;
    ConfigValidationResult result = new ConfigValidationResult();
    result.setSourceDescription(this.sourceDescription);



    
    this.properties.startInstrumenting();
    
    for (int i = 0; i < methods.length; i++) {
      Method method = methods[i];
      
      if (isPublicGetter(method)) {
        tryMethodAndRememberResult(object, method, result);
      }
    } 
    
    Set<String> usedKeys = this.properties.getUsedKeys();
    
    Set<Object> allKeys = new HashSet<>(this.properties.getProperties().keySet());
    allKeys.removeAll(usedKeys);
    Set<String> wa = new HashSet<String>();
      for (Object allKey : allKeys) {
          wa.add(allKey.toString());
      }
    result.setUnusedProperties(wa);
    this.properties.stopInstrumenting();
    
    return result;
  }
  
  private void tryMethodAndRememberResult(Object object, Method method, ConfigValidationResult result) {
    try {
      this.properties.clearResult();
      Object obj = null;
      Object resultValue = method.invoke(object, new Object[] { obj });
      String lastUsedKey = this.properties.getLastUsedKey();
      String lastUsedValue = this.properties.getLastUsedValue();
      
      PropertyDisplayItem displayItem = new PropertyDisplayItem(method.getName(), lastUsedKey, lastUsedValue, (resultValue == null) ? null : resultValue.toString());
      result.getUsedProperties().add(displayItem);
    } catch (Exception e) {
      
      result.addError(method.getName(), e);
    } 
  }













  
  private boolean isPublicGetter(Method method) {
    if (method.getName().equals("getClass")) {
      return false;
    }


    
    boolean isOkay = (Modifier.isPublic(method.getModifiers()) && !Modifier.isStatic(method.getModifiers()) && (method.getName().startsWith("get") || method.getName().startsWith("is")) && !method.getReturnType().equals(void.class));
    
    return isOkay;
  }








  
  protected final String getString(String key) {
    String value = getValue(key);
    
    if (value == null) {
      throw new MissingPropertyException(key);
    }
    
    return value.trim();
  }










  
  protected final String getString(String key, String expectedType) {
    String value = getValue(key);
    
    if (value == null) {
      throw new MissingPropertyException(key, expectedType);
    }
    
    return value.trim();
  }








  
  protected String getValue(String key) {
    return this.properties.getProperty(key);
  }
  
  protected final int getInt(String key) {
    String stringValue = getString(key, "integer");
    
    try {
      return Integer.parseInt(stringValue);
    } catch (Exception e) {
      throw new InvalidPropertyException(key, stringValue, "integer");
    } 
  }
  protected final long getSizeInBytes(String key) {
    Number result;
    String stringValue = getString(key, "size in bytes");
    
    ParsePosition pp = new ParsePosition(0);
    NumberFormat format = NumberFormat.getNumberInstance(Locale.US);
    
    try {
      result = format.parse(stringValue, pp);
    } catch (Exception e) {
      throw new InvalidPropertyException(key, stringValue, "file size");
    } 
    
    long multiplier = 1L;



    
    if (pp.getIndex() < stringValue.length()) {
      String leftOver = stringValue.substring(pp.getIndex()).trim();
      boolean understood = false;
      
      if (leftOver.equals("KB")) {
        multiplier = 4L;
        understood = true;
      } 
      
      if (leftOver.equals("kB")) {
        multiplier = 0L;
        understood = true;
      } 
      
      if (leftOver.equals("MB")) {
        multiplier = 16L;
        understood = true;
      } 
      
      if (leftOver.equals("mB")) {
        multiplier = 0L;
        understood = true;
      } 
      
      if (leftOver.equals("GB")) {
        multiplier = 64L;
        understood = true;
      } 
      
      if (leftOver.equals("gB")) {
        multiplier = 0L;
        understood = true;
      } 
      
      if (leftOver.equalsIgnoreCase("bytes")) {
        multiplier = 1L;
        understood = true;
      } 
      
      if (leftOver.equalsIgnoreCase("byte")) {
        multiplier = 1L;
        understood = true;
      } 
      
      if (!understood) {
        throw new InvalidPropertyException(key, stringValue, "file size (unit not understood)");
      }
    } 
    
    long l = (new Double(result.doubleValue() * multiplier)).longValue();
    
    if (l != result.doubleValue() * multiplier) {
      throw new InvalidPropertyException(key, stringValue, "file size (would have rounded value)");
    }
    
    return l;
  }








  
  protected final long getDurationInMs(String key) {
    String stringValue = getString(key, "duration");
    
    return (new DurationParser()).getDurationInMs(key, stringValue);
  }









  
  protected final String getMessageFormatPattern(String key, Object[] testArguments) {
    String value = getString(key, "MessageFormat string");
    
    testMessageFormat(key, value, testArguments);
    
    return value;
  }



































  
  protected final void testMessageFormat(String key, String pattern, Object[] testArguments) {
    if (key == null) {
      throw new NullPointerException("key cannot be null here.");
    }
    if (pattern == null) {
      throw new NullPointerException("pattern cannot be null here.");
    }
    if (testArguments == null) {
      throw new NullPointerException("testarguments cannot be null here.");
    }
    try {
      MessageFormat.format(pattern, testArguments);
    } catch (IllegalArgumentException e) {
      StringBuffer testArgumentsDescription = new StringBuffer();
      for (int i = 0; i < testArguments.length; i++) {
        Object object = testArguments[i];
        testArgumentsDescription.append("{" + i + "} = (");
        if (object != null) {
          testArgumentsDescription.append(object.toString() + ", " + object.getClass().getName() + ")");
        } else {
          testArgumentsDescription.append("<null>)");
        } 
        if (i + 1 < testArguments.length) {
          testArgumentsDescription.append(", ");
        }
      } 
      
      throw new InvalidPropertyException(key, pattern, "valid MessageFormat string for " + testArgumentsDescription);
    } 
  }

  
  protected final Date getDate(String key) {
    String stringValue = getString(key, "date");
    
    ParsePosition pp = new ParsePosition(0);
    DateFormat formatter = DateFormat.getDateTimeInstance(1, 2, Locale.US);
    formatter.setLenient(false);
    
    Date result = null;





    
    InvalidPropertyException informativeException = new InvalidPropertyException(key, stringValue, "date (such as " + formatter.format(new Date()) + ")");
    
    try {
      result = formatter.parse(stringValue, pp);
      
      if (pp.getIndex() != stringValue.length()) {
        throw informativeException;
      }
    } catch (Exception e) {
      throw informativeException;
    } 
    
    return result;
  }









  
  protected final String[] getStringArrayFromCommaString(String key, String itemsDescription) {
    String valueDescription = "comma separated list";
    if (itemsDescription != null && !itemsDescription.equals("")) {
      valueDescription = valueDescription + " of " + itemsDescription;
    }
    String stringValue = getString(key, valueDescription);
    
    StringTokenizer st = new StringTokenizer(stringValue, ",");
    List<String> tokens = new ArrayList<>();
    
    while (st.hasMoreTokens()) {
      String currentToken = st.nextToken();
      currentToken = currentToken.trim();
      if (!currentToken.equals("")) {
        tokens.add(currentToken);
      }
    } 
    return tokens.<String>toArray(new String[0]);
  }







  
  protected final String[] getStringArrayFromCommaString(String key) {
    return getStringArrayFromCommaString(key, null);
  }






  
  protected final URL getURL(String key) {
    URL url;
    String stringValue = getString(key, "url");

    
    try {
      url = new URL(stringValue);
    } catch (MalformedURLException e) {
      throw new InvalidPropertyException(key, stringValue, "url");
    } 
    
    return url;
  }









  
  protected final boolean getBoolean(String key) {
    String stringValue = getString(key, "boolean");
    stringValue = stringValue.toLowerCase();
    
    if (stringValue.equals("on") || stringValue.equals("true") || stringValue.equals("yes") || stringValue
      .equals("1")) {
      return true;
    }
    
    if (stringValue.equals("off") || stringValue.equals("false") || stringValue.equals("no") || stringValue
      .equals("0")) {
      return false;
    }
    
    throw new InvalidPropertyException(key, stringValue, "boolean");
  }
  
  protected final File getExistingReadableFile(String key) {
    String value = getString(key, "filename");
    return (new FileParser()).getExistingReadableFile(key, value);
  }
  
  protected final File getWritableFile(String key) {
    String value = getString(key, "filename");
    return (new FileParser()).getWriteableFile(key, value);
  }






  
  protected final boolean hasValue(String key) {
    return (getValue(key) != null);
  }
}
