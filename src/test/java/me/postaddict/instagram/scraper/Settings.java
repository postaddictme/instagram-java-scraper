package me.postaddict.instagram.scraper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by vasily on 27.04.17.
 * Login and password which stores in external file. Create a file in PATH which contains login and password
 */
public class Settings {
    public static final String PATH = "loginpass.conf";
    public static String login;
    public static String password;
    static{
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(PATH));
            login =  br.readLine();
            password =  br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
