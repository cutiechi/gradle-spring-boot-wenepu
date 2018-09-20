package com.cutiechi.wenepu.service.impl;

import com.cutiechi.wenepu.config.SemesterConfig;
import com.cutiechi.wenepu.exception.NepuServerErrorException;
import com.cutiechi.wenepu.model.Semester;
import com.cutiechi.wenepu.model.dto.ServiceResult;
import com.cutiechi.wenepu.service.SemesterService;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 开课学期业务逻辑实现类
 *
 * @author Cutie Chi
 */
@Service
public class SemesterServiceImpl implements SemesterService {

    /**
     * 开课学期配置
     */
    private final SemesterConfig semesterConfig;

    /**
     * 自动注入开课学期配置
     *
     * @param semesterConfig 开课学期配置对象
     */
    @Autowired
    public SemesterServiceImpl (SemesterConfig semesterConfig) {
        this.semesterConfig = semesterConfig;
    }

    /**
     * 获取全部开课学期列表
     *
     * @param webToken Web Token
     * @return 附带全部开课学期列表的业务逻辑结果
     * @throws NepuServerErrorException 东北石油大学教务管理系统服务器错误异常
     */
    @Override
    public ServiceResult listAll (final String webToken) throws NepuServerErrorException {

        // 实例化 Jsoup 连接对象
        final Connection connection = Jsoup
            .connect(semesterConfig.getListAllUrl())
            .data(semesterConfig.getMethodKey(), semesterConfig.getMethodValue())
            .cookie(semesterConfig.getCookieKey(), webToken)
            .timeout(semesterConfig.getTimeout());
        try {

            // 使用 Jsoup 连接对象的 POST 方式获取文档对象
            final Document document = connection.post();

            // 当文档对象的标题不是获取错误时的标题继续
            if (!semesterConfig.getErrorTitle().equals(document.title())) {

                // 从文档对象中获得到包含开课学期信息的 Option 对象集合
                final Elements options = document
                    .getElementById(semesterConfig.getSelectElementId())
                    .select(semesterConfig.getOptionElementName());

                // 获取春季学期, 也就是下学期的最后一位字符
                final String springSemesterOriginalEndWith = semesterConfig.getSpringSemesterOriginalEndWith();

                // 获取拼接开课学期别名中开课年份和开课季节之间的连接字符串
                final String semesterAliasSeparator = semesterConfig.getSemesterAliasSeparator();

                // 利用 Stream API 从 Option 对象集合中生成开课学期列表
                final List<Semester> semesters = options
                    .stream()

                    // 跳过第一个 Option , 第一个 Option 中的值为'全部学期', 暂时不用
                    .skip(1)

                    // 依次获取Option 元素中的值并返回原始开课学期的集合
                    .map(Element::html)

                    //遍历原始开课学期集合
                    .map(semesterOriginal -> {

                        // 根据原始开课学期的结尾判断是春季学期还是秋季学期
                        final String semesterSeason = semesterOriginal.endsWith(springSemesterOriginalEndWith)
                            ? semesterConfig.getSpringSemesterAlias()
                            : semesterConfig.getAutumnSemesterAlias();

                        // 根据开课学期的结尾判断后获取开课学年
                        final String semesterYear = semesterOriginal.endsWith(springSemesterOriginalEndWith)
                            ? semesterOriginal.substring(0, 4)
                            : semesterOriginal.substring(5, 9);

                        // 拼接开课学期别名, 格式为 '2016 年春季学期'
                        final String semesterAlias = semesterYear + semesterAliasSeparator + semesterSeason;

                        // 返回开课学期对象
                        return new Semester(semesterAlias, semesterOriginal);
                    })

                    // 转为 List
                    .collect(Collectors.toList());

                // 获取开课学期列表成功, 返回附带开课学期列表的业务逻辑结果
                return ServiceResult.success("获取全部开课学期列表成功！", semesters);
            } else {

                // 文档标题为错误标题时说明 Web Token 错误, 返回附带错误信息的业务逻辑结果
                return ServiceResult.fail("Web Token 错误，获取全部开课学期列表失败！");
            }
        } catch (IOException exception) {

            // 发生 IOException 说明东北石油大学教务管理系统服务器错误, 抛出异常
            throw new NepuServerErrorException("服务器错误，获取全部开课学期列表成功！");
        }
    }
}
