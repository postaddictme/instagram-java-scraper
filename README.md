Instagram Java scraper
======================

Instagram Java Scraper. Get account information, photos and videos without any authorization.

 
### Get account by username
```java
Instagram instagram = new Instagram(httpClient);
Account account = instagram.getAccountByUsername("kevin");
System.out.println(account.getMedia().getCount());
```

### Get account by account id
```java
Instagram instagram = new Instagram(httpClient);
Account account = instagram.getAccountById(3);
System.out.println(account.getFullName());
```

### Get account medias
```java
PageObject<Media> medias = instagram.getMedias("durov", 1);
System.out.println(medias.getNodes().get(0).getMediaResource().getDisplayUrl());
```

### Get media by code
```java
Media media = instagram.getMediaByUrl("BGY0zB4r7X2");
System.out.println(media.getOwner().getUsername());
```

### Get media by url
```java
Media media = instagram.getMediaByUrl("https://www.instagram.com/p/BGY0zB4r7X2/");
System.out.println(media.getOwner().getUsername());
```
### Convert media id to shortcode
```java
MediaUtil.getCodeFromId("1270593720437182847_3");
// OR
MediaUtil.getCodeFromId("1270593720437182847");
// Output: BGiDkHAgBF_
// So you can do like this: instagram.com/p/BGiDkHAgBF_
```

### Convert shortcode to media id
```java
MediaUtil.getIdFromCode('BGiDkHAgBF_');
// Output: 1270593720437182847
```

### How to use release version of Instagram Java scraper ###

TODO documentation/release

[mvnrepository](https://mvnrepository.com/artifact/me.postaddict/instagramscraper)

[jcenter](https://bintray.com/bintray/jcenter/me.postaddict%3Ainstagramscraper)

### How to use development version of Instagram Java scraper ###

Read more info on [jitpack page of project](https://jitpack.io/#com.github.postaddictme/instagram-java-scraper).
Open "Commit" tab and select revision by commit hash. Just open Gradle or Maven tab copy artifact info and place it with dendency management repository in your project build configuration

### Other
PHP library: https://github.com/postaddictme/instagram-php-scraper
