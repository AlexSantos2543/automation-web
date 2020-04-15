package com.automation.web.interaction;

import com.automation.web.config.Wait;
import org.openqa.selenium.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Nullable;

public abstract class ElementInteraction {

    @Autowired
    private Wait wait;

    /**
     * Get text of an element
     *
     * @param element to get text of
     * @return the text of the element
     */
    public String getText(WebElement element) {
        return element.getText();
    }

    /**
     * Get value of an element
     *
     * @param element to get value of
     * @return the value of the element
     */
    public String getValue(WebElement element) {
        return element.getAttribute("value");
    }

    /**
     * Get an attribute from an element
     *
     * @param element to get attribute from
     * @param attributeName attribute of element
     * @return the value of the attribute
     */
    public String getAttribute(WebElement element, String attributeName) {
        return element.getAttribute(attributeName);
    }

    /**
     * Check if element is enabled
     *
     * @param element to check if it is enabled
     * @return true if the element is enabled
     */
    public boolean isEnabled(WebElement element) {
        return element.isEnabled();
    }

    /**
     * Click element
     *
     * @param element to click
     */
    public void click(WebElement element) {
        click(element, null);
    }

    /**
     * Click element
     *
     * @param element to click
     * @param js Javascript executor
     */
    public void click(WebElement element, @Nullable JavascriptExecutor js) {
        try {
            element.click();
        } catch (NoSuchElementException e) {
            wait.untilVisible(element);
            element.click();
        }
    }

    /**
     * Click element, click position alignment can be specified
     *
     * @param element to click
     * @param driver driver
     * @param alignment horizontal alignment
     */
    public void click(WebElement element, WebDriver driver,
                      Alignment.Horizontal alignment) {
        Rectangle rect = null;
        try {
            rect = element.getRect();
        } catch (NoSuchElementException e) {
            wait.untilVisible(element);
            rect = element.getRect();
        }

        int x = selectX(rect, alignment);
        click(driver, x, rect.y + (rect.height / 2));
    }

    /**
     * Selects a value for the X coordinate based on the current horizontal alignment.
     *
     * @return integer X coordinate
     */
    protected int selectX(Rectangle rect, Alignment.Horizontal alignment) {
        final int offset = 40;
        switch (alignment) {
            case CENTER_HORIZONTAL:
                return rect.x + (rect.width / 2);
            case LEFT:
                return rect.x + offset;
            case RIGHT:
                return rect.x + rect.width - offset;
            default:
                throw new IllegalArgumentException(
                        "Expected horizontal alignment, got: " + alignment);
        }
    }

    /**
     * Click by coordinates
     *
     * @param driver WebDriver
     * @param x x-coordinate on screen
     * @param y y-coordinate on screen
     */
    public void click(WebDriver driver, int x, int y) {
        throw new UnsupportedOperationException("Click by coords is not implemented yet.");
    }

    /**
     * Clear text from element
     *
     * @param element to clear
     */
    public void clear(WebElement element) {
        element.clear();
    }

    /**
     * Send text to element
     *
     * @param element to input to
     * @param text to input
     */
    public void input(WebElement element, String text) {
        element.sendKeys(text);
    }

    public abstract void hover(WebElement element, WebDriver driver);

    /**
     * Check if a radio or select button is checked
     *
     * @param element to check if it is checked
     * @return true if the element has attribute value 'checked'
     */
    public abstract boolean isChecked(WebElement element);
}

