package util;

import lombok.NoArgsConstructor;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@NoArgsConstructor
public final class PropertiesUtil {
     private static final Properties PROPERTIES = new Properties();

     static {
         loadProperties();
     }

    private static void loadProperties() {
        try (InputStream stream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
             PROPERTIES.load(stream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static String get(String key){
         return PROPERTIES.getProperty(key);
    }
    public static String get(List<String> keys){
       return keys.stream().map(key->PROPERTIES.getProperty(key)).collect(Collectors.joining(""));
    }
}
