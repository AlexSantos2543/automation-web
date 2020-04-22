package com.automation.web.translationi18n;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class JsonStorage implements Storage<String, String> {
    private Map<String, String> translations;

    public JsonStorage(String file) throws IOException {
        Gson gson = new GsonBuilder().create();
        translations = gson.<Map<String, String>>fromJson(
            new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8), Map.class);
    }

    @Override
    public String get(String key) {
        return translations.get(key);
    }
}
