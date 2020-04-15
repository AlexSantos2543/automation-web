package com.automation.web.app;

import com.automation.web.pages.LoginSsoPage;
import lombok.Getter;
import lombok.extern.java.Log;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.Map;
import java.util.Set;

@Lazy
@Service
@Log
public class CompanyApp extends WebApp implements InitializingBean {

    @Autowired
    protected Environment<BcdService> env;

    @Override
    public void afterPropertiesSet() {
        openApp();
    }

    @Override
    public String getBaseUrl() {
        return env.getServiceUri(BcdService.WEB_APP);
    }

    public void openApp() {
        String screenSize = getExpectedBrowserSize();
        switch (screenSize) {
            case "desktop":
                setBrowserSize(1280, 993);
                break;
            case "tablet":
                setBrowserSize(990, 768);
                break;
            case "mobile":
                setBrowserSize(479, 800);
                break;
            default:
                throw new InvalidParameterException("Invalid screen size: '" + screenSize + "' was "
                        + "used, accepted values are: 'desktop', 'tablet' and 'mobile'.");
        }
    }


    public String getExpectedBrowserSize() {
        String profile = System.getProperty("spring.profiles.active");
        if (profile.contains("desktopscreen")) {
            return "desktop";
        } else if (profile.contains("tabletscreen")) {
            return "tablet";
        } else if (profile.contains("mobilescreen")) {
            return "mobile";
        } else {
            // If size is undefined the desktop one will be used
            return "desktop";
        }
    }

    public String getBrowserName() {
        String profile = System.getProperty("spring.profiles.active");
        if (profile.contains("chrome")) {
            return "chrome";
        } else if (profile.contains("safari")) {
            return "safari";
        } else if (profile.contains("ie")) {
            return "ie";
        } else if (profile.contains("firefox")) {
            return "firefox";
        } else {
            throw new RuntimeException(
                    "Unexpected browser value in spring.profiles.active variable: '" + profile
                            + "'.");
        }
    }

    private void setBrowserSize(int width, int height) {
        driver.manage().window().setSize(new Dimension(width, height));
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public void navigateBack() {
        driver.navigate().back();
    }

    public <T> void injectItemToLocalStorage(T key, T value) {
        ((JavascriptExecutor) (driver)).executeScript(String.format(
                "window.localStorage.setItem('%s','%s');", key, value));
    }

    public <T> void injectItemsToLocalStorage(Map<T, T> items) {
        for (Map.Entry<T, T> item : items.entrySet()) {
            injectItemToLocalStorage(item.getKey(), item.getValue());
        }
        log.info("Injected items to local storage: " + items);
    }

    public void reloadPage() {
        driver.navigate().refresh();
    }

    public WebDriver getDriver() {
        return driver;
    }

    public void switchFrame(String frameIdOrName) {
        driver.switchTo().frame(frameIdOrName);
    }

    public void switchToParentFrame() {
        driver.switchTo().parentFrame();
    }

    public String getCurrentWindowHandle() {
        return driver.getWindowHandle();
    }

    public Set<String> getWindowsHandles() {
        return driver.getWindowHandles();
    }

    public void switchToWindow(String handle) {
        if(getWindowsHandles().stream().anyMatch(s -> s.equals(handle))) {
            driver.switchTo().window(handle);
        } else {
            throw new InvalidArgumentException("Can't switch to window handle [" + handle + "] as it does not exist.");
        }
    }

    public void switchToOtherWindow() {
        String currentHandle = getCurrentWindowHandle();
        String handleToSwitch = getNonCurrentWindowHandle(currentHandle);
        switchToWindow(handleToSwitch);
    }

    private String getNonCurrentWindowHandle(String currentHandle) {
        return getWindowsHandles().stream()
                .filter(s -> !s.equals(currentHandle))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Couldn't find any other handle than the current window handle."));
    }
}

