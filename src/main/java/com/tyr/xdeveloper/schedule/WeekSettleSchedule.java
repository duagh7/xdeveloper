package com.tyr.xdeveloper.schedule;

import com.tyr.xdeveloper.common.model.SettleStat;
import com.tyr.xdeveloper.common.model.User;
import com.tyr.xdeveloper.common.util.DateUtil;
import com.tyr.xdeveloper.mapper.CommitLogMapper;
import com.tyr.xdeveloper.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class WeekSettleSchedule {

    @Resource
    private UserMapper userMapper;

    @Resource
    private CommitLogMapper commitLogMapper;

    @Autowired
    private PlatformTransactionManager transactionManager;

    public static final int shouldCommitDayNum = 6;

    @Scheduled(cron = "0 0 4 * * 1")
    public void settle() {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            List<User> userList = userMapper.getUserList();
            if (CollectionUtils.isEmpty(userList)) {
                return;
            }
            List<Long> userIdList = userList.stream().filter(u -> u.getStatus() == 0).map(User::getId).collect(Collectors.toList());

            Date lastWeekLastDay = DateUtil.getYesterday();
            Date lastWeekFirstDay = DateUtil.addDayToDate(lastWeekLastDay, -6);

            String firstDayStr = DateUtil.format(lastWeekFirstDay, DateUtil.FORMAT_STR3);
            String lastDayStr = DateUtil.format(lastWeekLastDay, DateUtil.FORMAT_STR3);

            List<SettleStat> settleStatList = commitLogMapper.statCommitDay(firstDayStr, lastDayStr, userIdList);
            if (CollectionUtils.isEmpty(settleStatList)) {
                return;
            }

            settleStatList = settleStatList.stream().filter(settleStat -> settleStat.getCommitDayNum() > shouldCommitDayNum).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(settleStatList)) {
                return;
            }

            List<User> holidayUsers = settleStatList.stream().map(settleStat -> {
                User user = new User();
                user.setId(settleStat.getUserId());
                int holidayNum = settleStat.getCommitDayNum() - shouldCommitDayNum;
                user.setHolidayNum(holidayNum);
                return user;
            }).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(holidayUsers)) {
                return;
            }

            for (User holidayUser : holidayUsers) {
                userMapper.updateHolidayNum(holidayUser);
            }

            String holidayStr = holidayUsers.stream().map(u -> u.getId() + " ").collect(Collectors.joining());

            log.info("以下id增加假期：" + holidayStr);

            transactionManager.commit(status);
        } catch (Exception e) {
            transactionManager.rollback(status);
            log.error("结算程序异常", e);
        }

    }


}
