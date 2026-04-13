package org.example.utils;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigReader {

    private static Properties props = new Properties();

    static {
        try {
            String path = Paths.get("src", "test", "resources", "config", "config.properties").toString();
            try (InputStream in = new FileInputStream(path)) {
                props.load(in);
            }
        } catch (Exception e) {
            // If config not found, leave props empty
        }
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }
}
