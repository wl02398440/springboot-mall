
package com.sam.springbootmall.controller;

import com.sam.springbootmall.util.CaptchaUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@RestController
@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
public class CaptchaController {

    @GetMapping("/captcha")
    public void getCaptcha(HttpServletResponse response, HttpSession session) throws IOException {
        // 產生驗證碼
        String code = CaptchaUtil.generateCode();

        // 把驗證碼存入 Session，之後登入時比對
        session.setAttribute("CAPTCHA_CODE", code);
        System.out.println("生成驗證碼: " + code);

        // 產生圖片
        BufferedImage image = CaptchaUtil.generateImage(code);

        // 設定回應格式為圖片
        response.setContentType("image/jpeg");

        // 輸出圖片
        ImageIO.write(image, "jpg", response.getOutputStream());
    }
}
