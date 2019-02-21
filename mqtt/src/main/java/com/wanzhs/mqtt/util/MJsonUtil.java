package com.wanzhs.mqtt.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component("mJsonUtil")
public class MJsonUtil {
    /**
     * @author:luqi
     * @description: 字符串转对象，注意不能转集合对象
     * @date:2018/9/3_11:33
     * @param: json字符串
     * @param: class对象
     * @return: 返回泛型对象
     */
    public <T> T jsonStrToObject(String jsonStr, Class<?> cls) {
        ObjectMapper mapper = new ObjectMapper();
        Object object = null;
        try {
            object = mapper.readValue(jsonStr, cls);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return (T) object;
    }


    /**
     * @author:luqi
     * @description: 对象变为json字符串
     * @date:2018/9/3_11:37
     * @param: 对象，注意不能是集合对象
     * @return: json字符串
     */
    public String ObjectToJsonStr(Object c) {
        String jsonStr = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            jsonStr = mapper.writeValueAsString(c);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }

    /**
     * @author:luqi
     * @description: list集合对象转json字符串
     * @date:2018/9/3_11:45
     * @param: list对象
     * @return: json字符串
     */
    public String listToJsonStr(List<?> list) {
        String jsonStr = "";
        ObjectMapper mapper = new ObjectMapper();
        try {
            jsonStr = mapper.writeValueAsString(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }


    /**
     * @author:luqi
     * @description: json字符串转List集合对象
     * @date:2018/9/3_11:41
     * @param: json字符串
     * @param: list集合类的类型
     * @return: List泛型对象
     */
    public List<?> jsonStrToList(String jsonStr, Class<?> cls) {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = getCollectionType(mapper, ArrayList.class, cls);
        List<?> lst = null;
        try {
            lst = (List<?>) mapper.readValue(jsonStr, javaType);
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lst;
    }


    /**
     * @author:luqi
     * @description: 字符串转义json字符串
     * @date:2018/9/3_11:46
     * @param: 字符串
     * @return: json字符串
     */
    public String stringToJson(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '\"':
                    sb.append("\\\"");
                    break;
                case '\\':
                    sb.append("\\\\");
                    break;
                case '/':
                    sb.append("\\/");
                    break;
                case '\b':
                    sb.append("\\b");
                    break;
                case '\f':
                    sb.append("\\f");
                    break;
                case '\n':
                    sb.append("\\n");
                    break;
                case '\r':
                    sb.append("\\r");
                    break;
                case '\t':
                    sb.append("\\t");
                    break;
                default:
                    sb.append(c);
            }
        }
        return sb.toString();
    }


    /**
     * @author:luqi
     * @description: 获取集合的类型，该方法不能对外使用
     * @date:2018/9/3_11:39
     * @param: ObjectMapper对象
     * @param: 集合class对象
     * @param: 集合元素class对象
     * @return:
     */
    @SuppressWarnings("deprecation")
    private JavaType getCollectionType(ObjectMapper mapper, Class<?> collectionClass,
                                       Class<?>... elementClasses) {
        return mapper.getTypeFactory().constructParametricType(collectionClass,
                elementClasses);
    }

}
