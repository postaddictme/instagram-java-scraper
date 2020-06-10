package me.postaddict.instagram.scraper.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.postaddict.instagram.scraper.model.general.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

/**
 * Login and password which stores in external file. Create a file in PATH which contains login and password
 */
public final class Credentials {

    private static final String PATH = "credentials.json";
    private final String login;
    private final String phone;
    private final String password;
    private final String encPassword;

    public Credentials() throws IOException {
        InputStream is = null;
        try {
            is = getClass().getClassLoader().getResourceAsStream(PATH);
            if (is == null) {
                throw new IOException("can't find credentials file");
            }
            ObjectMapper mapper = new ObjectMapper();
            List<User> users = mapper.readValue(is, new TypeReference<List<User>>() {
            });
            Collections.shuffle(users);
            User user = users.get(0);

            this.login = user.getLogin();
            this.phone = user.getPhone();
            this.password = user.getPassword();
            this.encPassword = user.getEncPassword();
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
