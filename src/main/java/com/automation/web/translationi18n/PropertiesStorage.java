package com.automation.web.translationi18n;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesStorage implements Storage<String, String> {
    private Properties properties = new Properties();

    public PropertiesStorage(String file) throws IOException {
        try (InputStream is = new FileInputStream(file)) {
            properties.load(new InputStreamReader(is, StandardCharsets.UTF_8));
        }
    }

    @Override
    public String get(String key) {
        return properties.getProperty(key);
    }
}
