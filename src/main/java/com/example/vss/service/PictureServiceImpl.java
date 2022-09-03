package com.example.vss.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vss.mapper.PictureMapper;
import com.example.vss.pojo.Picture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PictureServiceImpl extends ServiceImpl<PictureMapper, Picture> implements PictureService {
    @Autowired
    private PictureMapper pictureMapper;

    @Override
    public String selectContentByVolIdOnlyOne(Integer id) {
        return pictureMapper.selectContentByVolIdOnlyOne(id);
    }
}
