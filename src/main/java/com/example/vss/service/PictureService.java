package com.example.vss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vss.pojo.Picture;
import org.springframework.stereotype.Service;

@Service
public interface PictureService extends IService<Picture>{
    public String selectContentByVolIdOnlyOne(Integer id);
}
