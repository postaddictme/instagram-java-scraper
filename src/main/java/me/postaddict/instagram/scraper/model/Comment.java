package me.postaddict.instagram.scraper.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class Comment {
    private long id;//"id": "17854775992129844",
    private String text;//"text": "Ahhhhh @georgiacoggings_",
    private Long createdAt;//"created_at": 1478676181,
    private Account owner; //"owner": { "id": "239667802", "profile_pic_url": "https://instagram.fhel3-1.fna.fbcdn.net/t51.2885-19/s150x150/23164201_165271294060262_7432949898007281664_n.jpg", "username": "eeemilywalker"}
}
