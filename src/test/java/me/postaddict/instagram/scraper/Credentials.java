package me.postaddict.instagram.scraper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by vasily on 27.04.17.
 * Login and password which stores in external file. Create a file in PATH which contains login and password
 */
final class Credentials {

    private final static String PATH = "credentials.properties";
    private String login;
    private String password;

    Credentials() throws IOException {
        InputStream is = null;
        Properties properties = new Properties();
        try {
            is = getClass().getClassLoader().getResourceAsStream(PATH);
            if (is == null) {
                throw new IOException("can't find credentials file");
            }
            properties.load(is);
            login = properties.getProperty("login");
            password = properties.getProperty("password");
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    String getLogin() {
        return login;
    }

    String getPassword() {
        return password;
    }

}
