package com.sam.springbootmall.service.impl;

import com.sam.springbootmall.dao.UserDao;
import com.sam.springbootmall.dto.UserLoginRequest;
import com.sam.springbootmall.dto.UserRegisterRequest;
import com.sam.springbootmall.model.User;
import com.sam.springbootmall.service.EmailService;
import com.sam.springbootmall.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.server.ResponseStatusException;

@Transactional
@Service
public class UserServiceImpl implements UserService {

    private final static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserDao userDao;
    @Autowired
    private EmailService emailService;

    //註冊
    @Override
    public Integer register(UserRegisterRequest userRegisterRequest) {
        String email = userRegisterRequest.getEmail();
        User user = userDao.getUserByEmail(email);
        if (user != null) {
            log.warn("該email {} 已經被註冊", userRegisterRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "該email已被註冊");
        }
        String hashPassword = DigestUtils.md5DigestAsHex(userRegisterRequest.getPassword().getBytes());
        userRegisterRequest.setPassword(hashPassword);
        //發送註冊信
        emailService.sendWelcomeEmail(email);

        return userDao.createUser(userRegisterRequest);
    }
    //登入
    @Override
    public User login(UserLoginRequest userLoginRequest) {

        User user = userDao.getUserByEmail(userLoginRequest.getEmail());
        if (user == null) {
            log.warn("該email{}未註冊", userLoginRequest.getEmail());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "該email尚未註冊");
        }
        String hashPassword = DigestUtils.md5DigestAsHex(userLoginRequest.getPassword().getBytes());
        if (user.getPassword().equals(hashPassword)) {
            return user;
        } else{
            log.warn("密碼錯誤");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "密碼錯誤");
        }
    }

    @Override
    public User getUserById(Integer id) {
        return userDao.getUserById(id);
    }


}
