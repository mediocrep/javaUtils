package com.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * JSON工具类
 *
 * @author 张伟
 * @version 1.0.0
 * @date 2021-11-25
 */
@Slf4j
public class JsonUtils {

    /**
     * 定义静态jackson对象
     */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public enum JSON_TYPE{
        /**JSONObject*/
        JSON_TYPE_OBJECT,
        /**JSONArray*/
        JSON_TYPE_ARRAY,
        /**不是JSON格式的字符串*/
        JSON_TYPE_ERROR
    }

    /**
     * 根据json字符串获取json的类型
     *
     * @param str json字符串
     * @return json的类型，包括json对象，json数组，非json格式
     */
    public static JSON_TYPE getJSONType(String str){
        if(StringUtils.isNotBlank(str)){
            return JSON_TYPE.JSON_TYPE_ERROR;
        }

        final char[] strChar = str.substring(0, 1).toCharArray();
        final char firstChar = strChar[0];

        if(firstChar == '{'){
            return JSON_TYPE.JSON_TYPE_OBJECT;
        }else if(firstChar == '['){
            return JSON_TYPE.JSON_TYPE_ARRAY;
        }else{
            return JSON_TYPE.JSON_TYPE_ERROR;
        }
    }

    /**
     * 将java对象转换成json字符串
     *
     * @param object java对象
     * @return json字符串
     */
    public static String objectToJsonStr(Object object) {
        try {
            String string = OBJECT_MAPPER.writeValueAsString(object);
            return string;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    /**
     * 将json字符串转化为java对象
     *
     * @param jsonStr json字符串
     * @param beanType java对象的类型
     * @return
     */
    public static <T> T jsonStrToObject(String jsonStr, Class<T> beanType) {
        try {
            T t = OBJECT_MAPPER.readValue(jsonStr, beanType);
            return t;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json字符串转换成java对象list
     *
     * @param jsonData json字符串
     * @param beanType java对象的类型
     * @return java对象list
     */
    public static <T> List<T> jsonStrToList(String jsonData, Class<T> beanType) {
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
        try {
            List<T> list = OBJECT_MAPPER.readValue(jsonData, javaType);
            return list;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }

    /**
     * 将json字符串转换成Map类型
     *
     * @param jsonStr json字符串
     * @return Map
     */
    public static Map<String, Object> jsonStrToMap(String jsonStr) {
        return jsonStrToObject(jsonStr, Map.class);
    }

    /**
     * 将json字符串转换成JsonNode类型
     *
     * @param jsonStr json字符串
     * @return Map
     */
    public static JsonNode jsonStrToJsonNode(String jsonStr) {
        return jsonStrToObject(jsonStr, JsonNode.class);
    }

    /**
     * 将java对象转换成JsonNode对象
     *
     * @param object java对象
     * @return json字符串
     */
    public static JsonNode objectToJsonNode(Object object) {
        try {
            String jsonStr = OBJECT_MAPPER.writeValueAsString(object);
            final JsonNode jsonNode = jsonStrToObject(jsonStr, JsonNode.class);
            return jsonNode;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;
    }


}
