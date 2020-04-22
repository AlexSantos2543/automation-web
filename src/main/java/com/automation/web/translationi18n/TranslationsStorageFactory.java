package com.automation.web.translationi18n;

import java.util.Locale;

public interface TranslationsStorageFactory<K, V> {
    Storage<K, V> create(Locale locale);
}
