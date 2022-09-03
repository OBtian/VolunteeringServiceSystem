package com.example.vss.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.awt.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@ApiModel("用户实体类")
@TableName("userinfo")
public class User {
    private String id;
    private String avatar;
    private String name;
//    @TableField("psw")
    private String psw;
    private Boolean isSociety;
    private Boolean isSupport;
    private Double totalDonTime;
    private Integer classId;
    private Integer collegeId;
    private Integer majorId;
}