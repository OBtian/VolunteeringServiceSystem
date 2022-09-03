package com.example.vss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.vss.pojo.Apply;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface ApplyMapper extends BaseMapper<Apply> {
    @Select("SELECT a.id as aid,u.name as uname,u.id as uid,c.name as cname,a.content,a.state from apply as a,userinfo as u,college as c WHERE a.user_id = u.id AND u.college_id = c.id AND a.volunteering_id = #{volId}")
    List<Map> applyBackend(String volId);
}
