package com.automation.web.pageobjects.decorator;

import com.automation.web.helpers.GenericsHelper;
import com.automation.web.pageobjects.NotDecorated;
import com.automation.web.pageobjects.factory.FieldInitializer;
import com.automation.web.pageobjects.factory.FormattedElementLocator;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AndroidFindByAllSet;
import io.appium.java_client.pagefactory.AndroidFindBySet;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;
import io.appium.java_client.pagefactory.iOSXCUITFindByAllSet;
import io.appium.java_client.pagefactory.iOSXCUITFindBySet;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Function;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.interactions.Locatable;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementHandler;
import org.openqa.selenium.support.pagefactory.internal.LocatingElementListHandler;

/**
 * Decorator for use with {@link FieldInitializer}. Will decorate: 1) all of the {@literal T}
 * fields, and 2) all of the {@literal List<T>} fields Fields must have {@literal @FindBy},
 * {@literal @FindBys}, or {@literal @FindAll} annotation.
 *
 * This will use annotation-provided locator to find web elements and use them as contexts for the
 * decorated fields.
 *
 * @param <T> base decoratable type
 */
public abstract class AnyFieldDecorator<T> implements FieldDecorator {

    private final Class<? extends T> decoratableClass;
    protected ElementLocatorFactory factory;

    @SuppressWarnings("unchecked")
    public AnyFieldDecorator(ElementLocatorFactory factory) {
        this.factory = factory;

        Type[] typeArgs = GenericsHelper.extractGenericArguments(this.getClass());
        this.decoratableClass = (Class<? extends T>) typeArgs[0];
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object decorate(ClassLoader loader, Field field) {
        if (field.isAnnotationPresent(NotDecorated.class)) {
            return null;
        }

        if (!isDecoratableField(field)) {
            return null;
        }

        ElementLocator locator = factory.createLocator(field);
        if (locator == null) {
            return null;
        }

        if (decoratableClass.isAssignableFrom(field.getType())) {
            return createOne((Class<? extends T>) field.getType(), loader, locator);

        } else if (List.class.isAssignableFrom(field.getType())) {
            Type genericType = field.getGenericType();
            Type listType = ((ParameterizedType) genericType).getActualTypeArguments()[0];
            return createMany((Class<? extends T>) listType, loader, locator);

        } else if (Function.class.isAssignableFrom(field.getType())) {
            Type genericType = field.getGenericType();
            Type argumentType = ((ParameterizedType) genericType).getActualTypeArguments()[0];
            Type returnType = ((ParameterizedType) genericType).getActualTypeArguments()[1];

            return new Function<Object, Object>() {
                @Override
                public Object apply(Object arg) {
                    ElementLocator parameterizedLocator =
                            new FormattedElementLocator(locator, arg);
                    return createOne((Class<? extends T>) returnType, loader, parameterizedLocator);
                }

                @Override
                public String toString() {
                    return String.format("Function<%s, %s>(%s)",
                            argumentType.getTypeName(), returnType.getTypeName(), super.toString());
                }
            };
        }
        return null;
    }

    protected boolean isDecoratableField(Field field) {
        if (decoratableClass.isAssignableFrom(field.getType())) {
            // single element of a decoratable type
            return isAnnotatedWithFindBy(field);
        }

        if (!List.class.isAssignableFrom(field.getType())
                && !Function.class.isAssignableFrom(field.getType())) {
            // not a list or a function, cannot be decorated
            return false;
        }

        // Type erasure in Java isn't complete. Attempt to discover the generic
        // type of the list or function.
        Type genericType = field.getGenericType();
        if (!(genericType instanceof ParameterizedType)) {
            // the field is of a raw type, cannot decorate it
            return false;
        }

        if (List.class.isAssignableFrom(field.getType())) {
            Type listType = ((ParameterizedType) genericType).getActualTypeArguments()[0];
            if (!(decoratableClass.isAssignableFrom((Class) listType))) {
                return false;
            }
        }

        if (Function.class.isAssignableFrom(field.getType())) {
            Type returnType = ((ParameterizedType) genericType).getActualTypeArguments()[1];
            if (!(decoratableClass.isAssignableFrom((Class) returnType))) {
                return false;
            }
        }

        return isAnnotatedWithFindBy(field);
    }

    protected boolean isAnnotatedWithFindBy(Field field) {
        return field.isAnnotationPresent(FindBy.class)
                || field.isAnnotationPresent(FindBys.class)
                || field.isAnnotationPresent(FindAll.class)
                || field.isAnnotationPresent(AndroidFindBy.class)
                || field.isAnnotationPresent(AndroidFindBySet.class)
                || field.isAnnotationPresent(AndroidFindByAllSet.class)
                || field.isAnnotationPresent(iOSXCUITFindBy.class)
                || field.isAnnotationPresent(iOSXCUITFindBySet.class)
                || field.isAnnotationPresent(iOSXCUITFindByAllSet.class);
    }

    protected WebElement proxyForLocator(ClassLoader loader, ElementLocator locator) {
        InvocationHandler handler = new LocatingElementHandler(locator);

        WebElement proxy;
        proxy = (WebElement) Proxy.newProxyInstance(
                loader, new Class[]{WebElement.class, WrapsElement.class, Locatable.class},
                handler);
        return proxy;
    }

    @SuppressWarnings("unchecked")
    protected List<WebElement> proxyForListLocator(ClassLoader loader, ElementLocator locator) {
        InvocationHandler handler = new LocatingElementListHandler(locator);

        List<WebElement> proxy;
        proxy = (List<WebElement>) Proxy.newProxyInstance(loader, new Class[]{List.class}, handler);
        return proxy;
    }

    protected abstract T createOne(Class<? extends T> elementClass, ClassLoader loader,
            ElementLocator locator);

    protected abstract List<T> createMany(Class<? extends T> elementClass, ClassLoader loader,
            ElementLocator locator);
}
