package com.cutiechi.wenepu.web.form;


import lombok.Getter;
import lombok.Setter;

/**
 * 鉴权表单
 *
 * @author Cutie Chi
 */
@Getter
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
