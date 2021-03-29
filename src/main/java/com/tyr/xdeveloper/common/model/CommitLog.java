package com.tyr.xdeveloper.common.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CommitLog implements Serializable {

    //  `id` int(11) NOT NULL,
    //  `settle_day` date DEFAULT NULL COMMENT '打卡日期',
    //  `user_id` int(11) DEFAULT NULL,
    private Long id;

    private Date settleDay;

    private Long userId;

}
