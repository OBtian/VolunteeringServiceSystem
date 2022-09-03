package com.example.vss.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.vss.pojo.*;
import com.example.vss.pojo.Class;
import com.example.vss.service.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@CrossOrigin(origins = "*")
@Api(tags = "用户相关操作")
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private ClassServiceImpl classService;
    @Autowired
    private MarjorServiceImpl marjorService;
    @Autowired
    private CollegeServiceImpl collegeService;


    @PostMapping(value = "/login")
    public Map login(@RequestBody Map<String, String> request) {
        Map data = new HashMap<String, Object>();
        data.put("code", 0);
        data.put("msg", "success");
        User user = userService.getById(request.get("id"));
        if (user != null) {
            if (user.getPsw().equals(request.get("psw"))) {
                data.put("user", user);
            } else {
                data.put("code", -1);
                data.put("msg", "密码错误");
            }
        } else {
            data.put("code", -1);
            data.put("msg", "用户名错误");
        }
        return data;
    }

    @GetMapping(value = "/register")
    public Map getRegister(@RequestParam String college_id,
                        @RequestParam String major_id){
        Map data = new HashMap<String, Object>();
        data.put("code", 0);
        data.put("msg", "success");
        List<College> colleges = collegeService.list();
        List<Major> majors = marjorService.list();
        if (!"0".equals(college_id)){
            majors = marjorService.selectByCollegeId(college_id);
        }
        if (!"0".equals(major_id)){
            List<Class> classes = classService.selectAllByMajorId(major_id);
            data.put("class_list",classes);
        }
        data.put("major_list",majors);
        data.put("college_list",colleges);
        return data;
    }

    @PostMapping(value = "/register")
    public Map postRegister(@RequestBody Map<String, String> request){
        Map data = new HashMap<String, Object>();
        data.put("code", 0);
        data.put("msg", "success");
        System.out.println(request);
        User user = userService.getById(request.get("id"));
        if (user!=null){
            data.put("error_num", 1);
            data.put("msg", "用户已存在");
        }else{
            userService.registerUser(request);
        }
        return data;
    }

    @PostMapping("/add_avatar")
    public Map addAvatar(@RequestBody Map<String, String> request){
        Map data = new HashMap<String, Object>();
        data.put("code", 0);
        data.put("msg", "success");
        String id = request.get("id");
        String avatar = request.get("avatar");
        userService.setAvatar(id, avatar);

        return data;
    }
}
