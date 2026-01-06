package com.sam.springbootmall.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class ImageService {
    // 設定存檔資料夾：專案根目錄/uploads/
    private final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads/";

    public String saveImage(MultipartFile file) throws IOException {
        // 檢查建立資料夾
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();
        String ext = originalFilename.substring
                (originalFilename.lastIndexOf("."));
        String str = originalFilename.substring
                (0, originalFilename.lastIndexOf("."));
        // UUID產生通用唯一識別碼(避免重複檔名)
        String newFileName =
                str + "-" + UUID.randomUUID().toString() + ext;
        // 寫入檔案
        Path path = Paths.get(UPLOAD_DIR + newFileName);
        Files.write(path, file.getBytes());

        return "http://localhost:8080/images/" + newFileName;
    }
}
