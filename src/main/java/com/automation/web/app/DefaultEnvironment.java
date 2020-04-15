package com.automation.web.app;

import lombok.extern.java.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Log
public abstract class DefaultEnvironment<S> implements Environment<S> {
    protected final Properties endpoints;
    protected final Map<String, Credentials> credentials;

    protected DefaultEnvironment(String endpointsFile, String credentialsFile) {
        this.credentials = new HashMap<>();
        try {
            log.info("Reading " + endpointsFile);
            endpoints = loadProperties(endpointsFile);

            log.info("Reading " + credentialsFile);
            Properties credentialsProperties = loadProperties(credentialsFile);
            for (Map.Entry user : credentialsProperties.entrySet()) {
                credentials.put(
                        user.getKey().toString(),
                        Credentials.fromEncodedString(user.getValue().toString()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Properties loadProperties(String filename) throws IOException {
        Properties properties = new Properties();

        try (InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(
                "env/" + filename)) {
            if (is == null) {
                log.fine("No environment properties file found");
            } else {
                properties.load(is);
            }
        }
        return properties;
    }

    @Override
    public String getServiceUri(S service) {
        if (endpoints.containsKey(service.toString())) {
            return endpoints.getProperty(service.toString());
        } else {
            throw new IllegalArgumentException("No endpoint for service: " + service.toString());
        }
    }

    @Override
    public Credentials getCredentials(String user) {
        if (credentials.containsKey(user)) {
            return credentials.get(user);
        }
        throw new RuntimeException("No credentials found for " + user);
    }

    @Override
    public Credentials findCredentials(String login) {
        for (Map.Entry<String, Credentials> user : credentials.entrySet()) {
            if (user.getValue().getLogin().equals(login)) {
                return user.getValue();
            }
        }
        throw new IllegalArgumentException("Could not find user: " + login);
    }
}

