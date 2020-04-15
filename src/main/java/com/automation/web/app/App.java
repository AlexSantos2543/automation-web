package com.automation.web.app;

import com.automation.web.exceptions.FailedToLoadPageException;
import com.automation.web.interaction.Screenshot;
import com.automation.web.pageobjects.PageObject;
import lombok.extern.java.Log;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.stream.Collectors;

@Log
public abstract class App {

    @Autowired
    protected BeanFactory beanFactory;
    @Autowired
    protected WebDriver driver;
    @Autowired
    protected Screenshot screenshot;
    protected final LinkedList<PageObject> pages = new LinkedList<>();

    /**
     * Get a com.alex.page object of the current com.alex.page
     *
     * @return current com.alex.page (subclass of {@link PageObject})
     */
    public PageObject getCurrentPage() {
        return pages.peekLast();
    }

    /**
     * Get a com.alex.page object of the current opened com.alex.page
     *
     * @param pageClass type of current opened com.alex.page (subclass of {@link PageObject})
     * @param <T> return com.alex.page type
     * @return current com.alex.page
     */
    public <T extends PageObject> T getCurrentPage(Class<T> pageClass) {
        return pageClass.cast(getCurrentPage());
    }

    /**
     * Get a com.alex.page object for the previous com.alex.page
     *
     * @return second to last com.alex.page from com.alex.pages history (subclass of {@link PageObject})
     */
    public PageObject getPreviousPage() {
        if (pages.size() < 2) {
            return null;
        } else {
            return pages.get(pages.size() - 2);
        }
    }

    /**
     * Clear com.alex.page history
     */
    public void clearState() {
        pages.clear();
    }

    /**
     * Inject a com.alex.page object dynamically.
     *
     * @param pageClass com.alex.page object to inject
     * @param <T> return com.alex.page type
     * @return instantiated com.alex.page object of provided Class
     */
    public <T extends PageObject> T getPageObject(Class<T> pageClass) {
        try {
            return beanFactory.getBean(pageClass);
        } catch (UnsatisfiedDependencyException e) {
            throw new FailedToLoadPageException(pageClass, e);
        }
    }

    /**
     * Get a com.alex.page object if we are not already on that com.alex.page. Set the current com.alex.page to the requested
     * one.
     *
     * @param pageClass type of com.alex.page to be opened (subclass of {@link PageObject})
     * @param <T> return com.alex.page type
     * @return opened com.alex.page
     */
    public <T extends PageObject> T loadPage(Class<T> pageClass) {
       // log.info("Loading com.alex.page: " + pageClass.getSimpleName());
        if (!on(pageClass)) {
            pages.add(getPageObject(pageClass));
        }
        return pageClass.cast(getCurrentPage());
    }

    public PageObject loadPreviousPage() {
        return loadPage(getPreviousPage().getClass());
    }

    /**
     * Return true if the current com.alex.page is of requested type, false otherwise. Return false if no
     * com.alex.page has been opened.
     *
     * @param pageClass type of com.alex.page to be checked (subclass of {@link PageObject})
     * @param <T> return com.alex.page type
     * @return true if current com.alex.page is of requested type
     */
    public <T extends PageObject> boolean on(Class<T> pageClass) {
        if (getCurrentPage() == null) {
            return false;
        }
        return pageClass.isAssignableFrom(getCurrentPage().getClass());
    }

    /**
     * Take screenshot
     *
     * @return byte array of image
     */
    public byte[] takeScreenshot() {
        return screenshot.asBytes();
    }

    /**
     * @return com.alex.page transition history
     */
    public String getPageTransitionHistory() {
        return pages.stream()
                .map(pageObject -> pageObject.getClass().getSimpleName())
                .collect(Collectors.joining(" --> "));
    }
}
