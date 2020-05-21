package com.automation.web.pageobjects.decorator;

import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.ElementLocatorFactory;

public class ElementFieldDecorator extends AnyFieldDecorator<WebElement> {

    public ElementFieldDecorator(ElementLocatorFactory locatorFactory) {
        super(locatorFactory);
    }

    @Override
    protected WebElement createOne(Class<? extends WebElement> elementClass, ClassLoader loader,
            ElementLocator locator) {
        return elementClass.cast(proxyForLocator(loader, locator));
    }

    @Override
    protected List<WebElement> createMany(Class<? extends WebElement> elementClass,
            ClassLoader loader, ElementLocator locator) {
        return proxyForListLocator(loader, locator);
    }
}
