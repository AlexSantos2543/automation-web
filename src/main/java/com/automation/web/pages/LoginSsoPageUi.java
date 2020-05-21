package com.automation.web.pages;

import com.automation.web.pageobjects.Fragment;
import com.automation.web.pageobjects.UiFacade;
import com.automation.web.stereotype.LazySingleton;
import lombok.Getter;
import org.openqa.selenium.support.FindBy;

@LazySingleton
@Getter
public class LoginSsoPageUi extends UiFacade {

    @FindBy(name = "q")
    private Fragment email;

    @Override
    public void waitToLoad() {
        email.waitUntilVisible();
    }
}
