package com.cutiechi.wenepu.model.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 业务逻辑层统一返回结果数据传输对象
 *
 * @author Cutie Chi
 */
@Getter
@Setter
public final class ServiceResult<T> {

    /**
     * 业务逻辑是否成功
     */
    private Boolean status;

    /**
     * 附带信息
     */
    private String message;

    /**
     * 附带结果
     */
    private T result;

    private ServiceResult () {

    }

    private ServiceResult (Boolean status, String message) {
        this.status = status;
        this.message = message;
    }

    private ServiceResult (Boolean status, String message, T result) {
        this.status = status;
        this.message = message;
        this.result = result;
    }

    /**
     * 业务逻辑成功
     *
     * @param message 附带信息
     * @return 业务逻辑结果数据传输对象
     */
    public static ServiceResult success (String message) {
        return new ServiceResult(true, message);
    }

    /**
     * 业务逻辑成功并附带结果
     *
     * @param message 附带信息
     * @param result  附带结果
     * @param <T>     附带结果类型
     * @return 业务逻辑结果数据传输对象
     */
    public static <T> ServiceResult<T> success (String message, T result) {
        return new ServiceResult<>(true, message, result);
    }

    /**
     * 业务逻辑失败
     *
     * @param message 附带信息
     * @return 业务逻辑结果数据传输对象
     */
    public static ServiceResult fail (String message) {
        return new ServiceResult(false, message);
    }
}
