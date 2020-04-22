package com.automation.web.translationi18n;

import lombok.Getter;

public enum Language {
    ENGLISH("en"),
    GERMAN("de"),
    FRENCH("fr"),
    SPANISH("es"),
    PORTUGUESE("pt");

    @Getter
    private String abbr;

    Language(String abbr) {
        this.abbr = abbr;
    }

    public static Language parse(String value) {
        try {
            return Language.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            for (Language language : Language.values()) {
                if (value.matches(String.format("^%s(-\\w+)?", language.abbr))) {
                    return language;
                }
            }
            throw new NoSuchLanguageException("Could not parse language: " + value);
        }
    }
}
