package com.example.vss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.vss.pojo.Class;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ClassMapper extends BaseMapper<Class> {
    @Select("select id,name from classs where major_id = #{majorId}")
    List<Class> selectAllByMajorId(String majorId);
}
