package com.automation.web.setup;

import java.util.Locale;

public interface I18n {
    I18n setOptions(TextProcessingOption... options);
    I18n clearOptions();
    String t(String key);
    Locale getLocale();
    void setLocale(Locale locale);
    Language getLanguage();

    default boolean isLanguageChangeRequired() {
        return true;
    }
}