package com.cutiechi.wenepu.service;

import com.cutiechi.wenepu.ApplicationTests;
import com.cutiechi.wenepu.exception.NepuServerErrorException;
import com.cutiechi.wenepu.model.dto.ServiceResult;
import com.cutiechi.wenepu.web.form.AuthenticationForm;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 鉴权业务逻辑测试类
 *
 * @author Cutie
 */
class AuthenticationServiceTests extends ApplicationTests {

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * 测试获取 Web Token
     */
    @Test
    void testGetWebToken () {

        // 学生学号, 这里修改为你的 12 位学号
        final String studentNumber = "";

        // 学生密码, 这里修改为你的密码
        final String studentPassword = "";

        // 实例化鉴权表单对象
        final AuthenticationForm authenticationForm = new AuthenticationForm(studentNumber, studentPassword);

        try {

            // 获取 Web Token
            ServiceResult result = authenticationService.getWebToken(authenticationForm);

            // 打印结果, 结果取决于你的学号和密码是否正确
            System.out.println(result.getMessage());
        } catch (NepuServerErrorException exception) {

            // 当东北石油大学教务管理系统服务器错误的时候会抛出这个异常
            System.out.println("服务器错误，获取 Web Token 失败！");
        }
    }
}
