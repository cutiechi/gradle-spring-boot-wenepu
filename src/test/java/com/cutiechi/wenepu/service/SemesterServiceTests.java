package com.cutiechi.wenepu.service;

import com.cutiechi.wenepu.ApplicationTests;
import com.cutiechi.wenepu.exception.NepuServerErrorException;
import com.cutiechi.wenepu.model.dto.ServiceResult;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * 开课学期业务逻辑测试类
 *
 * @author Cutie Chi
 */
class SemesterServiceTests extends ApplicationTests {

    /**
     * 自动注入开课学期业务逻辑对象
     */
    @Autowired
    private SemesterService semesterService;

    /**
     * 测试获取全部开课学期列表
     */
    @Test
    void testListAll () {

        // 使用你的 Web Token 替换下面的空字符串
        final String webToken = null;
        try {

            // 获取开课学期列表
            ServiceResult result = semesterService.listAll(webToken);

            // 返回消息的结果取决于你 Web Token 的正确性
            System.out.println(result.getMessage());
        } catch (NepuServerErrorException exception) {

            // 当东北石油大学教务管理系统打不开获取服务器错误的时候打印
            System.out.println("服务器错误，获取全部开课学期列表失败！");
        }
    }
}
