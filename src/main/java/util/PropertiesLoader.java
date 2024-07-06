package util;

import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Slf4j
public class PropertiesLoader {
    private final Properties properties;

    public PropertiesLoader(String filePath) {
        properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            log.error("Error loading file properties", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}


