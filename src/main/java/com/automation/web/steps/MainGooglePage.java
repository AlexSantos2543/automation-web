package com.automation.web.steps;

import com.automation.web.app.CompanyApp;
import com.automation.web.config.TestConfiguration;
import com.automation.web.config.Wait;
import com.automation.web.pages.LoginSsoPage;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = TestConfiguration.class)
@Log
public class MainGooglePage {

    @Autowired
    private CompanyApp app;

    @Autowired
    private Wait wait;
//
//    @Autowired
//    I18n i18n;

    @When("I open the app")
    public void iOpenTheApp() {
        app.goTo("https://www.google.com/");
    }

    @Then("I search by city name")
    public void i_should_see_ntent() {
        app.getPageObject(LoginSsoPage.class).enterEmail("eff");

    }

    @Then("I should see google home page contents")
    public void i_should_see_google_home_page_content() {
        LoginSsoPage loginSsoPage = app.getPageObject(LoginSsoPage.class);
        loginSsoPage.validator().validate();

    }
}
