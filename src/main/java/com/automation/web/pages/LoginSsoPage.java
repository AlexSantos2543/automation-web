package com.automation.web.pages;

import com.automation.web.pageobjects.PageObject;
import com.automation.web.stereotype.LazySingleton;

@LazySingleton
public class LoginSsoPage extends PageObject<LoginSsoPageUi, LoginSsoPageValidator> {

    public LoginSsoPage enterEmail(String email) {
        ui.getEmail().clear().input(email);
        return this;
    }
}
