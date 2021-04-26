package cn.pioneeruniverse.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * @Author: yaojiaxin [yaojiaxin@pioneerservice.cn]
 * @Description: json工具类
 * @Date: Created in 16:42 2018/11/20
 * @Modified By:
 */
public class JsonUtil {
    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);
    private static JsonUtil jsonUtil;
    private ObjectMapper objectMapper = new ObjectMapper();
    private Gson gson;

    public static JsonUtil getInstance() {
        if (jsonUtil == null) {
            jsonUtil = new JsonUtil();
        }
        return jsonUtil;
    }

    private JsonUtil() {
        //初始化objectMapper
        initObjectMapper();
        initGson();
    }

    private void initObjectMapper() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        //允许带单引号字段名称
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        //允许不带引号的字段名称
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        // 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // 空值处理为空串
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object value, JsonGenerator jgen,
                                  SerializerProvider provider) throws IOException,
                    JsonProcessingException {
                jgen.writeString("");
            }
        });
        // 进行HTML解码。
        objectMapper.registerModule(new SimpleModule().addSerializer(String.class, new JsonSerializer<String>() {
            @Override
            public void serialize(String value, JsonGenerator jgen,
                                  SerializerProvider provider) throws IOException,
                    JsonProcessingException {
                jgen.writeString(StringEscapeUtils.unescapeHtml4(value));
            }
        }));
        // 设置时区
        objectMapper.setTimeZone(TimeZone.getDefault());//getTimeZone("GMT+8:00")
    }

    private void initGson() {
        gson = new GsonBuilder()
                .enableComplexMapKeySerialization()
                .excludeFieldsWithoutExposeAnnotation()//步骤1. 声明不包含的字段是注解方式
                .serializeNulls()
                .setDateFormat("yyyy-MM-dd")
                //.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_DASHES)//会把字段首字母大写
                .setPrettyPrinting()
                .setVersion(1.0)
                //.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter())//自定义类型适配器（自定义序列化，反序列方式）
                .disableHtmlEscaping()
                .create();
    }

    /**
     * Object可以是POJO，也可以是Collection或数组。
     * 如果对象为Null, 返回"null".
     * 如果集合为空集合, 返回"[]".
     */
    private String toJsonSupport(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (IOException e) {
            logger.warn("write to json string error:" + object, e);
            return null;
        }
    }


    /**
     * 反序列化POJO或简单Collection如List<String>.
     * <p>
     * 如果JSON字符串为Null或"null"字符串, 返回Null.
     * 如果JSON字符串为"[]", 返回空集合.
     * <p>
     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String,JavaType)
     */
    private <T> T fromJsonSupport(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 反序列化复杂Collection如List<Bean>, 先使用函數createCollectionType构造类型,然后调用本函数.
     */
    @SuppressWarnings("unchecked")
    private <T> T fromJsonSupport(String jsonString, JavaType javaType) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        }
        try {
            return (T) objectMapper.readValue(jsonString, javaType);
        } catch (IOException e) {
            logger.warn("parse json string error:" + jsonString, e);
            return null;
        }
    }

    /**
     * 构造泛型的Collection Type如:
     * ArrayList<MyBean>, 则调用constructCollectionType(ArrayList.class,MyBean.class)
     * HashMap<String,MyBean>, 则调用(HashMap.class,String.class, MyBean.class)
     */
    private JavaType createCollectionTypeSupport(Class<?> collectionClass, Class<?>... elementClasses) {
        return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    /**
     * 当JSON里只含有Bean的部分属性時，更新一个已存在Bean，只覆盖該部分的属性.
     */
    @SuppressWarnings("unchecked")
    private <T> T updateSupport(String jsonString, T object) {
        try {
            return (T) objectMapper.readerForUpdating(object).readValue(jsonString);
        } catch (JsonProcessingException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        } catch (IOException e) {
            logger.warn("update json string:" + jsonString + " to object:" + object + " error.", e);
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> T mapToObjectSupport(Map<String, Object> map, Class<T> clazz) {
        if (map == null) {
            return null;
        }
        return (T) objectMapper.convertValue(map, clazz);
    }


    public static String toJson(Object object) {
        return JsonUtil.getInstance().toJsonSupport(object);
    }

    public static <T> T fromJson(String jsonString, Class<T> clazz) {
        return JsonUtil.getInstance().fromJsonSupport(jsonString, clazz);
    }

    public static <T> T fromJson(String jsonString, JavaType javaType) {
        return JsonUtil.getInstance().fromJsonSupport(jsonString, javaType);
    }

    public static JavaType createCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return JsonUtil.getInstance().createCollectionTypeSupport(collectionClass, elementClasses);
    }

    public static <T> T update(String jsonString, T object) {
        return JsonUtil.getInstance().updateSupport(jsonString, object);
    }

    public static <T> T mapToObject(Map<String, Object> map, Class<T> clazz) {
        return JsonUtil.getInstance().mapToObjectSupport(map, clazz);
    }

    /*******************************************************************GSON******************************************************************/

    private String GsonStringSupport(Object object) {
        return gson.toJson(object);
    }

    private <T> T GsonToBeanSupport(String jsonString, Class<T> clazz) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        } else {
            return gson.fromJson(jsonString, clazz);
        }
    }

    private <T> List<T> GsonToListSupport(String jsonString) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        } else {
            return gson.fromJson(jsonString, new TypeToken<List<T>>() {
            }.getType());
        }
    }

    private <T> List<Map<String, T>> GsonToListMapsSupport(String jsonString) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        } else {
            return gson.fromJson(jsonString, new TypeToken<List<Map<String, T>>>() {
            }.getType());
        }
    }

    private <T> Map<String, T> GsonToMapsSupport(String jsonString) {
        if (StringUtils.isEmpty(jsonString)) {
            return null;
        } else {
            return gson.fromJson(jsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
    }

    public static String GsonString(Object object) {
        return JsonUtil.getInstance().GsonStringSupport(object);
    }

    public static <T> T GsonToBean(String jsonString, Class<T> clazz) {
        return JsonUtil.getInstance().GsonToBeanSupport(jsonString, clazz);
    }

    public static <T> List<T> GsonToList(String jsonString) {
        return JsonUtil.getInstance().GsonToListSupport(jsonString);
    }

    public static <T> List<Map<String, T>> GsonToListMaps(String jsonString) {
        return JsonUtil.getInstance().GsonToListMapsSupport(jsonString);
    }

    public static <T> Map<String, T> GsonToMaps(String jsonString) {
        return JsonUtil.getInstance().GsonToMapsSupport(jsonString);
    }
}
