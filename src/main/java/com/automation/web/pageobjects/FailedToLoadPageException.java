package com.automation.web.pageobjects;

public class FailedToLoadPageException extends RuntimeException {

    public FailedToLoadPageException(Class<? extends PageObject> pageClass, Throwable cause) {
        super(String.format("Failed to load page: %s", pageClass.getSimpleName()), cause);
    }
}
