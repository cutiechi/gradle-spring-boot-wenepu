package com.cutiechi.wenepu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 成绩
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
@ToString
public final class Score {

    /**
     * 总学分
     */
    private String totalCredit;

    /**
     * 平均绩点
     */
    private String gradePointAverage;

    /**
     * 包含每一个科目成绩的列表
     */
    private List<ScoreItem> scoreItems;
}
