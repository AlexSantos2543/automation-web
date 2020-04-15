package com.automation.web.steps;

import com.automation.web.app.CompanyApp;
import com.automation.web.config.TestConfiguration;
import cucumber.api.java.en.When;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = TestConfiguration.class)
@Log
public class LoginSteps {

    @Autowired
    CompanyApp app;

//    @Autowired
//    private Wait wait;

    @When("I open the app")
    public void iOpenTheApp() {
        app.goTo("https://www.google.com/");

    }
}
