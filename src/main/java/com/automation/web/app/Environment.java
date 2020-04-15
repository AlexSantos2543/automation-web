package com.automation.web.app;

public interface Environment<S> {
    String getServiceUri(S service);
    Credentials getCredentials(String prefix);
    Credentials findCredentials(String username);

    default Credentials getCredentials(S service) {
        return getCredentials(service.toString());
    }
}