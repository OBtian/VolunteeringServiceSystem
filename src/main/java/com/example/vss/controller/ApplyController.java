package com.example.vss.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.vss.pojo.Apply;
import com.example.vss.pojo.User2volunteering;
import com.example.vss.pojo.Volunteering;
import com.example.vss.service.ApplyServiceImpl;
import com.example.vss.service.User2volunteeringServiceImpl;
import com.example.vss.service.VolunteeringServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplyController {
    @Autowired
    private ApplyServiceImpl applyService;

    @Autowired
    private VolunteeringServiceImpl volunteeringService;

    @Autowired
    private User2volunteeringServiceImpl user2volunteeringService;

    public void updateApplyplState(List<Apply> applies, Volunteering vol){
        //每次查询前更新状态
        Date date = new Date(System.currentTimeMillis());
        for (Apply apply : applies) {
            if (date.after(vol.getSuEndTime())){
                apply.setState(2);
                applyService.updateById(apply);
            }
        }
    }

    @PostMapping(value = "/add_apply")
    public void addApply(@RequestBody Apply apply){
        applyService.save(apply);
    }

    @GetMapping(value = "/poster_backend")
    public Map getApplies(@RequestParam("id") String userId){
        Map data = new HashMap<String, Object>();
        data.put("code", 0);
        data.put("msg", "success");

        Volunteering vol = volunteeringService.isPost(userId);
        List<Apply> applyList = applyService.list(new QueryWrapper<Apply>().eq("volunteering_id", vol.getId()));
        updateApplyplState(applyList,vol);

        List<Map> applies = applyService.applyBackend(String.valueOf(vol.getId()));
        String createrName = volunteeringService.selectCreater(String.valueOf(vol.getId()));


        data.put("apply_list",applies);
        data.put("vol",vol);
        data.put("createrName",createrName);

        return data;
    }

    @PostMapping(value = "/poster_backend")
    public void setApplies(@RequestBody Apply apply){
        applyService.updateById(apply);
        if (apply.getState() == 1){
            Apply a = applyService.getById(apply.getId());
            User2volunteering user2Volunteering = new User2volunteering();
            user2Volunteering.setUserId(a.getUserId());
            user2Volunteering.setVolunteeringId(a.getVolunteeringId());
            System.out.println(user2Volunteering);
            user2volunteeringService.save(user2Volunteering);
        }
    }
}
