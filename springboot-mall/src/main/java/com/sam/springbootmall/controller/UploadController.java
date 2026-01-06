package com.sam.springbootmall.controller;

import com.sam.springbootmall.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@CrossOrigin(origins = "*")
public class UploadController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = imageService.saveImage(file);
            return imageUrl;
        } catch (IOException e) {
            e.printStackTrace();
            return "Upload Failed";
        }
    }
}
