package com.example.vss.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vss.mapper.ApplyMapper;
import com.example.vss.pojo.Apply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApplyServiceImpl extends ServiceImpl<ApplyMapper,Apply> implements ApplyService {
    @Autowired
    private ApplyMapper applyMapper;
    @Override
    public List<Map> applyBackend(String volId) {
        return applyMapper.applyBackend(volId);
    }
}
