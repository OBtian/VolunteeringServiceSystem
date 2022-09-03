package com.example.vss.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User2volunteering {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Boolean state;
    private String userId;
    private Integer volunteeringId;
}
