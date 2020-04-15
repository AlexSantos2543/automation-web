package com.automation.web.pageobjects;

import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Log
@Service
public class Validate {

    public boolean isTrue(Boolean actual, String description) {
        if (actual) {
            log.info(String.format("validate pass: [%s] is true", description));
            return true;
        } else {
            log.warning(String.format("VALIDATE FAIL: [%s] is false, but expected to be true",
                    description));
            return false;
        }
    }

    public boolean isFalse(Boolean actual, String description) {
        if (!actual) {
            log.info(String.format("validate pass: [%s] is false", description));
            return true;
        } else {
            log.warning(String.format("VALIDATE FAIL: [%s] is true, but expected to be false",
                    description));
            return false;
        }
    }

    public boolean equals(String actual, String expected) {
        return equals(false, actual, expected);
    }

    public boolean equals(boolean ignoreCase, String actual, String expected) {
        boolean isEqual = ignoreCase ? actual.equalsIgnoreCase(expected) : actual.equals(expected);
        if (isEqual) {
            log.info(String.format("validate pass: [%s] equals [%s]", actual, expected));
            return true;
        } else {
            log.warning(String.format("VALIDATE FAIL: [%s] does not equal [%s]", actual, expected));
            return false;
        }
    }

    public boolean equalsAny(String actual, String... expected) {
        return equalsAny(false, actual, expected);
    }

    public boolean equalsAny(boolean ignoreCase, String actual, String... expected) {
        final boolean anyMatch = Arrays.stream(expected).anyMatch(
                ignoreCase ? actual::equalsIgnoreCase : actual::equals);

        if (anyMatch) {
            log.info(String.format("validate pass: [%s] equals one or more of [%s]", actual,
                    String.join(", ", expected)));
            return true;
        } else {

            log.warning(String.format("VALIDATE FAIL: [%s] does not equal any of [%s]", actual,
                    String.join(", ", expected)));
            return false;
        }
    }

    public boolean matches(String actual, String regex) {
        if (actual.matches(regex)) {
            log.info(String.format("validate pass: [%s] matches [%s]", actual, regex));
            return true;
        } else {
            log.warning(String.format("VALIDATE FAIL: [%s] does not match [%s]", actual, regex));
            return false;
        }
    }

    public boolean matchesAny(String actual, String... regexes) {
        final boolean anyMatch = Arrays.stream(regexes).anyMatch(actual::matches);

        if (anyMatch) {
            log.info(String.format("validate pass: [%s] matches [%s]",
                    actual, Arrays.toString(regexes)));
            return true;
        } else {
            log.warning(String.format("VALIDATE FAIL: [%s] does not match [%s]",
                    actual, Arrays.toString(regexes)));
            return false;
        }
    }

    public boolean contains(String actual, String content) {
        if (actual.contains(content)) {
            log.info(String.format("validate pass: [%s] contains [%s]", actual, content));
            return true;
        } else {
            log.warning(
                    String.format("VALIDATE FAIL: [%s] does not contain [%s]", actual, content));
            return false;
        }
    }
}

