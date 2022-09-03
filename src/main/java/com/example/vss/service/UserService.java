package com.example.vss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vss.pojo.User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public interface UserService extends IService<User> {
    void registerUser(Map<String,String> user);

    void setAvatar(String id,String avatar);

    Map selectSchoolMsgById(String id);
}
