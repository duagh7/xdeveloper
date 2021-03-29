package com.tyr.xdeveloper.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class User {

    private Long id;

    private String wxAccount;

    private String nickName;

    private Integer commitDayNum;

    private Integer absentDayNum;

    private Integer holidayNum;

    private Date createTime;

    private Date updateTime;

    private Integer status;
}
