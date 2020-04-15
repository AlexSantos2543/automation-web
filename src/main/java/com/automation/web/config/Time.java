package com.automation.web.config;

import lombok.extern.java.Log;

@Log
public class Time {
    public static final long HALF_SECOND = 500L;
    public static final long SECOND = 1000L;
    public static final long MINUTE = SECOND * 60L;
    public static final long HOUR = MINUTE * 60L;

    /**
     * Private constructor
     */
    private Time() {

    }

    /**
     * Thread sleep for a time
     * @param time length of time to sleep in milliseconds
     */
    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch(InterruptedException ie) {
            log.severe(ie.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}

