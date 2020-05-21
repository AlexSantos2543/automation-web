package com.automation.web.pageobjects;


import com.automation.web.stereotype.LazyPrototype;

/**
 * Defines the smallest section of the user interface, usually a single element,
 * such as button or text label.
 *
 * Use instead of {@link org.openqa.selenium.WebElement} to access a uniform cross-platform
 * interface for element interactions (.click(), .getText(), etc).
 */
@LazyPrototype
public class Fragment extends UiFacade {
}
