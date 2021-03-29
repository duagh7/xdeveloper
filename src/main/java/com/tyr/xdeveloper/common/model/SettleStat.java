package com.tyr.xdeveloper.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class SettleStat implements Serializable {

    private Long userId;

    private Integer commitDayNum;

}
