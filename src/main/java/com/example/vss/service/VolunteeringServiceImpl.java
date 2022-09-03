package com.example.vss.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.vss.mapper.VolunteeringMapper;
import com.example.vss.pojo.Picture;
import com.example.vss.pojo.Volunteering;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class VolunteeringServiceImpl extends ServiceImpl<VolunteeringMapper, Volunteering> implements VolunteeringService {
    @Autowired
    private  VolunteeringMapper volunteeringMapper;

    @Override
    public List<Volunteering> selectByMounth(String mounthBegin,String mountEnd) {
        return volunteeringMapper.selectList(new QueryWrapper<Volunteering>().eq("state",2).lt("real_end_time",mountEnd).gt("real_end_time",mounthBegin));
    }

    @Override
    public String selectCreater(String id) {
        return volunteeringMapper.selectCreater(id);
    }

    @Override
    public Boolean selectIsApply(String volId, String userId) {
        Integer integer = volunteeringMapper.selectIsApply(volId, userId);
        Volunteering volunteering = volunteeringMapper.selectById(volId);
        if (integer!=null || volunteering.getState() == 2 || volunteering.getCreaterId().equals(userId)){
            return true;
        }
        return false;
    }

    @Override
    public List<Map> volTop3() {
        return volunteeringMapper.volTop3();
    }



    @Override
    public List<Map> collegeTop5() {
        return volunteeringMapper.collegeTop5();
    }

    @Override
    public List<Map> donateOrderByUser() {
        return volunteeringMapper.donateOrderByUser();
    }

    @Override
    public Volunteering isPost(String userId) {
        return volunteeringMapper.selectOne(new QueryWrapper<Volunteering>().ne("state",2).eq("creater_id", userId));

    }

    @Override
    public List<String> selectPicturesByVolId(String volId) {
        return volunteeringMapper.selectPicturesByVolId(volId);
    }

    @Override
    public List<Map> selectCommentsByVolId(String volId) {
        return volunteeringMapper.selectCommentsByVolId(volId);
    }
}
