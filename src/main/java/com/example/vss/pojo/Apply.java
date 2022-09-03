package com.example.vss.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Apply {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String content;
    private Integer state;
    private String userId;
    private Integer volunteeringId;
}
