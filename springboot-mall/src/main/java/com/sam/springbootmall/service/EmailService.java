
package com.sam.springbootmall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    @Qualifier("backEndMailSender")
    private JavaMailSender mailSender;

    // 發送註冊信
    public void sendWelcomeEmail(String email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("商場 <wl0239844@gmail.com>");
            message.setTo(email);
            message.setSubject("歡迎加入 SIMPLESHOP");

            // 信件內容
            String content = "親愛的 " + email + " 您好：\n\n" +
                    "恭喜您註冊成功！歡迎加入 SIMPLESHOP 極簡生活。\n" +
                    "我們已經收到您的註冊資訊，現在您可以開始選購商品了。\n\n" +
                    "祝您購物愉快！";

            message.setText(content);

            mailSender.send(message);
            System.out.println("註冊信已寄出給：" + email);

        } catch (Exception e) {
            System.err.println("寄信失敗：" + e.getMessage());
            e.printStackTrace();
        }
    }
}
