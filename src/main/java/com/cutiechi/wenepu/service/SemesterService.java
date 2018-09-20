package com.cutiechi.wenepu.service;

import com.cutiechi.wenepu.exception.NepuServerErrorException;
import com.cutiechi.wenepu.model.dto.ServiceResult;

/**
 * 开课学期业务逻辑接口
 *
 * @author Cutie Chi
 */
public interface SemesterService {

    /**
     * 获取全部开课学期列表
     *
     * @param webToken Web Token
     * @return 附带全部开课学期列表的业务逻辑结果
     * @throws NepuServerErrorException 东北石油大学教务管理系统服务器错误异常
     */
    ServiceResult listAll (final String webToken) throws NepuServerErrorException;
}
