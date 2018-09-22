package com.cutiechi.wenepu.web.controller;

import com.cutiechi.wenepu.exception.NepuServerErrorException;
import com.cutiechi.wenepu.model.dto.JsonResponse;
import com.cutiechi.wenepu.model.dto.ServiceResult;
import com.cutiechi.wenepu.service.ScoreService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 成绩控制器
 *
 * @author Cutie Chi
 */
@RequestMapping("/scores")
@RestController
public class ScoreController {

    /**
     * 成绩业务逻辑对象
     */
    private final ScoreService scoreService;

    /**
     * Spring 自动注入
     *
     * @param scoreService 成绩业务逻辑对象
     */
    @Autowired
    public ScoreController (ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    /**
     * 根据开课学期的原始值获取成绩, API 为 GET 方式请求 /scores/{semesterOriginal}
     *
     * @param webToken Web Token
     * @param semesterOriginal 开课学期的原始值
     * @return JSON 响应
     * @throws NepuServerErrorException 东北石油大学教务管理系统服务器错误异常
     */
    @GetMapping("/{semesterOriginal}")
    public JsonResponse getBySemesterOriginal (
        @RequestHeader("Web-Token") final String webToken,
        @PathVariable("semesterOriginal") final String semesterOriginal
    ) throws NepuServerErrorException {

        // 获取附带成绩的业务逻辑结果对象
        ServiceResult result = scoreService.getBySemesterOriginal(semesterOriginal, webToken);

        // 根据业务逻辑结果中的状态返回 JSON 响应
        return result.getStatus()
            ? new JsonResponse(20000, result.getMessage(), result.getResult())
            : new JsonResponse(40003, result.getMessage());
    }

    /**
     * 根据课程成绩详情 URI 获取课程成绩详情, API 为 GET 方式请求 /scores/details/{detailUri}
     *
     * @param webToken Web Token
     * @param detailUri 从东北石油大学教务管理系统 a 元素 attribute 获取到的 URI
     * @return JSON 响应
     * @throws NepuServerErrorException 东北石油大学教务管理系统服务器错误异常
     */
    @GetMapping("/details/{detailUri}")
    public JsonResponse getDetailByDetailUri (
        @RequestHeader("Web-Token") final String webToken,
        @PathVariable("detailUri") final String detailUri
    ) throws NepuServerErrorException {

        // 获取附带成绩详情的业务逻辑结果对象
        ServiceResult result = scoreService.getDetailByDetailUri(webToken, detailUri);

        // 根据业务逻辑结果中的状态返回 JSON 响应
        return result.getStatus()
            ? new JsonResponse(20000, result.getMessage(), result.getResult())
            : new JsonResponse(40003, result.getMessage());
    }
}
