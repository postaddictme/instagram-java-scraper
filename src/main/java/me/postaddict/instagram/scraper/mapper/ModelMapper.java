package me.postaddict.instagram.scraper.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.igorsuhorukov.dom.transform.DomTransformer;
import com.github.igorsuhorukov.dom.transform.converter.NopTypeConverter;
import lombok.SneakyThrows;
import me.postaddict.instagram.scraper.model.*;
import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.persistence.jaxb.JAXBContextProperties;
import org.eclipse.persistence.jaxb.UnmarshallerProperties;
import org.eclipse.persistence.jaxb.dynamic.DynamicJAXBContextFactory;
import org.eclipse.persistence.oxm.MediaType;
import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.xpath.XPathFactory;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ModelMapper implements Mapper{

    private final ThreadLocal<ObjectMapper> mapperThreadLocal = ThreadLocal.withInitial(ObjectMapper::new);
    private final TypeReference<Map<String, Object>> jsonMapTypeReference = new TypeReference<Map<String, Object>>() {
    };
    private final ConcurrentHashMap<String, ThreadLocal<Unmarshaller>> unmarshallerCache = new ConcurrentHashMap<>();

    public Account mapAccount(InputStream jsonStream){
        //   /media/date
        return mapObject(jsonStream, "me/postaddict/instagram/scraper/model/account-binding.json");
    }

    public GraphQlResponse<Media> mapMedia(InputStream jsonStream){
        GraphQlResponse<Media> graphQlResponse = mapObject(jsonStream,
                "me/postaddict/instagram/scraper/model/media-by-url.json");
        Media media = graphQlResponse.getPayload();
        media.setCommentCount(media.getCommentPreview().getCount());
        return graphQlResponse;
    }

    public GraphQlResponse<PageObject<Comment>> mapComments(InputStream jsonStream){
        return mapObject(jsonStream, "me/postaddict/instagram/scraper/model/comments.json");
    }

    public Location mapLocation(InputStream jsonStream){
        Location location = mapObject(jsonStream, "me/postaddict/instagram/scraper/model/location.json");
        location.setCount(location.getMediaRating().getMedia().getCount());
        return location;
    }

    @SneakyThrows
    public Account mapMediaList(InputStream jsonStream){
        Account account = mapObject(jsonStream, "me/postaddict/instagram/scraper/model/medias.json");
        Account accountCopy = (Account) BeanUtils.cloneBean(account);
        accountCopy.setMedia(null);
        account.getMedia().getNodes().forEach(media-> media.setOwner(accountCopy));
        return account;
    }

    public Tag mapTag(InputStream jsonStream){
        Tag tag = mapObject(jsonStream, "me/postaddict/instagram/scraper/model/tag.json");
        if(tag!=null && tag.getMediaRating()!=null && tag.getMediaRating().getMedia()!=null) {
            tag.setCount(tag.getMediaRating().getMedia().getCount());
        }
        return tag;
    }

    public GraphQlResponse<PageObject<Account>> mapFollow(InputStream jsonStream){
        return mapObject(jsonStream, "me/postaddict/instagram/scraper/model/follow.json");
    }

    public GraphQlResponse<PageObject<Account>> mapFollowers(InputStream jsonStream) {
        return mapObject(jsonStream, "me/postaddict/instagram/scraper/model/followers.json");
    }

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public ActionResponse<Comment> mapMediaCommentResponse(InputStream jsonStream) {
        ObjectMapper objectMapper = mapperThreadLocal.get();
        Map<String, Object> json = objectMapper.readValue(jsonStream, jsonMapTypeReference);
        ActionResponse<Comment> commentActionResponse = new ActionResponse<>();
        commentActionResponse.setStatus(json.get("status").toString());
        Comment comment = new Comment();
        comment.setId(Long.parseLong((String) json.get("id")));
        comment.setText((String) json.get("text"));
        comment.setCreatedAt(((Integer) json.get("created_time")).longValue());
        Account owner = new Account();
        Map<String, String> from = (Map<String, String>) json.get("from");
        owner.setId(Long.parseLong(from.get("id")));
        owner.setUsername(from.get("username"));
        owner.setFullName(from.get("full_name"));
        owner.setProfilePicUrl(from.get("profile_picture"));
        comment.setOwner(owner);
        commentActionResponse.setPayload(comment);
        return commentActionResponse;
    }

    @Override
    @SneakyThrows
    public String getLastMediaShortCode(InputStream jsonStream) {
        Map<String,Object> jsonMap = mapperThreadLocal.get().readValue(jsonStream, jsonMapTypeReference);
        Node jsonDom = new DomTransformer(new NopTypeConverter()).transform(Collections.singletonMap("root",jsonMap));
        return XPathFactory.newInstance().newXPath().evaluate("//shortcode", jsonDom);
    }

    @SuppressWarnings("unchecked")
    protected  <T> T mapObject(InputStream jsonStream, String mappingFile){
        try {
            ThreadLocal<Unmarshaller> unmarshaller = unmarshallerCache.computeIfAbsent(mappingFile, mapping -> ThreadLocal.withInitial(() -> getUnmarshaller(mapping)));
            return (T) unmarshaller.get().unmarshal(jsonStream);
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @SneakyThrows
    protected Unmarshaller getUnmarshaller(String mappingFile){
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(JAXBContextProperties.OXM_METADATA_SOURCE, mappingFile);
        properties.put(JAXBContextProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader()==null?
                Account.class.getClassLoader():Thread.currentThread().getContextClassLoader();

        JAXBContext jaxbContext = DynamicJAXBContextFactory.createContextFromOXM(classLoader, properties);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        unmarshaller.setProperty(UnmarshallerProperties.MEDIA_TYPE, MediaType.APPLICATION_JSON);
        return unmarshaller;
    }

}
