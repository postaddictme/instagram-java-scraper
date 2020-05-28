package me.postaddict.instagram.scraper.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Login and password which stores in external file. Create a file in PATH which contains login and password
 */
public final class Credentials {

    private static final String PATH = "credentials.properties";
    private String login;
    private String phone;
    private String password;
    private String encPassword;

    public Credentials() throws IOException {
        InputStream is = null;
        Properties properties = new Properties();
        try {
            is = getClass().getClassLoader().getResourceAsStream(PATH);
            if (is == null) {
                throw new IOException("can't find credentials file");
            }
            properties.load(is);
            this.login = properties.getProperty("login");
            this.phone = properties.getProperty("phone");
            this.password = properties.getProperty("password");
            this.encPassword = properties.getProperty("enc_password");
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    public String getLogin() {
        return this.login;
    }

    public String getPhone() {
        return this.phone;
    }

    public String getPassword() {
        return this.password;
    }

    public String getEncPassword() {
        return this.encPassword;
    }
}
