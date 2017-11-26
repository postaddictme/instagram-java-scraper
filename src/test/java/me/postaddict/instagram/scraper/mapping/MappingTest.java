package me.postaddict.instagram.scraper.mapping;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import me.postaddict.instagram.scraper.mapper.ModelMapper;
import me.postaddict.instagram.scraper.model.*;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class MappingTest {

    private final ModelMapper modelMapper = new ModelMapper();

    @Test
    public void testAccountByUsername() throws Exception {
        Account account = modelMapper.mapAccount(
                MappingTest.class.getResourceAsStream("/getAccountByUsername_6e4a017e-e0ca-4512-9e96-48df0d486288.json"));
        assertThat(account.getUsername()).isNotNull();
        assertThat(toJson(account)).isEqualTo(IOUtils.toString(
                MappingTest.class.getResourceAsStream("/expected/mapAccountByUsername.json"),"UTF-8"));
    }

    @Test
    public void testMediaByUrl() throws Exception {
        Media media = modelMapper.mapMedia(
                MappingTest.class.getResourceAsStream("/getMediaByUrl_ffd03200-d537-4719-8671-ac8414a796c0.json"));

        assertThat(media).isNotNull();
        assertThat(toJson(media)).isEqualTo(IOUtils.toString(
                MappingTest.class.getResourceAsStream("/expected/mapMediaByUrl.json"),"UTF-8"));
    }

    @Test
    public void testMediaByUrlTagged() throws Exception {
        Media media = modelMapper.mapMedia(
                MappingTest.class.getResourceAsStream("/mapMediaByUrl-tagged.json"));

        assertThat(media).isNotNull();
        assertThat(toJson(media)).isEqualTo(IOUtils.toString(
                MappingTest.class.getResourceAsStream("/expected/mapMediaByUrl-tagged.json"),"UTF-8"));
    }

    @Test
    public void testMediaByUrlCarousel() throws Exception {
        Media media = modelMapper.mapMedia(
                MappingTest.class.getResourceAsStream("/getMediaByUrl_ac2a5f1c-6e7a-4b66-bf9a-bdaac02e1f08.json"));

        assertThat(media).isNotNull();
        assertThat(toJson(media)).isEqualTo(IOUtils.toString(
                MappingTest.class.getResourceAsStream("/expected/mapMediaByUrlCarousel.json"),"UTF-8"));
    }

    @Test
    public void testMediaByUrlCarouselTagged() throws Exception {

        Media media = modelMapper.mapMedia(
                MappingTest.class.getResourceAsStream("/getMediaByUrl-Carusele-Tagged.json"));

        assertThat(media).isNotNull();
        assertThat(toJson(media)).isEqualTo(IOUtils.toString(
                MappingTest.class.getResourceAsStream("/expected/mapMediaByUrlCarousel-Tagged.json"),"UTF-8"));
    }

    @Test
    public void testComments() throws Exception {
        PageObject<Comment> comments = modelMapper.mapComments(
                MappingTest.class.getResourceAsStream("/getCommentsByMediaCode_9cc06fff-6531-4530-a76e-ff72738def57.json"));

        assertThat(comments).isNotNull();
        assertThat(toJson(comments)).isEqualTo(IOUtils.toString(
                MappingTest.class.getResourceAsStream("/expected/mapComments.json"),"UTF-8"));
    }

    @Test
    public void testLocation() throws Exception {
        InputStream locationStream = MappingTest.class.getResourceAsStream("/getLocationMediasById_90bf84cd-10ea-4d98-a224-bb3792255439.json");
        Location location = modelMapper.mapLocation(
                locationStream);

        assertThat(location).isNotNull();
        assertThat(toJson(location)).isEqualTo(IOUtils.toString(
                MappingTest.class.getResourceAsStream("/expected/mapLocation.json"),"UTF-8"));
    }

    @Test
    public void testMediaCarousel() throws Exception {
        InputStream mediaListStream = MappingTest.class.getResourceAsStream("/getMedias.json");
        Account medias = modelMapper.mapAccount(mediaListStream);

        assertThat(medias).isNotNull();
        assertThat(toJson(medias)).isEqualTo(IOUtils.toString(
                MappingTest.class.getResourceAsStream("/expected/mapMediaListCarousel.json"),"UTF-8"));
    }

    @Test
    public void testTag() throws Exception {
        InputStream tagStream = MappingTest.class.getResourceAsStream("/getTagByName_22ba043e-0545-4654-a609-5c2841ae16d9.json");
        Tag tag = modelMapper.mapTag(tagStream);

        assertThat(tag).isNotNull();
        assertThat(toJson(tag)).isEqualTo(IOUtils.toString(
                MappingTest.class.getResourceAsStream("/expected/mapTag.json"),"UTF-8"));
    }

    @Test
    public void testFollow() throws Exception {
        InputStream followStream = MappingTest.class.getResourceAsStream("/follows.json");
        PageObject<Account> follow = modelMapper.mapFollow(followStream);

        assertThat(follow).isNotNull();
        assertThat(toJson(follow)).isEqualTo(IOUtils.toString(
                MappingTest.class.getResourceAsStream("/expected/mapFollows.json"),"UTF-8"));
    }

    @Test
    public void testFollowers() throws Exception {
        InputStream followersStream = MappingTest.class.getResourceAsStream("/followers.json");
        PageObject<Account> followers = modelMapper.mapFollowers(followersStream);

        assertThat(followers).isNotNull();
        assertThat(toJson(followers)).isEqualTo(IOUtils.toString(
                MappingTest.class.getResourceAsStream("/expected/mapFollowers.json"),"UTF-8"));
    }

    @Test
    public void testAddComment() throws Exception {
        InputStream commentStream = MappingTest.class.getResourceAsStream("/addComment.json");
        ActionResponse<Comment> mediaCommentResponse = modelMapper.mapMediaCommentResponse(commentStream);

        assertThat(mediaCommentResponse).isNotNull();
        assertThat(mediaCommentResponse.getStatus()).isEqualTo("ok");
        assertThat(toJson(mediaCommentResponse)).isEqualTo(IOUtils.toString(
                MappingTest.class.getResourceAsStream("/expected/mapComment.json"),"UTF-8"));
    }


    @JsonFilter("skipLastUpdated")
    private class PropertyFilterMixIn {}

    private String toJson(Object instance) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.addMixIn(Object.class, PropertyFilterMixIn.class);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        SimpleBeanPropertyFilter theFilter = SimpleBeanPropertyFilter
                .serializeAllExcept("lastUpdated");
        FilterProvider filters = new SimpleFilterProvider()
                .addFilter("skipLastUpdated", theFilter);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        return objectMapper.writer(filters).writeValueAsString(instance);
    }
}
