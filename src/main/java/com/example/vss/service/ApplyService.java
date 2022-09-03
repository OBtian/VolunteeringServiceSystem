package com.example.vss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vss.pojo.Apply;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface ApplyService extends IService<Apply> {
    List<Map> applyBackend(String volId);
}
