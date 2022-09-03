package com.example.vss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vss.pojo.Major;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MajorService extends IService<Major> {
    List<Major> selectByCollegeId(String collegeId);
}
