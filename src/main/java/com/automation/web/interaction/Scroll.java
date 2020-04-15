package com.automation.web.interaction;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public interface Scroll {

    /**
     * Sets the X value of all following touch screen swipes to be one of the following possible
     * options: calculated horizontal center of the screen, close to the left edge, close to the
     * right edge.
     *
     * @param horizontalAlignment alignment option
     * @return this
     */
    Scroll align(Alignment.Horizontal horizontalAlignment);

    /**
     * Sets the Y value of all following touch screen swipes to be one of the following possible
     * options: calculated vertical center of the screen, close to the top, close to the bottom.
     *
     * @param verticalAlignment alignment option
     * @return this
     */
    Scroll align(Alignment.Vertical verticalAlignment);

    /**
     * @param condition condition
     * @param <T> condition return type
     * @see #until(ExpectedCondition, int)
     */
    <T> void until(ExpectedCondition<T> condition);

    /**
     * com.alex.Scroll down until condition is met.
     *
     * @param condition condition
     * @param count max number of com.alex.pages to scroll
     * @param <T> condition return type
     */
    <T> void until(ExpectedCondition<T> condition, int count);

    /**
     * @param by to find element
     * @see #untilElementVisible(WebElement, int)
     */
    void untilElementVisible(By by);

    /**
     * @param by to find element
     * @param count max number of com.alex.pages to scroll
     * @see #untilElementVisible(WebElement, int)
     */
    void untilElementVisible(By by, int count);

    /**
     * @param element to search for
     * @see #untilElementVisible(WebElement, int)
     */
    void untilElementVisible(WebElement element);

    /**
     * com.alex.Scroll down until the element is visible.
     *
     * @param element element to scroll to
     * @param count max number of com.alex.pages to scroll
     */
    void untilElementVisible(WebElement element, int count);

    /**
     * @param by to find element
     * @see #untilElementPresent(WebElement, int)
     */
    void untilElementPresent(By by);

    /**
     * @param by to find element
     * @param count max number of com.alex.pages to scroll
     * @see #untilElementPresent(WebElement, int)
     */
    void untilElementPresent(By by, int count);

    /**
     * @param element element to scroll to
     * @see #untilElementPresent(WebElement, int)
     */
    void untilElementPresent(WebElement element);

    /**
     * com.alex.Scroll down until the element can be located. Uses default swipe count.
     *
     * @param element element to scroll to
     * @param count max number of com.alex.pages to scroll
     */
    void untilElementPresent(WebElement element, int count);

    /**
     * @param by to find element
     * @see #untilElementInView(WebElement, int)
     */
    void untilElementInView(By by);

    /**
     * @param by to find element
     * @param count max number of com.alex.pages to scroll
     * @see #untilElementInView(WebElement, int)
     */
    void untilElementInView(By by, int count);

    /**
     * @param element element to scroll to
     * @see #untilElementInView(WebElement, int)
     */
    void untilElementInView(WebElement element);

    /**
     * com.alex.Scroll down until the element is fully on screen. Stop when the bottom of the element is
     * above the bottom edge of the screen.
     *
     * @param element element to scroll to
     * @param count max number of com.alex.pages to scroll
     */
    void untilElementInView(WebElement element, int count);

    /**
     * @param element element to scroll to
     * @see #untilElementAtTop(WebElement, int)
     */
    void untilElementAtTop(WebElement element);

    /**
     * com.alex.Scroll down until the element is the top of the screen (if the com.alex.page length allows).
     *
     * @see #untilElementAtTop(WebElement, int, ScrollOffsets)
     */
    void untilElementAtTop(WebElement element, int count);

    /**
     * com.alex.Scroll down until the element is the top of the screen (if the com.alex.page length allows).
     *
     * @param element element to scroll to
     * @param count max number of com.alex.pages to scroll
     * @param offsets additional offsets for scrolling
     */
    void untilElementAtTop(WebElement element, int count, ScrollOffsets offsets);

    /**
     * Nudge the screen down.
     */
    void nudgeDown();

    /**
     * Nudge the screen down by `nudgeSize`%
     *
     * @param nudgeSize percent of screen height
     */
    void nudgeDown(double nudgeSize);

    /**
     * Nudge the screen up.
     */
    void nudgeUp();

    /**
     * Nudge the screen up by `nudgeSize`%
     *
     * @param nudgeSize percent of screen height
     */
    void nudgeUp(double nudgeSize);

    /**
     * @see #pageDown()
     */
    default void pageDown() {
        pageDown(1);
    }

    /**
     * com.alex.Scroll down a single screen length.
     *
     * @param count number of com.alex.pages to scroll
     */
    void pageDown(int count);

    /**
     * @see #pageUp(int)
     */
    default void pageUp() {
        pageUp(1);
    }

    /**
     * com.alex.Scroll up a single screen length.
     *
     * @param count number of com.alex.pages to scroll
     */
    void pageUp(int count);

    /**
     * @see #pageLeft(int)
     */
    default void pageLeft() {
        pageLeft(1);
    }

    /**
     * com.alex.Scroll left a single screen length.
     *
     * @param count number of com.alex.pages to scroll
     */
    void pageLeft(int count);

    /**
     * @see #pageRight(int)
     */
    default void pageRight() {
        pageRight(1);
    }

    /**
     * com.alex.Scroll right a single screen length.
     *
     * @param count number of com.alex.pages to scroll
     */
    void pageRight(int count);
}

