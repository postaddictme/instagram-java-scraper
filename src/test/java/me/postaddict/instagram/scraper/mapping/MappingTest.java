package me.postaddict.instagram.scraper.mapping;

import me.postaddict.instagram.scraper.model.Account;
import org.apache.commons.io.IOUtils;
import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.eclipse.persistence.jaxb.dynamic.DynamicJAXBContextFactory;
import org.eclipse.persistence.oxm.MediaType;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MappingTest {
    @Test
    public void testAccountMapping() throws Exception {
        InputStream accountJson = MappingTest.class.getResourceAsStream("/getAccountByUsername_6e4a017e-e0ca-4512-9e96-48df0d486288.json");


        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, "me/postaddict/instagram/scraper/model/account-binding.json");
        properties.put("eclipselink.media-type", "application/json");
        JAXBContext jaxbContext = DynamicJAXBContextFactory.createContextFromOXM(Account.class.getClassLoader(), properties);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);
        //   /media/ date
        unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, true);
        Account account = (Account) unmarshaller.unmarshal(accountJson);
        assertThat(account.getUsername()).isNotNull();

    }
}
