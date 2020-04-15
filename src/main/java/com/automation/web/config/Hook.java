package com.automation.web.config;

import com.automation.web.app.App;
import cucumber.api.java.Before;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.ContextConfiguration;

@Log
@ContextConfiguration(classes = {TestConfiguration.class})
public final class Hook {

//    @Autowired
//    private App app;

//    @Before("@Setup:Login")
//    public void login() {
//        app.clearState();
//        app.loadPage(LoginSsoPage.class).enterEmail("email");
//    }

    @Before(value = "@User:bcd.automation.user+merge@gmail.com", order = HookOrder.SET_USER)
    public void setUserMerge() {
        setUser("app_user_merge");
    }

    private void setUser(String username) {
    }
}
