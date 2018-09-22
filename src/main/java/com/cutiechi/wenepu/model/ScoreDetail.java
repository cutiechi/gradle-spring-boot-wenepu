package com.cutiechi.wenepu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 成绩详情
 *
 * @author Cutie Chi
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class ScoreDetail {

    /**
     * 平时成绩
     */
    private String dailyScore;

    /**
     * 平时成绩比例
     */
    private String dailyScoreRatio;

    /**
     * 期中成绩
     */
    private String midtermScore;

    /**
     * 其中成绩比例
     */
    private String midtermScoreRatio;

    /**
     * 期末成绩
     */
    private String endtermScore;

    /**
     * 期末成绩比例
     */
    private String endtermScoreRatio;

    /**
     * 总成绩
     */
    private String totalScore;
}
