package com.automation.web.translationi18n;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Default implementation, requires an instance of key-value storage factory
 * to retrieve translations.
 */
public class DefaultI18n implements I18n {
    protected Locale locale;
    protected TranslationsStorageFactory<String, String> storageFactory;
    protected Storage<String, String> storage;
    protected boolean isLanguageChangeRequired;
    protected Set<TextProcessingOption> processingOptions = new LinkedHashSet<>();

    public DefaultI18n(TranslationsStorageFactory<String, String> storageFactory) {
        this.storageFactory = storageFactory;

        this.isLanguageChangeRequired = System.getProperty("locale") != null;

        String localeString = System.getProperty("locale", "en_GB");
        if (localeString.matches("\\w{2}[_-]\\w{2}")) {
            String[] localeParts = localeString.split("_|-"); // en-GB | en_GB
            this.setLocale(new Locale(localeParts[0], localeParts[1]));
        } else {
            this.setLocale(new Locale(localeString));
        }
    }

    public DefaultI18n(TranslationsStorageFactory<String, String> storageFactory, Locale locale) {
        this.storageFactory = storageFactory;
        this.isLanguageChangeRequired = locale != null;
        this.setLocale(locale);
    }

    @Override
    public I18n setOptions(TextProcessingOption... options) {
        processingOptions = new LinkedHashSet<>(Arrays.asList(options));
        return this;
    }

    @Override
    public I18n clearOptions() {
        processingOptions = new LinkedHashSet<>();
        return this;
    }

    @Override
    public String t(String key) {
        String value = storage.get(key);
        if (value == null) {
            throw new NoTranslationException(key, this.getLocale());
        }

        for (TextProcessingOption option : processingOptions) {
            switch (option) {
                case TRIM:
                    value = value.trim();
                    break;
                case STRIP_HTML:
                    value = stripHtml(value);
                    break;
            }
        }
        return value;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
        this.storage = storageFactory.create(locale);
    }

    @Override
    public Language getLanguage() {
        return Language.parse(getLocale().getDisplayLanguage());
    }

    @Override
    public boolean isLanguageChangeRequired() {
        return this.isLanguageChangeRequired;
    }

    protected String stripHtml(String s) {
        return s.replaceAll("</?\\w+>", "");
    }
}
