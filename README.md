Instagram Java scraper
======================

Instagram Java Scraper. Get account information, photos and videos without any authorization.

 
### Get account by username
```java
Instagram instagram = new Instagram();
Account account = instagram.getAccount("kevin");
System.out.println(account.mediaCount);
```

### Get account medias
```java
List<Media> medias = instagram.getMedias("durov", 12);
System.out.println(medias.get(0).imageHighResolutionUrl);
```

### Get media by code
```java
Media media = instagram.getMediaByUrl("BGY0zB4r7X2");
System.out.println(media.owner.username);
```

### Get media by url
```java
Media media = instagram.getMediaByUrl("https://www.instagram.com/p/BGY0zB4r7X2/");
System.out.println(media.owner.username);
```

### Other
PHP library: https://github.com/raiym/instagram-java-scraper