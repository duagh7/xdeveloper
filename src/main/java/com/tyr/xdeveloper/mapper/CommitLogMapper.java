package com.tyr.xdeveloper.mapper;

import com.tyr.xdeveloper.common.model.CommitLog;
import com.tyr.xdeveloper.common.model.SettleStat;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommitLogMapper {

    /**
     *
     * @param firstDayStr
     * @param lastDayStr
     * @param userIdList
     * @return
     */
    List<SettleStat> statCommitDay(@Param("startDay") String firstDayStr, @Param("endDay") String lastDayStr,
                                   @Param("userIdList") List<Long> userIdList);

    int insertBatch(List<CommitLog> commitLogList);
}
