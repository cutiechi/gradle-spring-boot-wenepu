package com.cutiechi.wenepu.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Getter;
import lombok.Setter;

/**
 * JSON 响应数据传输类, 所有的 API 都返回该对象经过序列化的 JSON
 *
 * @author Cutie Chi
 */
@Getter
@Setter
public class JsonResponse {

    /**
     * 状态编码
     */
    private Integer code;

    /**
     * 附带消息
     */
    private String message;

    /**
     * 数据
     */
    @JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
    private Object data;

    public JsonResponse (Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public JsonResponse (Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
