package com.automation.web.pageobjects.decorator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import com.automation.web.pageobjects.UiFacade;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.springframework.beans.factory.BeanFactory;

public class LocatingFragmentListHandler implements InvocationHandler {

    private final BeanFactory beanFactory;

    private final ElementLocator locator;
    private final Class<? extends UiFacade> elementClass;

    private List<UiFacade> fragments;

    public LocatingFragmentListHandler(BeanFactory beanFactory,
            Class<? extends UiFacade> elementClass, ElementLocator locator) {
        this.beanFactory = beanFactory;
        this.locator = locator;
        this.elementClass = elementClass;
    }

    @Override
    public Object invoke(Object object, Method method, Object[] objects) throws Throwable {
        if (fragments == null) {
            fragments = new LinkedList<>();
            List<WebElement> contexts = locator.findElements();

            for (WebElement parent : contexts) {
                UiFacade fragment = beanFactory.getBean(elementClass);
                fragment.init(parent);
                fragments.add(fragment);
            }
        }

        try {
            return method.invoke(fragments, objects);
        } catch (InvocationTargetException e) {
            // Unwrap the underlying exception
            throw e.getCause();
        }
    }
}
