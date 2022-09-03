package com.example.vss.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Volunteering {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Integer state;
    private Double time;
    private String name;
    private Date suStartTime;
    private Date suEndTime;
    private Date startTime;
    private Date endTime;
    private String address;
    private String content;
    private String phone;
    private Date realEndTime;
    private Integer tag1;
    private Integer tag2;
    private Integer tag3;
    private String createrId;
}
