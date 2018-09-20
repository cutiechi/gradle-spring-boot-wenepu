package com.cutiechi.wenepu.web.controller;

import com.cutiechi.wenepu.exception.NepuServerErrorException;
import com.cutiechi.wenepu.model.dto.JsonResponse;
import com.cutiechi.wenepu.model.dto.ServiceResult;
import com.cutiechi.wenepu.service.SemesterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 开课学期控制器
 *
 * @author Cutie Chi
 */
@RequestMapping("/semesters")
@RestController
public final class SemesterController {

    /**
     * 开课学期业务逻辑对象
     */
    private final SemesterService semesterService;

    /**
     * Spring 构造函数自动注入
     *
     * @param semesterService 开课学期业务逻辑对象
     */
    @Autowired
    public SemesterController (SemesterService semesterService) {
        this.semesterService = semesterService;
    }

    /**
     * 获取全部开课学期列表, API 为 GET 方式请求 /semesters
     *
     * @param webToken Web Token
     * @return JSON 响应
     * @throws NepuServerErrorException 东北石油大学教务管理系统服务器错误异常
     */
    @GetMapping("")
    public final JsonResponse listAll (@RequestHeader("Web-Token") final String webToken) throws NepuServerErrorException {

        // 获取附带全部开课学期列表的业务逻辑结果对象
        ServiceResult result = semesterService.listAll(webToken);

        // 根据业务逻辑结果中的状态返回 JSON 响应
        return result.getStatus()
            ? new JsonResponse(20000, result.getMessage(), result.getResult())
            : new JsonResponse(40003, result.getMessage());
    }
}
