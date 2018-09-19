package com.cutiechi.wenepu.service;

import com.cutiechi.wenepu.exception.NepuServerErrorException;
import com.cutiechi.wenepu.model.dto.ServiceResult;
import com.cutiechi.wenepu.web.form.AuthenticationForm;

/**
 * 鉴权业务逻辑接口
 *
 * @author Cutie Chi
 */
public interface AuthenticationService {


    /**
     * 获取 Web Token, 该 Token 本质为 JSESSIONID, 请求东北石油大学教务系统时需该 Token
     *
     * @param authenticationForm 鉴权表单
     * @return 附带 Web Token 的业务逻辑结果数据传输对象
     * @throws NepuServerErrorException 东北石油大学教务管理系统服务器错误异常
     */
    ServiceResult getWebToken (AuthenticationForm authenticationForm) throws NepuServerErrorException;
}
