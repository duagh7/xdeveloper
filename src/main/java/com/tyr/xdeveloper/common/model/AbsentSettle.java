package com.tyr.xdeveloper.common.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@Accessors(chain = true)
public class AbsentSettle {

    private String absentStr;

    private Long id;

    private Date settleDay;

    private Date createTime;

    private Date updateTime;

}
