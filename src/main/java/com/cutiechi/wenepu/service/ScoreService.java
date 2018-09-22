package com.cutiechi.wenepu.service;

import com.cutiechi.wenepu.exception.NepuServerErrorException;
import com.cutiechi.wenepu.model.dto.ServiceResult;

/**
 * 成绩业务逻辑接口
 *
 * @author Cutie Chi
 */
public interface ScoreService {

    /**
     * 根据开课学期的原始值获取成绩
     *
     * @param semesterOriginal 从东北石油大学教务管理系统 Option 元素获取到的开课学期原始值
     * @param webToken Web Token
     * @return 附带成绩的业务逻辑结果
     * @throws NepuServerErrorException 东北石油大学教务管理系统服务器错误异常
     */
    ServiceResult getBySemesterOriginal (final String semesterOriginal, final String webToken) throws NepuServerErrorException;
}
