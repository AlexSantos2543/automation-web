package com.automation.web.app;

import com.automation.web.browsers.Dev;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Dev
@Lazy
@Service
public final class DevEnvironment extends DefaultEnvironment<BcdService> {
    private static final Logger log = Logger.getLogger(DevEnvironment.class.getName());

    public DevEnvironment() {
        super("dev.properties", "dev_credentials.properties");
    }
}