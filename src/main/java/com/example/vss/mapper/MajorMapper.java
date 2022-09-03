package com.example.vss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.vss.pojo.Major;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface MajorMapper extends BaseMapper<Major> {
    @Select("select id,name from major where college_id = #{collegeId}")
    List<Major> selectByCollegeId(String collegeId);
}
