package com.automation.web.pageobjects;

import java.lang.reflect.Field;

import com.automation.web.config.Wait;
import com.automation.web.exceptions.TimeoutException;
import com.automation.web.interaction.Alignment;
import com.automation.web.interaction.ElementInteraction;
import com.automation.web.interaction.Scroll;
import com.automation.web.pageobjects.factory.FieldInitializer;
import com.automation.web.stereotype.LazyPrototype;
import lombok.extern.java.Log;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Provides a facade to access UI elements in a uniform way across platforms.
 *
 * This bean is initialized after construction:
 *
 * - Element lookup context is set to the available WebDriver bean;
 *
 * - Fields of type {@link WebElement} are decorated;
 *
 * - Fields of type {@link Fragment} are decorated;
 *
 * - "waitToLoad" method is called.
 *
 * Inheriting classes annotated with {@link LazyPrototype} must be initialized manually.
 */
@Log
public abstract class UiFacade implements InitializingBean {

    protected static final String ELEMENT_NOT_FOUND_TEXT = "Element not found";

    @Autowired
    protected FieldInitializer fieldInitializer;

    /**
     * Selenium WebDriver and wrapped element
     */
    @NotDecorated
    protected WebElement element;

    @Autowired
    protected WebDriver driver;
    protected SearchContext context;

    public WebElement getElement() {
        return element;
    }

    @Autowired
    protected ElementInteraction interaction;
    @Autowired
    protected Wait wait;
    @Autowired
    protected Scroll scroll;

    @Override
    public void afterPropertiesSet() {
        //noinspection StatementWithEmptyBody
        if (this.getClass().isAnnotationPresent(LazyPrototype.class)) {
            // initialize manually with an appropriate context
        } else {
            this.init();
        }
    }

    /**
     * Wait for the slowest element of this element map is visible.
     * <p>
     * Consider overriding this method for each element map.
     */
    protected void waitToLoad() {
    }

    /**
     * Re-initialize elements using existing context
     */
    public void refresh() {
        init(context);
    }

    public void init(SearchContext context) {
        if (context == null) {
            throw new RuntimeException("Attempted to initialize fragment with null context!");
        }
        log.fine("Initializing: " + this.getClass());

        SearchContext explicitContext = findExplicitContext();
        if (explicitContext != null) {
            context = explicitContext; // IMPORTANT: search context change
        }

        if (context instanceof WebElement) {
            this.element = (WebElement) context;
        }
        this.context = context;

        fieldInitializer.initFields(context, this);
        this.waitToLoad(); //TODO: log an error if used with lazy prototype (slow page loading)
    }

    /**
     * Re-initialize elements using driver
     */
    public void init() {
        this.init(driver);
    }

    /**
     * If context is defined explicitly within this object, decorate that field. Explicit context is
     * located relative to driver in order to ensure that the same element will be found every time,
     * even after calling refresh() method!
     *
     * @return WebElement to use as context
     */
    private SearchContext findExplicitContext() {
        try {
            Field explicitContextField = this.getClass().getDeclaredField("explicitContext");
            explicitContextField.setAccessible(true);

            fieldInitializer.initField(driver, this, explicitContextField);

            return ((UiFacade) explicitContextField.get(this)).getElement();
        } catch (ReflectiveOperationException e) {
            log.fine("No explicit context of type UiFacade was found");
        }
        return null;
    }

    public String getTextIgnoreNotFound() {
        return getTextIgnoreNotFound(ELEMENT_NOT_FOUND_TEXT);
    }

    public String getTextIgnoreNotFound(String defaultText) {
        try {
            return getText();
        } catch (NoSuchElementException e) {
            return defaultText;
        }
    }

    public String getText() {
        return trimWhitespace(interaction.getText(element));
    }

    public String getValue() {
        return interaction.getValue(element);
    }

    public UiFacade click() {
        interaction.click(element, (JavascriptExecutor) driver);
        return this;
    }

    public UiFacade click(Alignment.Horizontal alignment) {
        interaction.click(element, driver, alignment);
        return this;
    }

    private String trimWhitespace(String text) {
        if (text != null) {
            return text.replaceAll("^\\s+", "").replaceAll("\\s+$", "");
        } else {
            return null;
        }
    }

    public UiFacade clear() {
        interaction.clear(element);
        return this;
    }

    public UiFacade input(String text) {
        interaction.input(element, text);
        return this;
    }

    // TODO: web only
    public boolean isActive() {
        return element.getAttribute("class").contains("active");
    }

    public boolean isEnabled() {
        return interaction.isEnabled(element);
    }

    public UiFacade waitUntilVisible() {
        wait.untilVisible(element);
        return this;
    }

    public UiFacade waitUntilVisible(long delaySeconds) {
        wait.untilVisible(element, delaySeconds);
        return this;
    }

    public UiFacade waitUntilHidden() {
        wait.untilHidden(element);
        return this;
    }

    public UiFacade waitUntilPresent() {
        wait.untilPresent(element);
        return this;
    }

    public UiFacade waitUntilAbsent() {
        wait.untilAbsent(element);
        return this;
    }

    public UiFacade waitUntilAbsent(long delaySeconds) {
        wait.untilAbsent(element, delaySeconds);
        return this;
    }

    public UiFacade waitUntilContainsText(String text) {
        wait.untilContainsText(element, text);
        return this;
    }

    public UiFacade waitUntilContainsText(String text, long delaySeconds) {
        wait.untilContainsText(element, text, delaySeconds);
        return this;
    }

    public UiFacade waitUntilContainsValue(String text) {
        wait.untilContainsValue(element, text);
        return this;
    }

    public UiFacade waitUntilContainsValue(String text, long delaySeconds) {
        wait.untilContainsValue(element, text, delaySeconds);
        return this;
    }

    public UiFacade scrollUntilPresent() {
        scroll.untilElementPresent(element);
        return this;
    }

    public UiFacade scrollUntilVisible() {
        scroll.untilElementVisible(element);
        return this;
    }

    public UiFacade scrollUntilAtTop() {
        scroll.untilElementAtTop(element);
        return this;
    }

    public UiFacade scrollUntilInView() {
        scroll.untilElementInView(element);
        return this;
    }

    public void withPageRefresh(Runnable action) {
        withPageRefresh(action, TimeoutException.class);
    }

    public void withPageRefresh(Runnable action, Class<? extends Exception> exceptionClass) {
        try {
            action.run();
        } catch (Exception e) {
            if (exceptionClass.isAssignableFrom(e.getClass())) {
                driver.navigate().refresh();
                // TODO: mark all elements as stale
                this.refresh();
                action.run();
            } else {
                throw e;
            }
        }
    }

    public boolean containsClass(String cssClass) {
        return element.getAttribute("class").contains(cssClass);
    }

    public boolean isVisible() {
        try {
            wait.untilVisible(element, Wait.DEFAULT_DELAY_SHORT);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public boolean isPresent() {
        try {
            wait.untilPresent(element, Wait.DEFAULT_DELAY_SHORT);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public String getAttribute(String attributeName) {
        return interaction.getAttribute(element, attributeName);
    }

    public UiFacade hover() {
        interaction.hover(element, driver);
        return this;
    }

    public boolean isChecked() {
        return interaction.isChecked(element);
    }

    public UiFacade waitUntilClickable() {
        wait.untilClickable(element);
        return this;
    }
}
