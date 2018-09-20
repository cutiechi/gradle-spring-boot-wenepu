package com.cutiechi.wenepu.config;

import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 开课学期配置类, 和开课学期有关的业务逻辑需要读取该配置类中的值, 该类与 application.yml 中前缀为 wenepu.semester 的对应
 *
 * @author Cutie Chi
 */
@Component
@ConfigurationProperties(prefix = "wenepu.semester")
@Getter
@Setter
public class SemesterConfig {

    /**
     * 请求携带数据中方法的 Key
     */
    private String methodKey;

    /**
     * 请求携带的 Cookie 的 Key
     */
    private String cookieKey;

    /**
     * 请求超时时间
     */
    private Integer timeout;

    /**
     * 获取全部开课学期列表的 URL
     */
    private String listAllUrl;

    /**
     * 请求携带数据中方法的值
     */
    private String methodValue;

    /**
     * 当请求错误时的标题
     */
    private String errorTitle;

    /**
     * 网页中开课学期选择框的 ID
     */
    private String selectElementId;

    /**
     * 网页中开课学期具体值元素的名称
     */
    private String optionElementName;

    /**
     * 春季学期, 也就是下学期在获取到的学期的原始值的最后一位
     */
    private String springSemesterOriginalEndWith;

    /**
     * 春季学期, 也就是下学期的别名
     */
    private String springSemesterAlias;

    /**
     * 秋季学期, 也就是上学期的别名
     */
    private String autumnSemesterAlias;

    /**
     * 开课学期别名中开课年份和开课季节的分隔符
     */
    private String semesterAliasSeparator;
}
