package com.sam.springbootmall.controller;

import com.sam.springbootmall.dto.UserLoginRequest;
import com.sam.springbootmall.dto.UserRegisterRequest;
import com.sam.springbootmall.model.User;
import com.sam.springbootmall.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:63342", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService userService;
    // 註冊
    @PostMapping("/users/register")
    public ResponseEntity<User> register(@RequestBody @Valid UserRegisterRequest userRegisterRequest) {
        Integer userId = userService.register(userRegisterRequest);
        User user = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);

    }
    // 登入
    @PostMapping("/users/login")
    public ResponseEntity<User> login
            (@RequestBody @Valid UserLoginRequest userLoginRequest,
             HttpSession session) {
        // 從 Session 取出驗證碼
        String correctCode = (String) session.getAttribute("CAPTCHA_CODE");
        // 比對 (忽略大小寫)
        if (!correctCode.equalsIgnoreCase(userLoginRequest.getCaptcha())) {
            throw new RuntimeException("驗證碼錯誤");
        }
        // 清除Session 裡的驗證碼
        session.removeAttribute("CAPTCHA_CODE");
        User user = userService.login(userLoginRequest);

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
    // 搜尋user
    @GetMapping("/users/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable Integer userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }
}
