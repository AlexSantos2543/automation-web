package com.automation.web.translationi18n;

import java.util.Locale;

/**
 * A trivial implementation of the {@link I18n} interface.
 * Suitable for projects that do not deal with translations.
 * This implementation will simply return the passed string.
 */
public class SingleLanguageI18n implements I18n {
    protected Locale locale;

    public SingleLanguageI18n() {
        this.locale = Locale.ENGLISH;
    }

    public SingleLanguageI18n(Locale locale) {
        this.locale = locale;
    }

    @Override
    public I18n setOptions(TextProcessingOption... options) {
        return this;
    }

    @Override
    public I18n clearOptions() {
        return this;
    }

    @Override
    public String t(String key) {
        return key;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public Language getLanguage() {
        return Language.parse(getLocale().getLanguage());
    }

    @Override
    public boolean isLanguageChangeRequired() {
        return false;
    }
}
