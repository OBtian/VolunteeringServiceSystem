package com.example.vss.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.vss.pojo.Picture;
import com.example.vss.pojo.Volunteering;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface VolunteeringService extends IService<Volunteering> {
//    List<Map<String, Object>> selectAllById();
    List<Volunteering> selectByMounth(String mounthBegin,String mountEnd);
    String selectCreater(String id);

    Boolean selectIsApply(String volId,String userId);

    List<Map> volTop3();

    List<Map> collegeTop5();

    List<Map> donateOrderByUser();

    Volunteering isPost(String userId);

    List<String> selectPicturesByVolId(String volId);

    List<Map> selectCommentsByVolId(String volId);
}
