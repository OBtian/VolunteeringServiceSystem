package com.example.vss.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.vss.pojo.Picture;
import com.example.vss.pojo.Volunteering;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface VolunteeringMapper extends BaseMapper<Volunteering> {
//    @Select("select id,name,time,phone,su_start_time,su_end_time,start_time,end_time,address,content from volunteering where creater_id != '18996343508'")
//    List<Map<String,Object>> selectAllById();

    @Select("SELECT name from userinfo where id = (select creater_id from volunteering where id = #{id})")
    String selectCreater(String id);
    @Select("select id from apply where volunteering_id = #{volId} and user_id = #{userId};")
    Integer selectIsApply(String volId,String userId);

    @Select("SELECT v.name,COUNT(a.user_id) AS count from volunteering as v,apply as a where a.volunteering_id = v.id GROUP BY a.volunteering_id ORDER BY count DESC LIMIT 3")
    List<Map> volTop3InThisMonth(String mounthBegin,String mountEnd);

    @Select("SELECT c.name,COUNT(c.id) as count from apply as a,userinfo as u,college as c WHERE a.user_id = u.id and u.college_id = c.id GROUP BY c.id ORDER BY count DESC limit 5")
    List<Map> collegeTop5InThisMonth(String mounthBegin,String mountEnd);

    @Select("SELECT v.name,COUNT(a.user_id) AS count from volunteering as v,apply as a where a.volunteering_id = v.id GROUP BY a.volunteering_id ORDER BY count DESC LIMIT 3")
    List<Map> volTop3();

    @Select("SELECT c.name,COUNT(c.id) as count from apply as a,userinfo as u,college as c WHERE a.user_id = u.id and u.college_id = c.id GROUP BY c.id ORDER BY count DESC limit 5")
    List<Map> collegeTop5();

    @Select("SELECT name,total_don_time from userinfo ORDER BY total_don_time DESC LIMIT 3")
    List<Map> donateOrderByUser();

    @Select("SELECT p.content from volunteering as v,picture as p WHERE v.id = p.volunteering_id and v.id = #{volId}")
    List<String> selectPicturesByVolId(String volId);

    @Select("SELECT c.content,u.name from comments as c,userinfo as u,volunteering as v WHERE c.volunteering_id = v.id and c.user_id = u.id and v.id = #{volId}")
    List<Map> selectCommentsByVolId(String volId);
}
