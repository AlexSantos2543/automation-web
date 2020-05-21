package com.automation.web.pageobjects.decorator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.List;

import com.automation.web.pageobjects.UiFacade;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;
import org.springframework.beans.factory.BeanFactory;

public class FragmentFieldDecorator extends AnyFieldDecorator<UiFacade> {

    protected BeanFactory beanFactory;

    public FragmentFieldDecorator(ElementLocatorFactory locatorFactory, BeanFactory beanFactory) {
        super(locatorFactory);
        this.beanFactory = beanFactory;
    }

    @Override
    protected UiFacade createOne(
            Class<? extends UiFacade> elementClass, ClassLoader loader, ElementLocator locator) {
        UiFacade fragment = beanFactory.getBean(elementClass);
        fragment.init(proxyForLocator(loader, locator));
        return fragment;
    }

    @Override
    @SuppressWarnings("unchecked")
    protected List<UiFacade> createMany(
            Class<? extends UiFacade> elementClass, ClassLoader loader, ElementLocator locator) {
        InvocationHandler handler = new LocatingFragmentListHandler(beanFactory, elementClass, locator);

        List<UiFacade> proxy;
        proxy = (List<UiFacade>) Proxy.newProxyInstance(
                loader, new Class[]{List.class}, handler);
        return proxy;
    }
}
