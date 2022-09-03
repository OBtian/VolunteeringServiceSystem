package com.example.vss.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vss.mapper.UserMapper;
import com.example.vss.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{
    @Autowired
    private UserMapper userMapper;
    @Override
    public void registerUser(Map<String, String> user) {
        userMapper.registerUser(user);
    }
    @Override
    public void setAvatar(String id,String avatar){
        userMapper.setAvatarById(id,avatar);
    }

    @Override
    public Map selectSchoolMsgById(String id) {
        return userMapper.selectSchoolMsgById(id);
    }
}
