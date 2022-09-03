package com.example.vss.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vss.mapper.MajorMapper;
import com.example.vss.pojo.Major;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarjorServiceImpl extends ServiceImpl<MajorMapper, Major> implements MajorService {
    @Autowired
    private MajorMapper majorMapper;
    @Override
    public List<Major> selectByCollegeId(String collegeId) {
        return majorMapper.selectByCollegeId(collegeId);
    }
}
