package com.example.vss.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vss.mapper.ClassMapper;
import com.example.vss.pojo.Class;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements ClassService {
    @Autowired
    private ClassMapper classMapper;
    @Override
    public List<Class> selectAllByMajorId(String majorId) {
        return classMapper.selectAllByMajorId(majorId);
    }
}
