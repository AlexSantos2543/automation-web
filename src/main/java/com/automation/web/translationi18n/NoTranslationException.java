package com.automation.web.translationi18n;

import java.util.Locale;

public class NoTranslationException extends RuntimeException {
    public NoTranslationException(String key, Locale locale) {
        super(String.format("No translation found for key %s, language %s", key, locale.getLanguage()));
    }

    public NoTranslationException(String key, Locale locale, Throwable cause) {
        super(String.format("No translation found for key %s, language %s", key, locale.getLanguage()), cause);
    }
}
