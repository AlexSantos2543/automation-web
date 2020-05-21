package com.automation.web.pages;

import com.automation.web.app.CompanyApp;
import com.automation.web.config.Wait;
import com.automation.web.pageobjects.PageObject;
import com.automation.web.pageobjects.Url;
import com.automation.web.stereotype.LazySingleton;
import org.springframework.beans.factory.annotation.Autowired;

@Url("login")
@LazySingleton
public class LoginSsoPage extends PageObject<LoginSsoPageUi, LoginSsoPageValidator> {

    @Autowired
    private Wait wait;

    @Autowired
    private CompanyApp bcdApp;


    public LoginSsoPage enterEmail(String email) {
        ui.getEmail().clear().input(email);
        return this;
    }
}
