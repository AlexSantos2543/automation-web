package com.automation.web.pageobjects.factory;

import io.appium.java_client.pagefactory.bys.ContentMappedBy;
import io.appium.java_client.pagefactory.bys.ContentType;
import io.appium.java_client.pagefactory.locator.CacheableLocator;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.IllegalFormatException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.openqa.selenium.support.pagefactory.ElementLocator;

/**
 * Element locator that formats its `formatArg` using the By strategy as a format string, e.g. a
 * locator with strategy `By.css("item%s")` and `1` passed as the format argument will be formatted
 * into a locator with strategy `By.css("item1")`.
 *
 * This allows for dynamic element lookup.
 */
public class FormattedElementLocator implements CacheableLocator {

    protected final SearchContext searchContext;
    protected final boolean shouldCache;
    protected final By by;
    protected WebElement cachedElement;
    protected List<WebElement> cachedElementList;

    public FormattedElementLocator(ElementLocator locator, Object formatArg) {
        Map<String, Field> fields = Arrays
                .stream(locator.getClass().getDeclaredFields())
                .collect(Collectors.toMap(Field::getName, Function.identity()));

        try {
            Field byField = fields.get("by");
            byField.setAccessible(true);
            By by = (By) byField.get(locator);
            this.by = formatBy(by, formatArg);

            Field searchContextField = fields.get("searchContext");
            searchContextField.setAccessible(true);
            this.searchContext = (SearchContext) searchContextField.get(locator);

            Field shouldCacheField = fields.get("shouldCache");
            shouldCacheField.setAccessible(true);
            this.shouldCache = (boolean) shouldCacheField.get(locator);


        } catch (IllegalAccessException e) {
            throw new FindByFormattingException(e);
        }
    }

    protected By formatBy(By originalBy, Object formatArg) throws IllegalAccessException {
        if (ByChained.class.isAssignableFrom(originalBy.getClass())) {
            return formatChainedBy((ByChained) originalBy, formatArg);

        } else if (ContentMappedBy.class.isAssignableFrom(originalBy.getClass())) {
            return formatMappedBy((ContentMappedBy) originalBy, formatArg);

        } else {
            return formatSimpleBy(originalBy, formatArg);
        }
    }

    @SuppressWarnings("unchecked")
    protected ContentMappedBy formatMappedBy(ContentMappedBy originalBy, Object formatArg) {
        Map<String, Field> fields = Arrays
                .stream(originalBy.getClass().getDeclaredFields())
                .collect(Collectors.toMap(Field::getName, Function.identity()));

        try {
            Field mapField = fields.get("map");
            mapField.setAccessible(true);
            Map<ContentType, By> originalMap = (Map<ContentType, By>) mapField.get(originalBy);

            Field currentContentField = fields.get("currentContent");
            currentContentField.setAccessible(true);
            ContentType originalCurrentContent = (ContentType) currentContentField.get(originalBy);

            By defaultBy = formatBy(originalMap.get(ContentType.HTML_OR_DEFAULT), formatArg);
            By mobileBy = formatBy(originalMap.get(ContentType.NATIVE_MOBILE_SPECIFIC), formatArg);

            Map<ContentType, By> map = new EnumMap<>(ContentType.class);
            map.put(ContentType.HTML_OR_DEFAULT, defaultBy);
            map.put(ContentType.NATIVE_MOBILE_SPECIFIC, mobileBy);

            return (ContentMappedBy) new ContentMappedBy(map).useContent(originalCurrentContent);
        } catch (IllegalAccessException e) {
            throw new FindByFormattingException(e);
        }
    }

    protected ByChained formatChainedBy(ByChained originalBy, Object formatArg)
            throws IllegalAccessException {
        Field[] fields = originalBy.getClass().getDeclaredFields();

        Field bysField = Arrays.stream(fields)
                .filter(field -> field.getName().equals("bys"))
                .findFirst().orElseThrow(() -> new RuntimeException(""));
        bysField.setAccessible(true);
        By[] originalBys = (By[]) bysField.get(originalBy);

        By[] bys = Arrays.stream(originalBys)
                .map(by -> formatSimpleBy(by, formatArg))
                .toArray(By[]::new);
        return new ByChained(bys);
    }

    /**
     * Constructs a copy of By with its strategy formatted using String.format and the argument
     *
     * @param originalBy By with a strategy expressed as a format string
     * @param formatArg argument for String.format
     * @return formatted By
     */
    @SuppressWarnings("unchecked")
    protected By formatSimpleBy(By originalBy, Object formatArg) {
        List<Field> stringFields = new ArrayList<>();
        Class<?> byType = originalBy.getClass();

        while (byType != Object.class && stringFields.isEmpty()) {
            stringFields = Arrays.stream(byType.getDeclaredFields())
                    .filter(field -> field.getType().equals(String.class)
                            && !Modifier.isStatic(field.getModifiers()))
                    .collect(Collectors.toList());

            byType = byType.getSuperclass();
        }

        Class<String>[] parameterTypes = stringFields.stream()
                .map(f -> String.class)
                .toArray(Class[]::new);

        List<String> parameters = new LinkedList<>();
        try {
            for (Field field : stringFields) {
                field.setAccessible(true);
                String value = (String) field.get(originalBy);
                parameters.add(formatOptional(value, formatArg));
            }
            Constructor<? extends By> constructor = originalBy.getClass()
                    .getDeclaredConstructor(parameterTypes);
            return constructor.newInstance(parameters.toArray());
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new FindByFormattingException(e);
        }
    }

    protected String formatOptional(String value, Object formatArg) {
        if (value == null) {
            return null;
        }
        try {
            return String.format(value, formatArg);
        } catch (IllegalFormatException e) {
            return value;
        }
    }

    /**
     * Find the element.
     */
    public WebElement findElement() {
        if (cachedElement != null && isLookUpCached()) {
            return cachedElement;
        }

        WebElement element = searchContext.findElement(by);
        if (isLookUpCached()) {
            cachedElement = element;
        }

        return element;
    }

    /**
     * Find the element list.
     */
    public List<WebElement> findElements() {
        if (cachedElementList != null && isLookUpCached()) {
            return cachedElementList;
        }

        List<WebElement> elements = searchContext.findElements(by);
        if (isLookUpCached()) {
            cachedElementList = elements;
        }

        return elements;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " '" + by + "'";
    }

    @Override
    public boolean isLookUpCached() {
        return shouldCache;
    }

    public static class FindByFormattingException extends RuntimeException {

        public FindByFormattingException() {
            super();
        }

        public FindByFormattingException(String message) {
            super(message);
        }

        public FindByFormattingException(String message, Throwable cause) {
            super(message, cause);
        }

        public FindByFormattingException(Throwable cause) {
            super(cause);
        }

        protected FindByFormattingException(String message, Throwable cause,
                boolean enableSuppression, boolean writableStackTrace) {
            super(message, cause, enableSuppression, writableStackTrace);
        }
    }
}
