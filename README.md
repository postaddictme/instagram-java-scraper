Instagram Java scraper
======================

Instagram Java Scraper. Get account information, photos and videos without any authorization.

 
### Get account by username
```java
Instagram instagram = new Instagram();
Account account = instagram.getAccountByUsername("kevin");
System.out.println(account.mediaCount);
```

### Get account by account id
```java
Instagram instagram = new Instagram();
Account account = instagram.getAccountById(3);
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
### Convert media id to shortcode
```java
Media.getCodeFromId("1270593720437182847_3");
// OR
Media.getCodeFromId("1270593720437182847");
// Output: BGiDkHAgBF_
// So you can do like this: instagram.com/p/BGiDkHAgBF_
```

### Convert shortcode to media id
```java
Media.getIdFromCode('BGiDkHAgBF_');
// Output: 1270593720437182847
```

### Other
PHP library: https://github.com/raiym/instagram-php-scraper
