package com.example.vss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.vss.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.Map;
@Repository
public interface UserMapper extends BaseMapper<User> {
    @Insert("insert into userinfo (id,name,psw,is_society,class_id,college_id,major_id) values (#{id},#{name},#{psw},#{is_society},#{classs_id},#{college_id},#{major_id})")
    void registerUser(Map<String,String> user);
    @Update("UPDATE userinfo set avatar=#{avatar} where id=#{id}")
    void setAvatarById(String id,String avatar);

    @Select("SELECT co.`name` as college,cl.`name` as classs,m.`name` as major from userinfo as u,college as co,classs as cl,major as m WHERE u.class_id = cl.id and u.college_id = co.id and u.major_id = m.id and u.id = #{id}")
    Map selectSchoolMsgById(String id);
}
