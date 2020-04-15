package com.automation.web.config;

/**
 * Default Cucumber hook order is 10000
 */
public class HookOrder {


    /**
     * Fetch user credentials before other hooks.
     */
    public static final int SET_USER = 100;

    public static final int PRECONDITION = 110;

    public static final int RESET_PASSWORD = 200;

    public static final int MOCK = 1_000;

    private HookOrder() {
    }
}
