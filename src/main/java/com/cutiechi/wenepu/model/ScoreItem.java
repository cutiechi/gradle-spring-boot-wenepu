package com.cutiechi.wenepu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 一个科目的成绩
 *
 * @author Cutie Chi
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ScoreItem {

    /**
     * 课程 ID
     */
    private String courseId;

    /**
     * 课程开课学期
     */
    private String courseSemesterOriginal;

    /**
     * 课程名称
     */
    private String courseName;

    /**
     * 课程成绩
     */
    private String courseScore;

    /**
     * 获取课程分数详情的 URI
     */
    private String courseScoreDetailUri;

    /**
     * 课程成绩标志
     */
    private String courseScoreSign;


    /**
     * 课程性质
     */
    private String courseNature;

    /**
     * 课程类别
     */
    private String courseType;

    /**
     * 课程学时
     */
    private String coursePeriod;

    /**
     * 课程学分
     */
    private String courseCredit;

    /**
     * 课程考试性质
     */
    private String courseExamNature;

    /**
     * 课程补重学期
     */
    private String courseSupplementSemester;
}
