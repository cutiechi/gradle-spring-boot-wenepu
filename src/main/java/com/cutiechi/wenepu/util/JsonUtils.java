package com.cutiechi.wenepu.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import java.util.Map;

/**
 * JSON 工具类
 *
 * @author Cutie Chi
 */
@Component
public final class JsonUtils {

    /**
     * Jackson 中的 ObjectMapper
     */
    private final ObjectMapper objectMapper;

    /**
     * Spring 自动注入
     *
     * @param objectMapper Jackson Object Mapper 实例
     */
    @Autowired
    public JsonUtils (ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * 根据 Key 获取 Object
     *
     * @param data JSON 字符串
     * @param key Key
     * @return Object
     * @throws IOException 获取不到, 或者 JSON 字符串格式错误时会抛出 IOException
     */
    public Object getObject (String data, String key) throws IOException {

        // 使用 Jackson Object Mapper 的 readValue 方法将 JSON 字符串序列化为 Map, 根据 Key 获取 Object
        return objectMapper
            .readValue(data, Map.class)
            .get(key);
    }
}
