package me.postaddict.instagram.scraper;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import me.postaddict.instagram.scraper.model.*;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.eclipse.persistence.jaxb.dynamic.DynamicJAXBContextFactory;
import org.eclipse.persistence.oxm.MediaType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ModelMapper {

    public static Account mapAccount(InputStream jsonStream){
        //   /media/date
        return mapObject(jsonStream, "me/postaddict/instagram/scraper/model/account-binding.json");
    }

    public static GraphQlResponse<Media> mapMedia(InputStream jsonStream){
        GraphQlResponse<Media> graphQlResponse = mapObject(jsonStream, "me/postaddict/instagram/scraper/model/media-by-url.json");
        Media media = graphQlResponse.getPayload();
        media.setCommentCount(media.getCommentPreview().getCount());
        return graphQlResponse;
    }

    public static GraphQlResponse<PageObject<Comment>> mapComments(InputStream jsonStream){
        return mapObject(jsonStream, "me/postaddict/instagram/scraper/model/comments.json");
    }

    public static Location mapLocation(InputStream jsonStream){
        Location location = mapObject(jsonStream, "me/postaddict/instagram/scraper/model/location.json");
        location.setCount(location.getMediaRating().getMedia().getCount());
        return location;
    }

    @SneakyThrows
    public static Account mapMediaList(InputStream jsonStream){
        Account account = mapObject(jsonStream, "me/postaddict/instagram/scraper/model/medias.json");
        Account accountCopy = (Account) BeanUtils.cloneBean(account);
        accountCopy.setMedia(null);
        account.getMedia().getNodes().forEach(media-> media.setOwner(accountCopy));
        return account;
    }

    public static Tag mapTag(InputStream jsonStream){
        Tag tag = mapObject(jsonStream, "me/postaddict/instagram/scraper/model/tag.json");
        if(tag!=null && tag.getMediaRating()!=null && tag.getMediaRating().getMedia()!=null) {
            tag.setCount(tag.getMediaRating().getMedia().getCount());
        }
        return tag;
    }


    @SuppressWarnings("unchecked")
    private static<T> T mapObject(InputStream jsonStream, String mappingFile){
        try {
            Unmarshaller unmarshaller = getUnmarshaller(mappingFile);
            return (T) unmarshaller.unmarshal(jsonStream);
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static Unmarshaller getUnmarshaller(String mappingFile) throws JAXBException {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, mappingFile);
        properties.put(JAXBContextProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader()==null?
                Account.class.getClassLoader():Thread.currentThread().getContextClassLoader();

        JAXBContext jaxbContext = DynamicJAXBContextFactory.createContextFromOXM(classLoader, properties);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);
        unmarshaller.setProperty(UnmarshallerProperties.JSON_INCLUDE_ROOT, true);
        return unmarshaller;
    }

}
