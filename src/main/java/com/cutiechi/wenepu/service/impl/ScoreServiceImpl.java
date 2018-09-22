package com.cutiechi.wenepu.service.impl;

import com.cutiechi.wenepu.config.ScoreConfig;
import com.cutiechi.wenepu.exception.NepuServerErrorException;
import com.cutiechi.wenepu.model.Score;
import com.cutiechi.wenepu.model.ScoreDetail;
import com.cutiechi.wenepu.model.ScoreItem;
import com.cutiechi.wenepu.model.dto.ServiceResult;
import com.cutiechi.wenepu.service.ScoreService;

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
 * 成绩业务逻辑实现类
 */
@Service
public class ScoreServiceImpl implements ScoreService {

    /**
     * 和成绩业务逻辑有关的配置对象
     */
    private final ScoreConfig scoreConfig;

    /**
     * Spring 自动注入
     *
     * @param scoreConfig 成绩配置对象
     */
    @Autowired
    public ScoreServiceImpl (ScoreConfig scoreConfig) {
        this.scoreConfig = scoreConfig;
    }

    /**
     * 根据开课学期的原始值获取成绩
     *
     * @param semesterOriginal 从东北石油大学教务管理系统 Option 元素获取到的开课学期原始值
     * @param webToken Web Token
     * @return 附带成绩的业务逻辑结果
     * @throws NepuServerErrorException 东北石油大学教务管理系统服务器错误异常
     */
    @Override
    public ServiceResult getBySemesterOriginal (String semesterOriginal, String webToken) throws NepuServerErrorException {

        // 获取 Jsoup 连接对象
        final Connection connection = Jsoup
            .connect(scoreConfig.getGetBySemesterOriginalUrl())
            .data(scoreConfig.getMethodKey(), scoreConfig.getGetBySemesterOriginalMethodValue())
            .data(scoreConfig.getGetBySemesterOriginalSemesterOriginalKey(), semesterOriginal)
            .cookie(scoreConfig.getCookieKey(), webToken)
            .timeout(scoreConfig.getTimeout());

        try {

            // 获取 Jsoup 连接对象 POST 请求返回的文档对象, 此时可能发生 IOException
            final Document document = connection.post();

            // 当文档对象的标题不是请求错误时的标题继续
            if (!scoreConfig.getErrorTitle().equals(document.title())) {

                // 获取总学分和平均绩点所在的 td 中所有 span 的集合对象
                final Elements totalCreditTdSpans = document
                    .getElementById(scoreConfig.getTotalCreditTdFatherElementId())
                    .select(scoreConfig.getTdElementName())
                    .get(0)
                    .select(scoreConfig.getSpanElementName());

                // 第一个 span 元素中 text 的值为总学分
                final String totalCredit = totalCreditTdSpans
                    .get(0)
                    .text();

                // 第四个 span 元素中 text 的值去掉末尾的句号为平均绩点
                final String gradlePointAverage = totalCreditTdSpans
                    .get(3)
                    .text()
                    .replace("。", "");
                List<ScoreItem> scoreItems = document

                    // 通过元素 ID 获取每一条成绩项所在 tr 的父元素对象
                    .getElementById(scoreConfig.getScoreItemTrFatherElementId())

                    // 通过元素名称获取每一条成绩项所在 tr 父元素对象中所有 tr 的集合对象
                    .select(scoreConfig.getTrElementName())

                    // 获取 tr 对象的流
                    .stream()

                    // 遍历 tr 对象的流, 返回成绩项对象的流
                    .map(tr -> {

                        // 获取每一条成绩项所在 tr 中所有 td 的集合对象
                        final Elements tds = tr.select(scoreConfig.getTdElementName());

                        // 课程成绩所在 a 为第六个 td 中的第一个元素
                        final Element courseScoreA = tds.get(5).selectFirst(scoreConfig.getAElementName());

                        // 获取 a 的 onclick 得值
                        final String courseScoreAAttrOnClick = courseScoreA.attr(scoreConfig.getAttributeOnclickKey());

                        // 返回成绩项对象
                        return new ScoreItem(

                            // 第一个 td 中的文本值为课程 ID
                            tds.get(0).text(),

                            // 第四个 td 中的文本值为课程开课学期
                            tds.get(3).text(),

                            // 第五个 td 中的文本值为课程名称
                            tds.get(4).text(),

                            // 第六个 td 中的第一个 a 的文本值为课程成绩
                            courseScoreA.text(),

                            // 第六个 td 中的第一个 a 的 onclick 属性的值从 '&' 到 ''' 之间为获取成绩详情需要的 URI
                            courseScoreAAttrOnClick.substring(courseScoreAAttrOnClick.indexOf("&") + 1, courseScoreAAttrOnClick.lastIndexOf("'")),

                            // 第七个 td 中的文本值为课程成绩标志
                            tds.get(6).text(),

                            // 第八个 td 中的文本值为课程性质
                            tds.get(7).text(),

                            // 第九个 td 中的文本值为课程类别
                            tds.get(8).text(),

                            // 第十个 td 中的文本值为课程学时
                            tds.get(9).text(),

                            // 第十一个 td 中的文本值为课程学分
                            tds.get(10).text(),

                            // 第十二个 td 中的文本值为课程考试性质
                            tds.get(11).text(),

                            // 第十三个 td 中的文本值为课程补重学期
                            tds.get(12).text()
                        );
                    })

                    // 将成绩项的流转为 List
                    .collect(Collectors.toList());

                // 返回附带成绩的业务逻辑结果
                return ServiceResult.success("获取成绩成功！", new Score(
                    totalCredit,
                    gradlePointAverage,
                    scoreItems
                ));
            } else {

                // 当文档标题时请求错误时的标题则返回附带错误信息的业务逻辑结果对象
                return ServiceResult.fail("Web Token 错误，获取成绩失败！");
            }
        } catch (IOException exception) {

            // 当发生 IOException 时说明东北石油大学教务管理系统连接失败, 抛出异常
            throw new NepuServerErrorException("服务器错误，获取成绩失败！");
        }
    }

    /**
     * 根据课程成绩详情 URI 获取课程成绩详情
     *
     * @param webToken Web Token
     * @param detailUri 从东北石油大学教务管理系统 a 元素 attribute 获取到的 URI
     * @return 附带成绩详情的业务逻辑结果
     * @throws NepuServerErrorException 东北石油大学教务管理系统服务器错误异常
     */
    @Override
    public ServiceResult getDetailByDetailUri (String webToken, String detailUri) throws NepuServerErrorException {

        // 拼接获取成绩详情请求的完整 URL
        final String url = scoreConfig.getGetDetailByDetailUriBaseUrl() + detailUri;

        // 获取 Jsoup 连接对象
        final Connection connection = Jsoup
            .connect(url)
            .cookie(scoreConfig.getCookieKey(), webToken)
            .timeout(scoreConfig.getTimeout());

        try {

            // 获取 Jsoup 连接对象 POST 请求返回的文档对象, 此时可能发生 IOException
            Document document = connection.post();

            // 当文档对象的标题不是请求错误时的标题继续
            if (!scoreConfig.getErrorTitle().equals(document.title())) {
                Elements tds = document

                    // 通过元素 ID 获取成绩详情所在 tr 的父元素对象
                    .getElementById(scoreConfig.getScoreItemTrFatherElementId())

                    // 获取 tr 中的所有 td
                    .select(scoreConfig.getTdElementName());

                // 返回附带成绩详情的业务逻辑结果
                return ServiceResult.success("获取成绩详情成功！", new ScoreDetail(

                    // 第一个 td 的文本值为平时成绩
                    tds.get(0).text(),

                    // 第二个 td 的文本值为平时成绩比例
                    tds.get(1).text(),

                    // 第三个 td 的文本值为期中成绩
                    tds.get(2).text(),

                    // 第四个 td 的文本值为期中成绩比例
                    tds.get(3).text(),

                    // 第五个 td 的文本值为期末成绩
                    tds.get(4).text(),

                    // 第六个 td 的文本值为期末成绩比例
                    tds.get(5).text(),

                    // 第七个 td 的文本值为总成绩
                    tds.get(6).text()
                ));
            } else {
                // 当文档标题时请求错误时的标题则返回附带错误信息的业务逻辑结果对象
                return ServiceResult.fail("Web Token 错误，获取成绩详情失败！");
            }
        } catch (IOException exception) {

            // 当发生 IOException 时说明东北石油大学教务管理系统连接失败, 抛出异常
            throw new NepuServerErrorException("服务器错误，获取成绩详情失败！");
        }
    }
}
