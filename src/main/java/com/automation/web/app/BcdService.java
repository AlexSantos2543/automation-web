package com.automation.web.app;

public enum BcdService {
    WEB_APP("web_app");


    private String property;

    private BcdService(String property) {
        this.property = property;
    }

    public String toString() {
        return this.property;
    }
}

