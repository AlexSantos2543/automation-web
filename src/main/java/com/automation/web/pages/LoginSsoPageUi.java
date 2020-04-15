package com.automation.web.pages;

import com.automation.web.pageobjects.UiFacade;
import com.automation.web.pageobjects.factory.Fragment;
import com.automation.web.stereotype.LazySingleton;
import lombok.Getter;
import org.openqa.selenium.support.FindBy;

@LazySingleton
@Getter
public class LoginSsoPageUi extends UiFacade {

    @FindBy(css = "[data-testid='login-in']")
    private Fragment logInLabel;

    @FindBy(css = "[data-testid='log-in-to-get-your-trip-details']")
    private Fragment loginToGetTripsLabel;

    @FindBy(css = "[data-testid='not-registered-yet']")
    private Fragment notRegisteredYetLabel;

    @FindBy(css = "[data-testid='create-account']")
    private Fragment createAccountBtn;

    @FindBy(css = "label[class*='InputEmail'] > span")
    private Fragment emailPlaceholderLabel;

    @FindBy(css = "[data-testid='sso-login-email']")
    private Fragment email;

    @FindBy(css = "[data-testid='login-next-btn']")
    private Fragment nextBtn;

    @FindBy(css = "a[mtt-text='auth.info.register-i-read-agree-to-terms-link']")
    private Fragment termsAndConditions;

    @FindBy(css = "a[mtt-text='bcdapp.menu.privacyPolicy']")
    private Fragment privacyPolicy;

    @FindBy(css = "a.mtt-faq-link > img")
    private Fragment needHelpIcon;

    @FindBy(css = "a.mtt-faq-link > span")
    private Fragment needHelpLabel;

    @FindBy(css = "language-selector[placement='bottom'] .translate-title.ng-binding")
    private Fragment languageLabel;

    @FindBy(css = "span.primary-title")
    private Fragment travelSimplyLabel;

    @FindBy(css = "span.secondary-title")
    private Fragment simplyTravel;

    @FindBy(css = "span.description")
    private Fragment description;

    @Override
    public void waitToLoad() {
        nextBtn.waitUntilVisible();
    }
}
