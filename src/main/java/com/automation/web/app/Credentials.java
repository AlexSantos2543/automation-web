package com.automation.web.app;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class Credentials {
    private String username;
    private String login;
    private String password;

    public Credentials(String login, String password) {
        this.username = login;
        this.login = login;
        this.password = password;
    }

    /**
     * Parse credentials from string
     * @param credentialsString separated by '/'. username/password
     * @return credentials
     */
    public static Credentials fromEncodedString(String credentialsString) {
        String[] parts = credentialsString.split("/");
        if (parts.length < 2) {
            throw new IllegalArgumentException(
                    "Incorrect format in credentials file! Expected <login>/<password>");
        }

        String username = parts[0];
        String login = parts[0];

        Base64.Decoder decoder = Base64.getDecoder();
        String password = new String(decoder.decode(parts[1]));

        return new Credentials(username, login, password);
    }

    /**
     * Parse credentials from a map
     * @param map of username/login/email and password
     * @return credentials
     */
    public static Credentials fromMap(Map<String, String> map) {
        String login;
        if (map.containsKey("username")) {
            login = map.get("username");
        } else if (map.containsKey("login")) {
            login = map.get("login");
        } else if (map.containsKey("email")) {
            login = map.get("email");
        } else {
            throw new IllegalArgumentException(
                    "Could not parse user name, login or email from table: " + mapAsString(map));
        }

        if (! map.containsKey("password")) {
            throw new IllegalArgumentException(
                    "Could not parse password from table: " + mapAsString(map));
        }
        return new Credentials(login, map.get("password"));
    }

    /**
     * Get string representation of map
     * @param map to stringify
     * @return string representation of map
     */
    private static String mapAsString(Map<String, String> map) {
        return map.keySet().stream()
                .map(key -> key + "=" + map.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
    }
}

