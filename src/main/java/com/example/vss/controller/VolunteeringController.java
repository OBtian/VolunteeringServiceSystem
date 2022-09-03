package com.example.vss.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.vss.pojo.*;
import com.example.vss.service.*;
import com.example.vss.utils.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.sql.Date;

import java.util.*;
@RestController
@CrossOrigin(origins = "*")
@Api(tags = "志愿相关操作")
public class VolunteeringController {

    @Autowired
    private VolunteeringServiceImpl volunteeringService;
    @Autowired
    private ApplyServiceImpl applyService;
    @Autowired
    private PictureServiceImpl pictrueService;

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private User2volunteeringServiceImpl user2volunteeringService;
    public void updateVolState(List<Volunteering> volunteerings){
        //每次查询前更新状态
        Date date = new Date(System.currentTimeMillis());
        for (Volunteering vol: volunteerings) {
            //flag判断是否要修改减少数据库更新次数
            boolean flag = false;
            if (vol.getState() != 2){
                if(date.before(vol.getSuStartTime())){
                    //如果当前时间先于活动时间那此时状态应为0未开始
                    if (vol.getState() != 0){
                        flag = true;
                        vol.setState(0);
                    }
                } else if (date.before(vol.getSuEndTime())) {
                    //如果当前时间后于活动时间且先于结束时间那此时状态应为1进行中
                    if (vol.getState() != 1){
                        flag = true;
                        vol.setState(1);
                    }
                }else{
                    if (vol.getState() != 2){
                        flag = true;
                        vol.setState(2);
                    }
                }
                if (flag){
                    //待优化为只更新状态的sql
                    volunteeringService.updateById(vol);
                }
            }
        }
    }
    public void updateApplyplState(List<Apply> applies, Volunteering vol){
        //每次查询前更新状态
        java.util.Date date = new java.util.Date(System.currentTimeMillis());
        for (Apply apply : applies) {
            if (date.after(vol.getSuEndTime())){
                apply.setState(2);
                applyService.updateById(apply);
            }
        }
    }

    @GetMapping(value = "/select_vol")
    public Map selectVol(@RequestParam String keyword,
                         @RequestParam Integer state){
        Map data = new HashMap<String, Object>();
        data.put("code", 0);
        data.put("msg", "success");
        List<Volunteering> volunteerings = null;

        if (state != 3){
            volunteerings = volunteeringService.list(
                    new QueryWrapper<Volunteering>().ne("id","1").ne("id","17")
                                                    .eq("state",state)
                                                    .like("name",keyword));
        }else{
            volunteerings = volunteeringService.list(
                    new QueryWrapper<Volunteering>().ne("id","1").ne("id","17")
                                                    .like("name",keyword));
        }

        //每次查询前更新状态
        updateVolState(volunteerings);

        data.put("vol_list",volunteerings);
        return data;
    }

    @PostMapping(value = "/select_vol")
    public Map selectVol(@RequestBody Map<String, String> request){
        Map data = new HashMap<String, Object>();
        data.put("code", 0);
        data.put("msg", "success");

        String volId = request.get("vol_id");
        String userId = request.get("user_id");

        data.put("createrName",volunteeringService.selectCreater(volId));
        data.put("isApply",volunteeringService.selectIsApply(volId,userId));


        return data;
    }

    @GetMapping(value = "/show_comment")
    public Map showComment(@RequestParam Integer month_num){
        Map data = new HashMap<String, Object>();
        data.put("code", 0);
        data.put("msg", "success");

        Map<String,String> monthTime = getMonthStartAndEndUtil.getMonthTime(month_num);

        Map<String,Object> volunteersAndPictures =new HashMap();
        List<Volunteering> volunteerings = volunteeringService.selectByMounth(monthTime.get("startDate"), monthTime.get("endDate"));
        for (Volunteering vol: volunteerings) {
            Map<String,Object> volunteerAndPicture =new HashMap();
            volunteerAndPicture.put("vol",vol);
//            volunteerAndPicture.put("picture",pictrueService.getOne(new QueryWrapper<Picture>().eq("volunteering_id",vol.getId())).getContent());
            volunteerAndPicture.put("picture",pictrueService.selectContentByVolIdOnlyOne(vol.getId()));
            volunteersAndPictures.put(String.valueOf(vol.getId()),volunteerAndPicture);
        }
        data.put("vol_list",volunteersAndPictures);

        return data;
    }

    @PostMapping(value = "/show_comment")
    public Map volunteerDetail(@RequestBody Map<String, String> request){
        Map data = new HashMap<String, Object>();
        data.put("code", 0);
        data.put("msg", "success");

        String volId = request.get("id");
        List<String> pictures = volunteeringService.selectPicturesByVolId(volId);
        Volunteering vol = volunteeringService.getById(volId);

        data.put("pictures",pictures);
        data.put("name",vol.getName());
        data.put("tag1",vol.getTag1());
        data.put("tag2",vol.getTag2());
        data.put("tag3",vol.getTag3());

        data.put("comment_list",volunteeringService.selectCommentsByVolId(volId));

        return data;
    }
    @Autowired
    private UserServiceImpl userService;

    @GetMapping(value = "/data")
    public Map showData(){
        Map data = new HashMap<String, Object>();
        data.put("code", 0);
        data.put("msg", "success");
//        Map<String,String> monthTime = getMonthStartAndEndUtil.getMonthTime(0);
//        String startDate = monthTime.get("startDate");
//        String endDate = monthTime.get("endDate");
        //热门志愿活动top3
        List<Map> volTop3 = volunteeringService.volTop3();
        data.put("volTop3",volTop3);

        //各学院参加志愿活动情况 TOP5
        List<Map> collegeTop5 = volunteeringService.collegeTop5();
        data.put("top5",collegeTop5);


        Map statistics = new HashMap<String, Long>();
        List<Volunteering> volunteerings = volunteeringService.list(new QueryWrapper<Volunteering>().ne("id","1").ne("id","17"));
        updateVolState(volunteerings);
        long ing = volunteeringService.count(new QueryWrapper<Volunteering>().ne("id","1").ne("id","17").eq("state", 1));
        long ed = volunteeringService.count(new QueryWrapper<Volunteering>().ne("id","1").ne("id","17").eq("state", 2));
        long yet = volunteeringService.count(new QueryWrapper<Volunteering>().ne("id","1").ne("id","17").eq("state", 0));
        statistics.put("ing",ing);
        statistics.put("ed",ed);
        statistics.put("yet",yet);
        long stu = userService.count(new QueryWrapper<User>().eq("is_society", 0));
        long soc = userService.count(new QueryWrapper<User>().eq("is_society", 1));
        statistics.put("stu",stu);
        statistics.put("soc",soc);

        data.put("statistics",statistics);

        Long currentTime = System.currentTimeMillis();

        String timeZone = "GMT+8:00";
        int setYear, setMonth, currentYear, currentMonth;
        //年度数据
        Calendar calendar = Calendar.getInstance();// 获取当前日期
        int month = calendar.get(Calendar.MONTH);
        Map<Integer,Long> annual = new HashMap();
        for (int i = 0; i < month; i++) {
            Map<String,String> monthTime = getMonthStartAndEndUtil.getMonthTime(i);
            String startDate = monthTime.get("startDate");
            String endDate = monthTime.get("endDate");
            long count = volunteeringService.count(new QueryWrapper<Volunteering>().lt("real_end_time", endDate).gt("real_end_time", startDate));
            System.out.println(count);
            annual.put((month - i),count);
        }
        data.put("annual",annual);

        //个人志愿时长统计
        data.put("userTop3",volunteeringService.donateOrderByUser());

        return data;
    }

    @GetMapping(value = "/post_vol")
    public Map isPost(@RequestParam String id) {
        Map data = new HashMap<String, Object>();
        data.put("code", 0);
        data.put("msg", "success");
        User user = userService.getById(id);
        Volunteering post = volunteeringService.isPost(id);
        if (post!=null){
            data.put("isPost",true);
        }else {
            data.put("isPost",false);
        }
        data.put("vol",post);
        data.put("isSupport",user.getIsSupport());


        return data;
    }

    @PostMapping(value = "/post_vol")
    public void postVol(@RequestBody Volunteering volunteering){
        Date date = new Date(System.currentTimeMillis());
        if (date.before(volunteering.getSuStartTime())){
            volunteering.setState(0);
        } else if (date.after(volunteering.getSuEndTime())) {
            volunteering.setState(2);
        }else {
            volunteering.setState(1);
        }
        volunteeringService.save(volunteering);
    }

    @PostMapping(value = "/post_pic")
    public void endVolunteering(@RequestBody Map request){
        String volId = String.valueOf(request.get("id"));
        List<String> users = (List<String>) request.get("id_name");
        List<String> imgs = (List<String>) request.get("imgs");
        Volunteering vol = volunteeringService.getById(volId);
        Double time = vol.getTime();
        for (String u : users) {
            String uid = u.split(" ")[0];
            User user = userService.getById(uid);
            user.setTotalDonTime(user.getTotalDonTime()+time);
            userService.updateById(user);
            User2volunteering u2v = user2volunteeringService.getOne(new QueryWrapper<User2volunteering>().eq("user_id", uid).eq("volunteering_id", volId));
            u2v.setState(true);
            user2volunteeringService.updateById(u2v);
        }
        for (String content : imgs) {
            Picture picture = new Picture();
            picture.setVolunteeringId(Integer.valueOf(volId));
            picture.setContent(content);

            pictrueService.save(picture);
        }
        vol.setState(2);
        vol.setRealEndTime(new Date(System.currentTimeMillis()));
        volunteeringService.updateById(vol);
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

    @GetMapping(value = "/user_site")
    public Map userSite(@RequestParam String id){
        Map data = new HashMap<String, Object>();
        data.put("code", 0);
        data.put("msg", "success");


        Map userinfo = new HashMap();
        User user = userService.getById(id);
        userinfo.put("avatar",user.getAvatar());
        userinfo.put("total_don_time",user.getTotalDonTime());
        userinfo.put("is_support",user.getIsSupport());
        userinfo.put("is_society",user.getIsSociety());
        if (!user.getIsSociety()){
            userinfo.putAll(userService.selectSchoolMsgById(id));
        }
        data.put("userinfo",userinfo);

        List<Map> applyList = new ArrayList<Map>();
        List<Apply> applies = applyService.list(new QueryWrapper<Apply>().eq("user_id", id));
        for (Apply apply : applies) {
            Map temp = new HashMap();
            Volunteering vol = volunteeringService.getOne(new QueryWrapper<Volunteering>().eq("id", apply.getVolunteeringId()));
            Comments comment = commentService.getOne(new QueryWrapper<Comments>().eq("user_id", id).eq("volunteering_id", apply.getVolunteeringId()));
            String creater = volunteeringService.selectCreater(String.valueOf(apply.getVolunteeringId()));
            Boolean isComment = false;
            System.out.println("评论" + comment);
            if (comment != null){
                isComment = true;
            }
            temp.put("vol",vol);
            temp.put("apl_state",apply.getState());
            temp.put("creater_name",creater);
            temp.put("is_comment",isComment);
            applyList.add(temp);
        }
        data.put("apply_list",applyList);

        List<Map> postList = new ArrayList<Map>();
        List<Volunteering> volunteerings = volunteeringService.list(new QueryWrapper<Volunteering>().eq("creater_id", id));
        data.put("post_list",volunteerings);

        return data;
    }

    @PostMapping(value = "/comment")
    public void comment(@RequestBody Comments comment){
        commentService.save(comment);
        Volunteering vol = volunteeringService.getById(comment.getVolunteeringId());
        switch (comment.getTag()){
            case 1:
                vol.setTag1(vol.getTag1()+1);
                break;
            case 2:
                vol.setTag2(vol.getTag2()+1);
                break;
            case 3:
                vol.setTag3(vol.getTag3()+1);
                break;
        }
        volunteeringService.updateById(vol);
    }
}
