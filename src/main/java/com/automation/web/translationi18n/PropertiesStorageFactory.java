package com.automation.web.translationi18n;

import java.io.IOException;
import java.util.Locale;

/**
 * Creates properties based storage.
 * Uses provided format string and locale to construct the filename,
 * then creates an instance of storage with that filename.
 */
public class PropertiesStorageFactory implements TranslationsStorageFactory<String, String> {
    private String filenameTemplate;

    public PropertiesStorageFactory(String filenameFormat) {
        this.filenameTemplate = filenameFormat;
    }

    @Override
    public Storage<String, String> create(Locale locale) {
        try {
            return new PropertiesStorage(String.format(filenameTemplate, locale.getLanguage()));
        } catch (IOException e) {
            throw new RuntimeException("Could not read from file", e);
        }
    }
}
