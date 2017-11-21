package me.postaddict.instagram.scraper.mapping;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import me.postaddict.instagram.scraper.ModelMapper;
import me.postaddict.instagram.scraper.model.*;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class MappingTest {

    @Test
    public void testAccountByUsername() throws Exception {
        Account account = ModelMapper.mapAccount(
                MappingTest.class.getResourceAsStream("/getAccountByUsername_6e4a017e-e0ca-4512-9e96-48df0d486288.json"));
        assertThat(account.getUsername()).isNotNull();
        assertThat(IOUtils.toString(
                MappingTest.class.getResourceAsStream("/expected/mapAccountByUsername.json"),"UTF-8")).isEqualTo(toJson(account));
    }

    @Test
    public void testMediaByUrl() throws Exception {
        GraphQlResponse<Media> graphQlResponse = ModelMapper.mapMedia(
                MappingTest.class.getResourceAsStream("/getMediaByUrl_ffd03200-d537-4719-8671-ac8414a796c0.json"));

        assertThat(graphQlResponse.getPayload()).isNotNull();
        assertThat(IOUtils.toString(
                MappingTest.class.getResourceAsStream("/expected/mapMediaByUrl.json"),"UTF-8")).isEqualTo(toJson(graphQlResponse));
    }

    @Test
    public void testMediaByUrlCarousel() throws Exception {
        GraphQlResponse<Media> graphQlResponse = ModelMapper.mapMedia(
                MappingTest.class.getResourceAsStream("/getMediaByUrl_ac2a5f1c-6e7a-4b66-bf9a-bdaac02e1f08.json"));

        assertThat(graphQlResponse.getPayload()).isNotNull();
        assertThat(IOUtils.toString(
                MappingTest.class.getResourceAsStream("/expected/mapMediaByUrlCarousel.json"),"UTF-8")).isEqualTo(toJson(graphQlResponse));
    }

    @Test
    public void testComments() throws Exception {
        GraphQlResponse<PageObject<Comment>> graphQlResponse = ModelMapper.mapComments(
                MappingTest.class.getResourceAsStream("/getCommentsByMediaCode_9cc06fff-6531-4530-a76e-ff72738def57.json"));

        assertThat(graphQlResponse.getPayload()).isNotNull();
        assertThat(IOUtils.toString(
                MappingTest.class.getResourceAsStream("/expected/mapComments.json"),"UTF-8")).isEqualTo(toJson(graphQlResponse));
    }

    @Test
    public void testLocation() throws Exception {
        InputStream locationStream = MappingTest.class.getResourceAsStream("/getLocationMediasById_90bf84cd-10ea-4d98-a224-bb3792255439.json");
        Location location = ModelMapper.mapLocation(
                locationStream);

        assertThat(location).isNotNull();
        assertThat(IOUtils.toString(
                MappingTest.class.getResourceAsStream("/expected/mapLocation.json"),"UTF-8")).isEqualTo(toJson(location));
    }

    @Test
    public void testMediaList() throws Exception {
        InputStream mediaListStream = MappingTest.class.getResourceAsStream("/getMedias_081fffaf-f255-4cf5-85e7-5eee0f0f8902.json");
        Account medias = ModelMapper.mapMediaList(mediaListStream);

        assertThat(medias).isNotNull();
        assertThat(IOUtils.toString(
                MappingTest.class.getResourceAsStream("/expected/mapMediaList.json"),"UTF-8")).isEqualTo(toJson(medias));

    }

    @Test
    public void testMediaList1() throws Exception {
        InputStream mediaListStream = MappingTest.class.getResourceAsStream("/getMedias.json");
        Account medias = ModelMapper.mapMediaList(mediaListStream);

        assertThat(medias).isNotNull();
        String expected = toJson(medias);
        assertThat(IOUtils.toString(
                MappingTest.class.getResourceAsStream("/expected/mapMediaListCarousel.json"),"UTF-8")).isEqualTo(expected);

    }

    @Test
    public void testTag() throws Exception {
        InputStream tagStream = MappingTest.class.getResourceAsStream("/getTagByName_22ba043e-0545-4654-a609-5c2841ae16d9.json");
        Tag tag = ModelMapper.mapTag(tagStream);

        assertThat(tag).isNotNull();
        assertThat(IOUtils.toString(
                MappingTest.class.getResourceAsStream("/expected/mapTag.json"),"UTF-8")).isEqualTo(toJson(tag));
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
