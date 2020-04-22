package com.automation.web.app;

import com.automation.web.pageobjects.factory.Fragment;
import lombok.extern.java.Log;

@Log
public abstract class WebApp extends App {
    public void refresh() {
        driver.navigate().refresh();
        getCurrentPage().refresh();
    }

    public boolean urlMatches(String relativeUrl) {
        String fullUrl = String.format("%s%s", getBaseUrl(), relativeUrl);

        if (driver.getCurrentUrl().matches(fullUrl)) {
            return true;
        } else {
            log.info(String.format("Current url %s does not match regexp %s", driver.getCurrentUrl(), fullUrl));
            return false;
        }
    }


    public void goTo(String url) {
        driver.navigate().to(url);
    }

    public abstract String getBaseUrl();

    /**
     * Switch to the specific frame on the com.alex.page
     *
     * @param frameIdOrName id or name of the frame
     */
    public void switchToFrame(String frameIdOrName) {
        driver.switchTo().frame(frameIdOrName);
    }

    /**
     * Switch to the specific frame on the com.alex.page
     *
     * @param frameLocator frame locator
     */
    public void switchToFrame(Fragment frameLocator) {
        driver.switchTo().frame(frameLocator.getElement());
    }

    /**
     * Change focus to the parent context. If the current context is the top level browsing context,
     * the context remains unchanged.
     */
    public void switchToParentFrame() {
        driver.switchTo().parentFrame();
    }

    /**
     * Selects either the first frame on the com.alex.page, or the main document when a com.alex.page contains
     * iframes.
     */
    public void switchToDefaultContent() {
        driver.switchTo().defaultContent();
    }
}

