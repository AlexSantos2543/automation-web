package com.automation.web.config;

import com.automation.web.browsers.Chrome;
import com.automation.web.browsers.Safari;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Configuration
public class WebDriverConfiguration {

    /**
     * Selenium http interface URL. If null, local web driver will be used.
     */
    private final URL remoteUrl;
    /**
     * True is remote ip points to a Selenium hub instance, false otherwise. Set
     * '-Ddriver.isHub=false' for testing on the local machine or with local Appium/Selenium
     * instances.
     * <p>
     * Default: true
     */
    private final boolean isHub;

//    @Autowired
//    private I18n i18n;

    @Autowired
    private Environment environment;

    public WebDriverConfiguration() throws MalformedURLException {
        isHub = Boolean.valueOf(System.getProperty("driver.isHub", "true"));

        String remoteIp = System.getProperty("driver.ip", null);
        String remotePort = System.getProperty("driver.port", "4723");
        remoteUrl = remoteIp == null ? null
                : new URL(String.format("http://%s:%s/wd/hub", remoteIp, remotePort));

    }

    private ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        options.setCapability(CapabilityType.BROWSER_NAME, "chrome");
//        options.addArguments("language=" + i18n.getLocale().getLanguage());
//        options.addArguments("--lang=" + i18n.getLocale().getLanguage());
        options.setCapability(CapabilityType.PLATFORM_NAME, Platform.MAC);

        Map<String, Object> prefs = new HashMap<>();
//        prefs.put("intl.accept_languages", i18n.getLocale().getLanguage());
        options.setExperimentalOption("prefs", prefs);

        // Remove the banner which says 'Chrome is being controlled by automated test software'
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        // Opens at minimum 500px width
        String[] activeProfiles = environment.getActiveProfiles();
        for (String profile : activeProfiles) {
            if (profile.equalsIgnoreCase("mobilescreen")) {
                Map<String, String> mobileEmulation = new HashMap<>();
                mobileEmulation.put("deviceName", "Nexus 5");
                options.setExperimentalOption("mobileEmulation", mobileEmulation);
            }
        }
        return options;
    }

    @Chrome
    @Bean(destroyMethod = "quit")
    public WebDriver chromeDriver() {
        ChromeOptions options = getChromeOptions();

        if (!isHub) {
            System.setProperty("webdriver.chrome.driver", "./chromedriver");
            return new ChromeDriver(options);
        }
        return getRemoteWebDriver(options);
    }

    private RemoteWebDriver getRemoteWebDriver(MutableCapabilities browserOptions) {

        if (isHub || remoteUrl != null) {
            return new RemoteWebDriver(remoteUrl, browserOptions);
        }
        throw new WebDriverException();
    }

    @Safari
    @Bean(destroyMethod = "quit")
    public WebDriver safariDriver() {
        SafariOptions options = new SafariOptions();

        if (!isHub) {
            return new SafariDriver(options);
        }
        return getRemoteWebDriver(options);
    }

//    @Lazy
//    @Bean
//    public I18n i18n() {
//        return new DefaultI18n(new JsonStorageFactory(
//                "src/main/resources/i18n/translations-%s.json"));
//    }

    @Lazy
    @Bean
    public ThreadLocalRandom random() {
        return ThreadLocalRandom.current();
    }

}
