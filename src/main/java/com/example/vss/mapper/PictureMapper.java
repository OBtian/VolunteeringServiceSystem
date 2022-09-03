package com.example.vss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.vss.pojo.Picture;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureMapper extends BaseMapper<Picture> {
    @Select("select content from picture where volunteering_id = #{id} limit 1")
    String selectContentByVolIdOnlyOne(Integer id);
}
