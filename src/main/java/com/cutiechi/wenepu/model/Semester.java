package com.cutiechi.wenepu.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 开课学期
 *
 * @author Cutie Chi
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class Semester {

    /**
     * 开课学期别名
     */
    private String semesterAlias;

    /**
     * 从东北石油大学教务管理系统 Option 元素获取到的原始值, 查询成绩时需要该值
     */
    private String semesterOriginal;
}
