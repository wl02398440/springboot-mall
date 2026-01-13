
package com.sam.springbootmall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 取得目前專案的根目錄
        String projectPath = System.getProperty("user.dir");

        // 指定圖片存放的外部路徑
        // 讓 http://localhost:8080/images/xxx.jpg 對應到 專案資料夾/uploads/xxx.jpg
        String uploadPath = "file:" + projectPath + "/uploads/";

        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);

        System.out.println("圖片對應路徑: " + uploadPath);
    }
}
