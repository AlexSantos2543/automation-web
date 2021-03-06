package com.automation.web.exceptions;

import com.automation.web.pageobjects.PageObject;

public class FailedToLoadPageException extends RuntimeException {

    public FailedToLoadPageException(Class<? extends PageObject> pageClass, Throwable cause) {
        super(String.format("Failed to load page: %s", pageClass.getSimpleName()), cause);
    }
}
