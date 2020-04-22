package com.automation.web.steps;

import com.automation.web.app.CompanyApp;
import com.automation.web.config.TestConfiguration;
import com.automation.web.config.Wait;
import com.automation.web.setup.I18n;
import cucumber.api.java.en.When;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = TestConfiguration.class)
@Log
public class MainGooglePage {

    @Autowired
    CompanyApp app;

    @Autowired
    private Wait wait;

    @Autowired
    I18n i18n;

    @When("I open the app")
    public void iOpenTheApp() {
        app.goTo("https://www.google.com/");
    }
}
