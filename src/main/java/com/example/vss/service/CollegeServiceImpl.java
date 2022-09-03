package com.example.vss.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vss.mapper.CollegeMapper;
import com.example.vss.pojo.College;
import org.springframework.stereotype.Service;

@Service
public class CollegeServiceImpl extends ServiceImpl<CollegeMapper, College> implements CollegeService {
}
