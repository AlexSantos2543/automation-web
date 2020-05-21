package com.automation.web.interaction;

import com.automation.web.browsers.Chrome;
import com.automation.web.browsers.Safari;
import com.automation.web.config.Wait;
import com.automation.web.exceptions.TimeoutException;
import com.automation.web.pageobjects.Fragment;
import lombok.extern.java.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Chrome
@Safari
@Log
@Lazy
@Service
public class WebScroll implements Scroll {

    protected static final int DEFAULT_SCROLL_COUNT = 10;
    protected static final double NUDGE_SIZE = 0.2d;

    protected final int nudgeHeight;
    protected final int nudgeWidth;

    protected Dimension screenSize;
    protected Wait wait;
    private WebDriver driver;

    public WebScroll(@Autowired WebDriver driver, @Autowired Wait wait) {
        this.driver = driver;
        this.wait = wait;
        this.screenSize = driver.manage().window().getSize();

        this.nudgeWidth = (int) (screenSize.width * NUDGE_SIZE);
        this.nudgeHeight = (int) (screenSize.height * NUDGE_SIZE);
    }

    /**
     * Scrolls the container, e.g. div (not the whole page), to the sub-element until it is visible
     * on the screen.
     *
     * @param container container view to scroll
     * @param element element to scroll to
     */
    public void toElementInContainer(Fragment container, Fragment element) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollTop=arguments[1].offsetTop", container.getElement(),
                element.getElement());
    }

    /**
     * Scrolls the container, e.g. div (not the whole page) vertically with specific number of pixels
     *
     * @param container container view to scroll
     * @param verticalPixels number of pixels to scroll vertically
     */
    public void scrollPixelsVertically(Fragment container, int verticalPixels) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollTop=arguments[1]", container.getElement(),
                verticalPixels);
    }

    @Override
    public Scroll align(Alignment.Horizontal horizontalAlignment) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Scroll align(Alignment.Vertical verticalAlignment) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public <T> void until(ExpectedCondition<T> condition) {
        until(condition, DEFAULT_SCROLL_COUNT);
    }

    @Override
    public <T> void until(ExpectedCondition<T> condition, int count) {
        while (count-- > 0) {
            try {
                wait.until(condition, 5);
                return;
            } catch (TimeoutException ignored) {
                log.fine("Element not found, scrolling");
                pageDown();
            }
        }

        wait.until(condition, 5);
    }

    @Override
    public void untilElementVisible(By by) {
        untilElementVisible(by, DEFAULT_SCROLL_COUNT);
    }

    @Override
    public void untilElementVisible(By by, int count) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void untilElementVisible(WebElement element) {
        untilElementVisible(element, DEFAULT_SCROLL_COUNT);
    }

    @Override
    public void untilElementVisible(WebElement element, int count) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void untilElementPresent(By by) {
        untilElementPresent(by, DEFAULT_SCROLL_COUNT);
    }

    @Override
    public void untilElementPresent(By by, int count) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void untilElementPresent(WebElement element) {
        untilElementPresent(element, DEFAULT_SCROLL_COUNT);
    }

    @Override
    public void untilElementPresent(WebElement element, int count) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void untilElementInView(By by) {
        untilElementInView(by, DEFAULT_SCROLL_COUNT);
    }

    @Override
    public void untilElementInView(By by, int count) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void untilElementInView(WebElement element) {
        untilElementInView(element, DEFAULT_SCROLL_COUNT);
    }

    @Override
    public void untilElementInView(WebElement element, int ignored) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    @Override
    public void untilElementAtTop(WebElement element) {
        untilElementAtTop(element, DEFAULT_SCROLL_COUNT);
    }

    @Override
    public void untilElementAtTop(WebElement element, int count) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void untilElementAtTop(WebElement element, int count, ScrollOffsets offsets) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public void nudgeDown() {
        nudgeDown(nudgeHeight);
    }

    @Override
    public void nudgeDown(double nudgeSize) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                String.format("window.scrollBy(0, %d)", (int) (screenSize.height * nudgeSize)));
    }

    @Override
    public void nudgeUp() {
        nudgeUp(nudgeHeight);
    }

    @Override
    public void nudgeUp(double nudgeSize) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                String.format("window.scrollBy(0, -%d)", (int) (screenSize.height * nudgeSize)));
    }

    @Override
    public void pageDown(int count) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        while (count-- > 0) {
            js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        }
    }

    @Override
    public void pageUp(int count) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        while (count-- > 0) {
            js.executeScript("window.scrollTo(0, -document.body.scrollHeight)");
        }
    }

    @Override
    public void pageLeft(int count) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        while (count-- > 0) {
            js.executeScript("window.scrollTo(-document.body.scrollWidth, 0)");
        }
    }

    @Override
    public void pageRight(int count) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        while (count-- > 0) {
            js.executeScript("window.scrollTo(document.body.scrollWidth, 0)");
        }
    }
}
