package me.postaddict.instagram.scraper.mapping;

import me.postaddict.instagram.scraper.model.*;
import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.eclipse.persistence.jaxb.dynamic.DynamicJAXBContextFactory;
import org.eclipse.persistence.oxm.MediaType;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MappingTest {
    @Test
    public void testAccountByUsername() throws Exception {
        InputStream accountJson = MappingTest.class.getResourceAsStream("/getAccountByUsername_6e4a017e-e0ca-4512-9e96-48df0d486288.json");
        String mappingFile = "me/postaddict/instagram/scraper/model/account-binding.json";
        Unmarshaller unmarshaller = getUnmarshaller(mappingFile);
        Account account = (Account) unmarshaller.unmarshal(accountJson);
        //   /media/date
        assertThat(account.getUsername()).isNotNull();
    }
    @Test
    public void testMediaByUrl() throws Exception {
        InputStream accountJson = MappingTest.class.getResourceAsStream("/getMediaByUrl_ffd03200-d537-4719-8671-ac8414a796c0.json");
        String mappingFile = "me/postaddict/instagram/scraper/model/media-by-url.json";
        Unmarshaller unmarshaller = getUnmarshaller(mappingFile);
        GraphQlResponse<Media> graphQlResponse = (GraphQlResponse<Media>) unmarshaller.unmarshal(accountJson);
        //copy firstComments,commentCount
        assertThat(graphQlResponse.getPayload()).isNotNull();
    }

    @Test
    public void testMediaByUrlCarousel() throws Exception {
        InputStream accountJson = MappingTest.class.getResourceAsStream("/getMediaByUrl_ac2a5f1c-6e7a-4b66-bf9a-bdaac02e1f08.json");
        String mappingFile = "me/postaddict/instagram/scraper/model/media-by-url.json";
        Unmarshaller unmarshaller = getUnmarshaller(mappingFile);
        GraphQlResponse<Media> graphQlResponse = (GraphQlResponse<Media>) unmarshaller.unmarshal(accountJson);
        //parentMedia
        assertThat(graphQlResponse.getPayload()).isNotNull();
    }

    @Test
    public void testComments() throws Exception {
        InputStream accountJson = MappingTest.class.getResourceAsStream("/getCommentsByMediaCode_9cc06fff-6531-4530-a76e-ff72738def57.json");
        String mappingFile = "me/postaddict/instagram/scraper/model/comments.json";
        Unmarshaller unmarshaller = getUnmarshaller(mappingFile);
        GraphQlResponse<PageObject<Comment>> graphQlResponse = (GraphQlResponse<PageObject<Comment>>) unmarshaller.unmarshal(accountJson);
        assertThat(graphQlResponse.getPayload()).isNotNull();
    }

    @Test
    public void testLocation() throws Exception {
        InputStream accountJson = MappingTest.class.getResourceAsStream("/getLocationMediasById_90bf84cd-10ea-4d98-a224-bb3792255439.json");
        String mappingFile = "me/postaddict/instagram/scraper/model/location.json";
        Unmarshaller unmarshaller = getUnmarshaller(mappingFile);
        Location location = (Location) unmarshaller.unmarshal(accountJson);
        assertThat(location).isNotNull();
    }

    private Unmarshaller getUnmarshaller(String mappingFile) throws JAXBException {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, mappingFile);
        properties.put(JAXBContextProperties.MEDIA_TYPE, "application/json");
        JAXBContext jaxbContext = DynamicJAXBContextFactory.createContextFromOXM(Account.class.getClassLoader(), properties);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);
        unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, true);
        return unmarshaller;
    }
}
