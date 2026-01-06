
package com.sam.springbootmall.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class CaptchaUtil {

    // 產生 4 位數隨機驗證碼
    public static String generateCode() {
        String str = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int index = random.nextInt(str.length());
            sb.append(str.charAt(index));
        }
        return sb.toString();
    }

    // 根據驗證碼產生圖片
    public static BufferedImage generateImage(String code) {
        int width = 100;
        int height = 40;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();

        // 設定背景色
        g.setColor(new Color(240, 240, 240));
        g.fillRect(0, 0, width, height);

        // 畫一些干擾線 (防止機器人辨識)
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            int x1 = random.nextInt(width);
            int y1 = random.nextInt(height);
            int x2 = random.nextInt(width);
            int y2 = random.nextInt(height);
            g.drawLine(x1, y1, x2, y2);
        }

        // 畫上驗證碼
        g.setFont(new Font("Arial", Font.BOLD, 24));
        for (int i = 0; i < code.length(); i++) {
            g.setColor(new Color(random.nextInt(150), random.nextInt(150), random.nextInt(150)));
            g.drawString(String.valueOf(code.charAt(i)), 20 * i + 10, 30);
        }

        g.dispose();
        return image;
    }
}
