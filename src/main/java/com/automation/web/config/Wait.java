package com.automation.web.config;

import com.automation.web.exceptions.TimeoutException;
import lombok.extern.java.Log;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

@Lazy
@Service
@Log
public class Wait implements InitializingBean {
    public static final long DEFAULT_DELAY_SHORT = 5L;
    public static final long DEFAULT_DELAY_LONG = 60L;

    protected long explicitWait = DEFAULT_DELAY_LONG;
    protected long implicitWait = DEFAULT_DELAY_SHORT;

    protected WebDriver driver;

    public Wait(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void afterPropertiesSet() {
        this.setUp();
    }

    private void setUp() {
        log.info(String.format("Configuring driver with implicit timeout: " +
                "%s seconds, explicit timeout: %s seconds", implicitWait, explicitWait));
        driver.manage().timeouts().implicitlyWait(implicitWait, TimeUnit.SECONDS);

        try {
            driver.manage().timeouts().pageLoadTimeout(explicitWait, TimeUnit.SECONDS);
        } catch (WebDriverException e) {
            log.warning("PageLoadTimeout is not implemented in this driver");
            log.warning(e.getMessage());
        }
    }

    public void configure(long explicitWait, long implicitWait) {
        this.implicitWait = implicitWait;
        this.explicitWait = explicitWait;
        this.setUp();
    }

    public <T> void until(ExpectedCondition<T> condition, long delaySeconds) {
        try {
//            new WebDriverWait(driver, delaySeconds).until(condition);
        } catch (RuntimeException e) {
            throw new TimeoutException(e);
        }
    }

    /**
     * Wait until element is visible
     * @param element to check visibility of
     */
    public void untilVisible(WebElement element) {
        untilVisible(element, explicitWait);
    }

    /**
     * Wait until element is visible
     * @param element to check visibility of
     * @param delaySeconds max time to wait
     */
    public void untilVisible(WebElement element, long delaySeconds) {
        until(ExpectedConditions.visibilityOf(element), delaySeconds);
    }

    /**
     * Wait until element is not visible
     * @param element to check visibility of
     */
    public void untilHidden(WebElement element) {
        untilHidden(element, explicitWait);
    }

    /**
     * Wait until element is not visible
     * @param element to check visibility of
     * @param delaySeconds max time to wait
     */
    public void untilHidden(WebElement element, long delaySeconds) {
        until(ExpectedConditions.invisibilityOf(element), delaySeconds);
    }

    /**
     * Wait until element is present
     * @param element to check presence of
     */
    public void untilPresent(WebElement element) {
        untilPresent(element, explicitWait);
    }

    /**
     * Wait until element is present
     * @param element to check presence of
     * @param delaySeconds max time to wait
     */
    public void untilPresent(WebElement element, long delaySeconds) {
        until(presenceOf(element), delaySeconds);
    }

    /**
     * Wait until element is not present
     * @param element to check presence of
     */
    public void untilAbsent(WebElement element) {
        untilAbsent(element, explicitWait);
    }

    /**
     * Wait until element is not present
     * @param element to check presence of
     * @param delaySeconds max time to wait
     */
    public void untilAbsent(WebElement element, long delaySeconds) {
        until(absenceOf(element), delaySeconds);
    }

    /**
     * Wait until element contains the provided text
     * @param element to wait for
     * @param text to check for
     */
    public void untilContainsText(WebElement element, String text) {
        untilContainsText(element, text, explicitWait);
    }

    /**
     * Wait until element contains the provided text
     * @param element to wait for
     * @param text to check for
     * @param delaySeconds max time to wait
     */
    public void untilContainsText(WebElement element, String text, long delaySeconds) {
        until(ExpectedConditions.textToBePresentInElement(element, text), delaySeconds);
    }

    /**
     * Wait until element contains the provided value
     * @param element to wait for
     * @param text to check for
     */
    public void untilContainsValue(WebElement element, String text) {
        untilContainsValue(element, text, explicitWait);
    }

    /**
     * Wait until element contains the provided value
     * @param element to wait for
     * @param text to check for
     * @param delaySeconds max time to wait
     */
    public void untilContainsValue(WebElement element, String text, long delaySeconds) {
        until(ExpectedConditions.textToBePresentInElementValue(element, text), delaySeconds);
    }

    /**
     * Wait until element is clickable
     * @param element to wait for
     * @param delaySeconds max time to wait
     */
    public void untilClickable(WebElement element, long delaySeconds) {
        until(ExpectedConditions.elementToBeClickable(element), delaySeconds);
    }

    /**
     * Wait until element is clickable
     * @param element to wait for
     */
    public void untilClickable(WebElement element) {
        untilClickable(element, explicitWait);
    }

    /**
     * Check if element is visible within a specified time
     * @param element to check visibility of
     * @param delaySeconds time delay to wait for visibility of element
     * @return true if element is visible. False otherwise.
     */
    public boolean isElementVisible(WebElement element, long delaySeconds) {
        try {
            until(ExpectedConditions.visibilityOf(element), delaySeconds);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Check immediately if element is visible
     * @param element to check visibility of
     * @return true if element is visible. False otherwise.
     */
    public boolean isElementVisible(WebElement element) {
        return isElementVisible(element, 0L);
    }

    /**
     * Create a boolean expected condition function to check the presence of an element
     * @param element check for the presence of element
     * @return expected condition that evaluates to true if element is displayed
     */
    public static ExpectedCondition<Boolean> presenceOf(final WebElement element) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return element.isDisplayed();
            }

            @Override
            public String toString() {
                return "presence of: " + element;
            }
        };
    }

    /**
     * Create a boolean expected condition function to check the absence of an element
     * @param element check for the absence of element
     * @return expected condition that evaluates to true if element is not displayed
     */
    public static ExpectedCondition<Boolean> absenceOf(final WebElement element) {
        return driver -> {
            //noinspection TryWithIdenticalCatches
            try {
                return !element.isDisplayed();
            } catch (NoSuchElementException e) {
                // Returns true because the element is not present in DOM. The
                // try block checks if the element is present but is invisible.
                return true;
            } catch (StaleElementReferenceException e) {
                // Returns true because stale element reference implies that element
                // is no longer visible.
                return true;
            }
        };
    }

    public void seconds(long delay) {
        Time.sleep(Time.SECOND * delay);
    }
}
