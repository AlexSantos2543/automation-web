package com.automation.web.pageobjects;

import java.util.LinkedList;
import java.util.List;
import org.openqa.selenium.SearchContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: init lists of page objects with FindBy instead of this class
@Service
public class PageObjectInitializer {
    @Autowired
    private BeanFactory beanFactory;

    public <T extends PageObject> T getPageWithContext(Class<T> pageClass, SearchContext context) {
        T page = beanFactory.getBean(pageClass);

        page.init(context);
        return page;
    }

    public <T extends PageObject> List<T> getPagesWithContexts(Class<T> pageClass, List<Fragment> contexts) {
        boolean includeHidden = false; // TODO: remove and replace with better element selection

        List<T> pages = new LinkedList<>();
        for (Fragment context : contexts) {
            if (includeHidden || context.getElement().isDisplayed()) {
                pages.add(getPageWithContext(pageClass, context.getElement()));
            }
        }
        return pages;
    }
}
