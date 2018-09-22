package com.cutiechi.wenepu.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 成绩配置类, 和成绩有关的业务逻辑需要读取该配置类中属性的值, 该类与 application.yml 中前缀为 wenepu.score 的对应
 */
@Component
@ConfigurationProperties(prefix = "wenepu.score")
@Getter
@Setter
public class ScoreConfig {

    /**
     * 请求携带数据中方法的 Key
     */
    private String methodKey;

    /**
     * 请求携带数据中 Cookie 的 Key
     */
    private String cookieKey;

    /**
     * 请求超时时间
     */
    private Integer timeout;

    /**
     * 请求错误时的标题
     */
    private String errorTitle;

    /**
     * td 元素名称
     */
    private String tdElementName;

    /**
     * span 元素名称
     */
    private String spanElementName;

    /**
     * tr 元素名称
     */
    private String trElementName;

    /**
     * a 元素名称
     */
    private String aElementName;

    /**
     * 属性 Onclick 的 Key
     */
    private String attributeOnclickKey;

    /**
     * 根据开课学期原始值获取成绩所请求的 URL
     */
    private String getBySemesterOriginalUrl;

    /**
     * 根据开课学期原始值获取成绩的请求携带数据中方法的值
     */
    private String getBySemesterOriginalMethodValue;

    /**
     * 根据开课学期原始值获取成绩的请求携带数据中开课学期原始值的 Key
     */
    private String getBySemesterOriginalSemesterOriginalKey;

    /**
     * 总学分和平均绩点所在 td 的父元素 ID
     */
    private String totalCreditTdFatherElementId;

    /**
     * 每一条成绩项所在 tr 的父元素 ID
     */
    private String scoreItemTrFatherElementId;

    /**
     * 根据课程成绩详情 URI 获取课程成绩详情请求的 URL
     */
    private String getDetailByDetailUriBaseUrl;
}
