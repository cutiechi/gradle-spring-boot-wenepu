package com.cutiechi.wenepu.service;

import com.cutiechi.wenepu.ApplicationTests;
import com.cutiechi.wenepu.exception.NepuServerErrorException;
import com.cutiechi.wenepu.model.dto.ServiceResult;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;


/**
 * 成绩业务逻辑测试类
 *
 * @author Cutie Chi
 */
class ScoreServiceTests extends ApplicationTests {

    /**
     * 自动注入成绩业务逻辑
     */
    @Autowired
    private ScoreService scoreService;

    /**
     * 测试根据开课学期原始值获取成绩
     */
    @Test
    void testGetBySemesterOriginal () {
        try {

            // 开课学期原始值
            final String semesterOriginal = "2017-2018-2";

            // 使用你的 Web Token 替换下面的空字符串
            final String webToken = "";

            // 获取成绩
            ServiceResult result = scoreService.getBySemesterOriginal(semesterOriginal, webToken);

            // 返回结果的消息取决于你的 Web Token 的正确性
            System.out.println(result.getMessage());
        } catch (NepuServerErrorException exception) {

            // 当东北石油大学教务管理系统打不开获取服务器错误的时候打印
            System.out.println("服务器错误，获取成绩失败！");
        }
    }
}
