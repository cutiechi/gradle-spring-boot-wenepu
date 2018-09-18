package com.cutiechi.wenepu.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;

import java.awt.Color;
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

    // 单元图片的宽度
    private Integer UNIT_WIDTH = 9;

    // 单元图片的高度
    private Integer UNIT_HEIGHT = 12;

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

    /**
     * 根据像素颜色的亮度判断是否为目标像素
     *
     * @param colorRgb 像素颜色的 RGB 混合值
     * @return 是否为目标像素
     */
    private Boolean isTarget (Integer colorRgb) {

        // 根据像素颜色的 RGB 混合值实例化 Color 对象
        Color color = new Color(colorRgb);

        // 实例化长度为 3 的 float 数组, 用来存放 RGB 转为 HSB 后的色相, 饱和度, 亮度
        float[] hsb = new float[3];

        // 通过 RGBtoHSB 方法将像素的 HSB 存在 hsb 数组中
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);

        // 根据像素颜色的亮度判断是否为目标像素
        return hsb[2] < 0.5f;
    }

    /**
     * 降噪, 将有用的像素设为黑色, 无用的像素设为白色
     *
     * @param image 验证码图片
     */
    private void doNoise (BufferedImage image) {

        // 验证码图片的宽度
        int width = image.getWidth();

        // 验证码图片的高度
        int height = image.getHeight();

        // 根据 isTarget 方法将有用像素的 RGB 值设为黑色, 无用的设为白色
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, isTarget(image.getRGB(x, y)) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }
    }

    /**
     * 分割验证码图片
     *
     * @param image 验证码图片
     * @return 分割后的单元图片数组
     */
    private BufferedImage[] split (BufferedImage image) {

        // 原点横坐标
        int baseX = 3;

        // 原点纵坐标
        int baseY = 4;

        // 横坐标差值
        int differenceX = 10;

        // 实例化长度为 4 的 Buffered Image 数组, 该数组用来存放分割后的四个单元 Buffered Image
        BufferedImage[] unitImages = new BufferedImage[4];

        // 将分割后的图片存放在 unitImages 数组中
        unitImages[0] = image.getSubimage(baseX, baseY, UNIT_WIDTH, UNIT_HEIGHT);
        unitImages[1] = image.getSubimage(baseX + differenceX, baseY, UNIT_WIDTH, UNIT_HEIGHT);
        unitImages[2] = image.getSubimage(baseX + 2 * differenceX, baseY, UNIT_WIDTH, UNIT_HEIGHT);
        unitImages[3] = image.getSubimage(baseX + 3 * differenceX, baseY, UNIT_WIDTH, UNIT_HEIGHT);
        return unitImages;
    }

    /**
     * 单元识别
     *
     * @param unitImage 单元图片
     * @return 识别结果
     */
    private Character unitRecognition (BufferedImage unitImage) {

        // 定义识别结果字符
        char result = ' ';

        // 初始最小差异像素为全部像素
        int minDifferencePixel = UNIT_WIDTH * UNIT_HEIGHT;

        // 遍历依赖 Map 中得 Key 即 Buffered Image
        for (BufferedImage referenceImage : dependencyMap.keySet()) {

            // 初始差异像素为 0
            int differencePixel = 0;

            // 逐像素比较
            for (int x = 0; x < UNIT_WIDTH; x++) {
                for (int y = 0; y < UNIT_HEIGHT; y++) {
                    differencePixel += unitImage.getRGB(x, y) != referenceImage.getRGB(x, y) ? 1 : 0;

                    // 当差异像素大于等于最小差异像素结束内层循环
                    if (differencePixel >= minDifferencePixel) {
                        break;
                    }
                }
            }

            // 差异像素比最小差异像素小时, 将差异像素的值给最小差异像素, 识别结果字符为依赖 Map 的 Value
            if (differencePixel < minDifferencePixel) {
                minDifferencePixel = differencePixel;
                result = dependencyMap.get(referenceImage);
            }
        }
        return result;
    }
}
