package com.automation.web.pages;


import com.automation.web.pageobjects.Validator;
import com.automation.web.stereotype.LazySingleton;

@LazySingleton
public class LoginSsoPageValidator extends Validator<LoginSsoPageUi> {

    public LoginSsoPageValidator validate() {
//        validate.equals(ui.getLogInLabel(), i18n.t("auth.button.login"));
//        validate.equals(ui.getLoginToGetTripsLabel(), i18n.t("auth.info.loginText"));
//        validate.equals(ui.getNotRegisteredYetLabel(), i18n.t("auth.info.notRegistered"));
//        validate.equals(ui.getCreateAccountBtn(), i18n.t("auth.info.login.no-account"));
//        validate.equals(ui.getEmailPlaceholderLabel(), i18n.t("auth.label.email-address"));
//        validate.equals(ui.getNextBtn(), i18n.t("newFeature.button.next"));
//        validate.equals(ui.getTermsAndConditions(), i18n.t("auth.info.register-i-read-agree-to-terms-link"));
//        validate.equals(ui.getPrivacyPolicy(), i18n.t("bcd.info.privacy"));
//        validate.isTrue(ui.getNeedHelpIcon().isVisible(), "Need help icon is not visible.");
//        validate.equals(ui.getNeedHelpLabel(), i18n.t("auth.link.faq.need-help"));
//        validate.equals(ui.getLanguageLabel(), i18n.t("translate.language"));
//        validate.equals(ui.getTravelSimplyLabel(), i18n.t("auth.info.login.title.primary"));
//        validate.equals(ui.getSimplyTravel(), i18n.t("auth.info.login.title.secondary"));
//        validate.equals(ui.getDescription(), i18n.t("auth.info.login.title.text"));
        return this;
    }
}
