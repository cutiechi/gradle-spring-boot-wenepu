package com.cutiechi.wenepu.handler;

import com.cutiechi.wenepu.exception.NepuServerErrorException;
import com.cutiechi.wenepu.model.dto.JsonResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 *
 * @author Cutie Chi
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * 东北石油大学教务管理系统服务器错误异常处理方法
     *
     * @param exception 东北石油大学教务管理系统服务器错误异常对象
     * @return JSON 响应
     */
    @ExceptionHandler(NepuServerErrorException.class)
    @ResponseBody
    public JsonResponse nepuServerErrorExceptionHandle (NepuServerErrorException exception) {
        return new JsonResponse(50000, exception.getMessage());
    }
}
