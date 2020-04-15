package com.automation.web.pageobjects;

import lombok.extern.java.Log;
import org.hamcrest.CoreMatchers;
import org.springframework.stereotype.Service;

@Log
@Service
public class ValidateElement extends Validate {
    private static final String ELEMENT_NOT_FOUND_TEXT = "Element was not found";

    public void equals(UiFacade fragment, String expected) {
        equals(fragment.getTextIgnoreNotFound(ELEMENT_NOT_FOUND_TEXT), expected);
    }

    public void equals(boolean ignoreCase, UiFacade fragment, String expected) {
        equals(ignoreCase, fragment.getTextIgnoreNotFound(ELEMENT_NOT_FOUND_TEXT), expected);
    }

    public void equalsAny(UiFacade fragment, String... expected) {
        equalsAny(fragment.getTextIgnoreNotFound(ELEMENT_NOT_FOUND_TEXT), expected);
    }

    public void equalsAny(boolean ignoreCase, UiFacade fragment, String... expected) {
        equalsAny(ignoreCase, fragment.getTextIgnoreNotFound(ELEMENT_NOT_FOUND_TEXT), expected);
    }

    public void matches(UiFacade fragment, String regex) {
        matches(fragment.getTextIgnoreNotFound(ELEMENT_NOT_FOUND_TEXT), regex);
    }

    public void matchesAny(UiFacade fragment, String... regex) {
        matchesAny(fragment.getTextIgnoreNotFound(ELEMENT_NOT_FOUND_TEXT), regex);
    }

    public void containsClass(UiFacade element, String elementDescription, String cssClass) {
        if (CoreMatchers.is(element.containsClass(cssClass)).matches(true)) {
            log.info(String.format("validate pass: element %s contains class: [%s]",
                    elementDescription, cssClass));
        } else {
            log.warning(String.format("VALIDATE FAIL: element %s does not contain class: [%s]",
                    elementDescription, cssClass));
        }
    }

    public void contains(UiFacade fragment, String text) {
        contains(fragment.getTextIgnoreNotFound(ELEMENT_NOT_FOUND_TEXT), text);
    }
}
