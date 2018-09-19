package com.cutiechi.wenepu.web.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 鉴权表单
 *
 * @author Cutie Chi
 */
@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class AuthenticationForm {

    /**
     * 学生编号
     */
    private String studentNumber;

    /**
     * 学生密码
     */
    private String studentPassword;
}
