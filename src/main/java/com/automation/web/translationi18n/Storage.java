package com.automation.web.translationi18n;

/**
 * Generic key-value storage suitable for translation strings
 * @param <K> key
 * @param <V> value
 */
public interface Storage<K, V> {
    V get(K key);
}
