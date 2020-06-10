package me.postaddict.instagram.scraper.model.general;

import lombok.Data;

import javax.persistence.Entity;

@Entity
@Data
public class User {
    private String login;
    private String phone;
    private String password;
    private String encPassword;
}
