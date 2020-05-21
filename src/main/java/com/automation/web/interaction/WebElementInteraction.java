package com.automation.web.interaction;

import javax.annotation.Nullable;

import com.automation.web.browsers.Chrome;
import com.automation.web.browsers.Safari;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Chrome
@Safari
@Lazy
@Service
public class WebElementInteraction extends ElementInteraction {

    /**
     * Get text of an element
     *
     * @param element to get text of
     * @return text of the element
     */
    @Override
    public String getText(WebElement element) {
        if (element.isDisplayed()) {
            return element.getText();
        } else {
            return element.getAttribute("textContent");
        }
    }

    /**
     * Click element using JavaScript if element is not displayed
     *
     * @param js Javascript executor, e.g. Selenium driver
     * @param element to click
     */
    @Override
    public void click(WebElement element, @Nullable JavascriptExecutor js) {
        if (element.isDisplayed()) {
            element.click();
        } else if (js != null) {
            js.executeScript("arguments[0].click();", element);
        } else {
            throw new NoSuchElementException("Failed to click element!");
        }
    }

    @Override
    public boolean isChecked(WebElement element) {
        return Boolean.parseBoolean(element.getAttribute("checked"));
    }

    public void hover(WebElement element, WebDriver driver){
        Actions action = new Actions(driver);
        action.moveToElement(element)
                    .build().perform();
    }
}
