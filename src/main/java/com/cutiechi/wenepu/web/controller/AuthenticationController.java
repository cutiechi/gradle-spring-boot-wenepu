package com.cutiechi.wenepu.web.controller;

import com.cutiechi.wenepu.exception.NepuServerErrorException;
import com.cutiechi.wenepu.model.dto.JsonResponse;
import com.cutiechi.wenepu.model.dto.ServiceResult;
import com.cutiechi.wenepu.service.AuthenticationService;
import com.cutiechi.wenepu.web.form.AuthenticationForm;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 鉴权控制器, 提供获取 Web Token 和 App Token 的 API
 *
 * @author Cutie Chi
 */
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController (AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * 获取 Web Token, API 为 /authentication/token/web
     *
     * @param authenticationForm 鉴权表单
     * @return JSON 响应
     * @throws NepuServerErrorException 东北石油大学教务管理系统服务器错误异常
     */
    @PostMapping("/token/web")
    public JsonResponse getWebToken (@RequestBody AuthenticationForm authenticationForm) throws NepuServerErrorException {
        ServiceResult result = authenticationService.getWebToken(authenticationForm);
        return result.getStatus()
            ? new JsonResponse(20000, result.getMessage(), result.getResult())
            : new JsonResponse(40001, result.getMessage());
    }

    /**
     * 获取 App Token, API 为 /authentication/token/app
     *
     * @param authenticationForm 鉴权表单
     * @return JSON 响应
     * @throws NepuServerErrorException 东北石油大学教务管理系统服务器错误异常
     */
    @PostMapping("/token/app")
    public JsonResponse getAppToken (@RequestBody AuthenticationForm authenticationForm) throws NepuServerErrorException {

        // 获取附带 App Token 的业务逻辑结果
        ServiceResult result = authenticationService.getAppToken(authenticationForm);

        // 根据业务逻辑结果中的状态返回 JSON 响应
        return result.getStatus()
            ? new JsonResponse(20000, result.getMessage(), result.getResult())
            : new JsonResponse(40001, result.getMessage());
    }
}
