package com.example.vss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vss.pojo.Class;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ClassService extends IService<Class> {
    List<Class> selectAllByMajorId(String majorId);
}
