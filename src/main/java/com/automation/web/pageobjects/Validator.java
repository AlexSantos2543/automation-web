package com.automation.web.pageobjects;

import com.automation.web.setup.I18n;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class Validator<U extends UiFacade> {

    /**
     * @deprecated use "ui" field instead of "map"
     */
    @Deprecated
    protected U map;

    protected U ui;

    @Autowired
    protected I18n i18n;

    @Autowired
    protected ValidateElement validate;
}
