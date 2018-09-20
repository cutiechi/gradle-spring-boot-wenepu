package com.cutiechi.wenepu.config;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 鉴权配置类
 *
 * @author Cutie Chi
 */
@Component
@ConfigurationProperties(prefix = "wenepu.authentication")
@Getter
@Setter
public final class AuthenticationConfig {


    // 获取 Web 验证信息所请求的 URL
    private String webVerificationUrl;

    // JSESSIONID 在响应头部中的 Key
    private String headerJSessionIdKey;

    // 获取 Web Token 请求的东北石油大学教务管理系统 URL 地址
    private String loginUrl;

    // 登录成功后第一次重定向的 URL 地址
    private String firstRedirectUrl;

    // 登录成功后第二次重定向的 URL 地址
    private String secondRedirectUrl;

    // 请求所携带 Cookie 的 KEY
    private String cookieKey;

    // 请求方法的 KEY
    private String methodKey;

    // 请求方法的值
    private String methodValue;

    private Integer errorTitleLength;

    // 第二次重定向请求方法的值
    private String secondRedirectMethodValue;

    // 请求携带数据学生编号的 KEY
    private String studentNumberKey;

    // 请求携带数据学生密码的 KEY
    private String studentPasswordKey;

    // 请求携带数据验证码的 KEY
    private String verificationCodeKey;

    // 请求超时时间
    private Integer timeout;
}
