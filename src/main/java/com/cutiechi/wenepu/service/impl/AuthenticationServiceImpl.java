package com.cutiechi.wenepu.service.impl;

import com.cutiechi.wenepu.config.AuthenticationConfig;
import com.cutiechi.wenepu.exception.NepuServerErrorException;
import com.cutiechi.wenepu.model.dto.ServiceResult;
import com.cutiechi.wenepu.service.AuthenticationService;
import com.cutiechi.wenepu.util.VerificationCodeImageUtils;
import com.cutiechi.wenepu.web.form.AuthenticationForm;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

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
     * 鉴权配置类对象
     */
    private final AuthenticationConfig authenticationConfig;

    /**
     * 获取 Web 验证信息时识别验证码图片的工具类
     */
    private final VerificationCodeImageUtils verificationCodeImageUtils;

    /**
     * Spring 自动注入依赖
     *
     * @param verificationCodeImageUtils 获取 Web 验证信息时识别验证码图片的工具类
     * @param authenticationConfig       鉴权配置类对象
     */
    @Autowired
    public AuthenticationServiceImpl (VerificationCodeImageUtils verificationCodeImageUtils, AuthenticationConfig authenticationConfig) {
        this.verificationCodeImageUtils = verificationCodeImageUtils;
        this.authenticationConfig = authenticationConfig;
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

        // 实例化 OkHttpClient 对象
        final OkHttpClient client = new OkHttpClient();

        // 实例化请求对象
        final Request request = new Request
            .Builder()
            .url(authenticationConfig.getWebVerificationUrl())
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
                    final ResponseBody body = response.body();

                    // Body 不为空
                    if (null != body) {

                        // 从响应 Body 的 字节流中读取验证码图片到 Buffered Image 对象中
                        final BufferedImage verificationCodeImage = ImageIO.read(body.byteStream());

                        // 识别验证码图片
                        final String verificationCode = verificationCodeImageUtils.recognition(verificationCodeImage);

                        // 由于这个验证码识别工具的可能会错误的识别 m, 只有不含 m 时继续
                        if (!verificationCode.contains("m")) {

                            // 从响应头部中读取 JSESSIONID
                            final String JSESSIONID = response.header(authenticationConfig.getHeaderJSessionIdKey());

                            // JSESSIONID 不为空继续
                            if (null != JSESSIONID) {

                                // 这里将从 JSESSIONID 中截取的部分字符串当做 Web Token, 其实本质还是 JSESSIONID
                                final String webToken = JSESSIONID.substring(11, 43);

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


    /**
     * 获取 Web Token, 该 Token 本质为 JSESSIONID, 请求东北石油大学教务系统时需该 Token
     *
     * @param authenticationForm 鉴权表单
     * @return 附带 Web Token 的业务逻辑结果数据传输对象
     * @throws NepuServerErrorException 东北石油大学教务管理系统服务器错误异常
     */
    @Override
    public ServiceResult getWebToken (final AuthenticationForm authenticationForm) throws NepuServerErrorException {

        // 获取 Web 验证信息字符串数组
        final String[] webVerification = getWebVerification();

        // Web 验证信息数组的第二个元素为 Web Token
        final String webToken = webVerification[1];

        // 从配置类中获取请求方法的 Key
        final String methodKey = authenticationConfig.getMethodKey();

        // 从配置类对象中获取请求所携带 Cookie 的 KEY
        final String cookieKey = authenticationConfig.getCookieKey();

        // 从配置类对象中获取请求超时时间
        final Integer timeout = authenticationConfig.getTimeout();

        //实例化 Jsoup 连接对象
        final Connection connection = Jsoup.connect(authenticationConfig.getLoginUrl())
            .data(methodKey, authenticationConfig.getMethodValue())
            .data(authenticationConfig.getStudentNumberKey(), authenticationForm.getStudentNumber())
            .data(authenticationConfig.getStudentPasswordKey(), authenticationForm.getStudentPassword())
            .data(authenticationConfig.getVerificationCodeKey(), webVerification[0])
            .cookie(cookieKey, webToken)
            .timeout(timeout);
        try {

            // 获取请求后的 Jsoup Document 对象, 此时可能会发生 IOException
            final Document document = connection.post();

            // 当标题长度不为 21 的时候登录东北石油大学教务管理系统成功
            if (authenticationConfig.getErrorTitleLength() != document.title().length()) {

                // 登录成功后需进行两次重定向
                Jsoup.connect(authenticationConfig.getFirstRedirectUrl())
                    .cookie(cookieKey, webToken)
                    .timeout(timeout)
                    .post();
                Jsoup.connect(authenticationConfig.getSecondRedirectUrl())
                    .data(methodKey, authenticationConfig.getSecondRedirectMethodValue())
                    .cookie(cookieKey, webToken)
                    .timeout(timeout)
                    .post();

                // 返回携带 Web Token 的业务逻辑结果对象
                return ServiceResult.success("获取 Web Token 成功！", webToken);
            } else {

                // 当标题长度为 21 的时候登录失败
                return ServiceResult.fail("学号或密码错误，获取 Web Token 失败！");
            }
        } catch (IOException e) {

            // 当发生 IOException 时抛出东北石油大学服务器错误异常
            throw new NepuServerErrorException("服务器错误，获取 Web Token 失败！");
        }
    }
}
