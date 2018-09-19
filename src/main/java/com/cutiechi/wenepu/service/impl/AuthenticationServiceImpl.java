package com.cutiechi.wenepu.service.impl;

import com.cutiechi.wenepu.exception.NepuServerErrorException;
import com.cutiechi.wenepu.service.AuthenticationService;
import com.cutiechi.wenepu.util.VerificationCodeImageUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;

import java.io.IOException;

/**
 * 鉴权业务逻辑实现类
 *
 * @author Cutie Chi
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {


    /**
     * 获取 Web 验证信息时识别验证码图片的工具类
     */
    private final VerificationCodeImageUtils verificationCodeImageUtils;

    /**
     * Spring 自动注入依赖
     *
     * @param verificationCodeImageUtils 获取 Web 验证信息时识别验证码图片的工具类
     */
    @Autowired
    public AuthenticationServiceImpl (VerificationCodeImageUtils verificationCodeImageUtils) {
        this.verificationCodeImageUtils = verificationCodeImageUtils;
    }

    /**
     * 获取 Web 验证信息, 包括验证码和 Web Token
     *
     * 登录东北石油大学教务管理系统时需要验证码和 JSESSIONID, 也就是这里获取的 Web Token
     *
     * @return 包含验证码和 Web Token 的字符串数组
     * @throws NepuServerErrorException NEPU 服务器错误异常
     */
    private String[] getWebVerification () throws NepuServerErrorException {

        // 获取 Web 验证信息所请求的 URL
        final String WEB_VERIFICATION_URL = "http://jwgl.nepu.edddddu.cn/verifycode.servlet";

        // JSESSIONID 在响应头部中的 Key
        final String HEADER_JSESSIONID_KEY = "Set-Cookie";

        // 实例化 OkHttpClient 对象
        final OkHttpClient client = new OkHttpClient();

        // 实例化请求对象
        final Request request = new Request
            .Builder()
            .url(WEB_VERIFICATION_URL)
            .get()
            .build();

        // 定义响应对象
        Response response;

        // 实例化长度为 2 的字符串数组用来存放验证码和该验证码对应的 JSESSIONID
        final String[] webVerification = new String[2];

        try {

            // 由于验证码图片识别工具可能会错误的识别出 m , 所以只要识别后验证码不含 m 的结果
            do {
                // 请求获取响应, 这里可能会发生 IOException
                response = client.newCall(request).execute();

                // 响应状态码为 200 时继续
                if (HttpStatus.OK.value() == response.code()) {
                    // 实例化响应 Body 对象
                    ResponseBody body = response.body();

                    // Body 不为空
                    if (null != body) {

                        // 从响应 Body 的 字节流中读取验证码图片到 Buffered Image 对象中
                        BufferedImage verificationCodeImage = ImageIO.read(body.byteStream());

                        // 识别验证码图片
                        String verificationCode = verificationCodeImageUtils.recognition(verificationCodeImage);

                        // 由于这个验证码识别工具的可能会错误的识别 m, 只有不含 m 时继续
                        if (!verificationCode.contains("m")) {

                            // 从响应头部中读取 JSESSIONID
                            String JSESSIONID = response.header(HEADER_JSESSIONID_KEY);

                            // JSESSIONID 不为空继续
                            if (null != JSESSIONID) {

                                // 这里将从 JSESSIONID 中截取的部分字符串当做 Web Token, 其实本质还是 JSESSIONID
                                String webToken = JSESSIONID.substring(11, 43);

                                // 将验证码和 Web Token 存放到 web 验证信息中
                                webVerification[0] = verificationCode;
                                webVerification[1] = webToken;

                                // 结束循环
                                break;
                            }
                        }
                    }
                } else {

                    // 响应状态码不为 200 时抛出自定义的 NEPU 服务器错误异常
                    throw new NepuServerErrorException("服务器错误，获取 Web Token 失败！");
                }
            } while (true);
        } catch (IOException exception) {

            // 无法获取响应或读取验证码图片发生 IOException 异常时抛出自定义的 NEPU 服务器错误异常
            throw new NepuServerErrorException("服务器错误，获取 Web Token 失败！");
        }
        return webVerification;
    }
}
