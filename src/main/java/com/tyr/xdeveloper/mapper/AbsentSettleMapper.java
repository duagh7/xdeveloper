package com.tyr.xdeveloper.mapper;

import com.tyr.xdeveloper.common.model.AbsentSettle;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface AbsentSettleMapper {

    List<AbsentSettle> getByTime(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    int insertAbsentSettle(AbsentSettle absentSettle);
}
