package com.automation.web.pageobjects;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;

import com.automation.web.app.App;
import com.automation.web.helpers.GenericsHelper;
import com.automation.web.stereotype.LazyPrototype;
import com.automation.web.stereotype.LazySingleton;
import org.openqa.selenium.SearchContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Page object provides an abstract interface to a page or a section of the page and translates
 * business terms to UiFacade element interactions.
 *
 * Page object defines actions in general terms, e.g. `po.login(username, password)` and implements
 * those actions.
 *
 * @param <U> element map type
 * @param <V> validator type
 */
public abstract class PageObject<U extends UiFacade, V extends Validator>
        implements InitializingBean {

    @Autowired
    protected App app;

    /**
     * @deprecated use "ui" field instead of "map"
     */
    @Deprecated
    @Autowired
    protected U map;

    @Autowired
    protected U ui;

    @Autowired
    protected V validator;

    /**
     * Validate generic types and annotations
     */
    public PageObject() {
        Type[] genericTypes = GenericsHelper.extractGenericArguments(this.getClass());

        if (genericTypes == null) {
            throw new IllegalArgumentException("Please specify element map and validator: " +
                    "MyPageObject extends PageObject<MyUiFacade, MyValidator>");
        }

        Class<U> mapClass = (Class<U>) genericTypes[0];
        Class<V> validatorClass = (Class<V>) genericTypes[1];

        Class[] expectedAnnotations = new Class[]{LazySingleton.class, LazyPrototype.class};

        // check that page object, map, and validator are annotated with the same spring annotation
        // TODO: add link to confluence to exception messages
        for (Class<? extends Annotation> annotation : expectedAnnotations) {
            if (this.getClass().isAnnotationPresent(annotation) &&
                    (!mapClass.isAnnotationPresent(annotation)
                            || !validatorClass.isAnnotationPresent(annotation))) {
                throw new RuntimeException(
                        String.format("Annotation mismatch! If %s is annotated with @%s, " +
                                        "then %s and %s must also be annotated with it!",
                                this.getClass().getSimpleName(), annotation.getSimpleName(),
                                mapClass.getSimpleName(), validatorClass.getSimpleName()));
            }
        }

        // check that one and only one annotation from the expected annotations list is present
        if (Arrays.stream(expectedAnnotations).filter(annotation ->
                this.getClass().isAnnotationPresent(annotation)).count() != 1) {
            throw new RuntimeException(
                    String.format("PageObject object %s must be annotated with either @%s or @%s!",
                            this.getClass(),
                            LazyPrototype.class.getSimpleName(),
                            LazySingleton.class.getSimpleName()));
        }
    }

    /**
     * Assign the same element map to the validator map field
     */
    @Override
    public void afterPropertiesSet() {
        // TODO: instead of autowiring initialize page objects with proxies
        try {
            Field mapField = Validator.class.getDeclaredField("map");
            mapField.setAccessible(true);
            mapField.set(validator, map);

            Field uiField = Validator.class.getDeclaredField("ui");
            uiField.setAccessible(true);
            uiField.set(validator, ui);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Could not instantiate the page object!", e);
        }
    }

    /**
     * Ensure {@link Url} annotation is present and extract its value
     *
     * @param pageClass page to get URL from (subclass of {@link PageObject})
     * @return page url
     */
    public static String getUrl(Class<? extends PageObject> pageClass) {
        if (pageClass.isAnnotationPresent(Url.class)) {
            return pageClass.getDeclaredAnnotation(Url.class).value();
        } else {
            throw new RuntimeException(String.format(
                    "Page object %s has no @Url annotation!", pageClass.getSimpleName()));
        }
    }

    public V validator() {
        return validator;
    }

    public void refresh() {
        map.refresh();
        ui.refresh();
    }

    public void init(SearchContext context) {
        map.init(context);
        ui.init(context);
    }

    public void init() {
        map.init();
        ui.init();
    }
}
