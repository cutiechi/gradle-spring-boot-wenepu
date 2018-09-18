package com.cutiechi.wenepu.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码图片工具类
 *
 * @author Cutie Chi
 */
@Component
public final class VerificationCodeImageUtils {

    /**
     * 验证码字符数组
     */
    private static char[] verificationCodeChars = {
        '1',
        '2',
        '3',
        'b',
        'c',
        'm',
        'n',
        'v',
        'x',
        'z'
    };

    /**
     * 识别验证码所依赖的 Map , 将在静态代码块中加载, Value 为验证码字符, Key 为该字符对应的 Buffered Image 对象
     */
    private static Map<BufferedImage, Character> dependencyMap;

    static {

        //  实例化依赖 Map
        dependencyMap = new HashMap<>();

        // 验证码字符 png 图片放在 resources/verification-code-images/ 中
        String prefix = "verification-code-images/";
        String suffix = ".png";

        try {

            // 遍历验证码字符数组为依赖的 Map 赋值
            for (char verificationCodeChar : verificationCodeChars) {

                // 加载验证码图片到 Buffered Image 对象
                Resource resource = new ClassPathResource(prefix + verificationCodeChar + suffix);
                BufferedImage image = ImageIO.read(resource.getFile());

                // 添加到依赖 Map 中
                dependencyMap.put(image, verificationCodeChar);
            }
        } catch (IOException exception) {

            // 加载失败抛出 Runtime Exception, 失败的原因有: 识别验证码所依赖的 png 图片位置或数量不正确
            throw new RuntimeException("load verification code dependency image fail!");
        }
    }
}
