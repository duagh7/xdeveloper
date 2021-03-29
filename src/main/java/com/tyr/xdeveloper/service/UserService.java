package com.tyr.xdeveloper.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.tyr.xdeveloper.common.model.AbsentSettle;
import com.tyr.xdeveloper.common.model.CommitLog;
import com.tyr.xdeveloper.common.model.User;
import com.tyr.xdeveloper.common.util.DateUtil;
import com.tyr.xdeveloper.mapper.AbsentSettleMapper;
import com.tyr.xdeveloper.mapper.CommitLogMapper;
import com.tyr.xdeveloper.mapper.UserMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private AbsentSettleMapper absentSettleMapper;

    @Resource
    private CommitLogMapper commitLogMapper;

    @Transactional
    public String collectName(String wxAccount, String nickName) {
        User user = userMapper.selectByWxAccount(wxAccount);
        List<User> similarUserList = userMapper.selectByNickName(nickName);
        if (user != null) {
            if (!CollectionUtils.isEmpty(similarUserList)) {
                if (similarUserList.size() > 1) {
                    return "昵称有重复，请录入一个更有识别度的昵称~";
                }
                User oldUser = similarUserList.get(0);
                String oldWxAccount = oldUser.getWxAccount();
                if (!oldWxAccount.equals(wxAccount)) {
                    return "昵称有重复，请录入一个更有识别度的昵称~";
                }
            }
            User updateUser = new User();
            updateUser.setNickName(nickName);
            updateUser.setId(user.getId());
            int i = userMapper.updateNickNameById(updateUser);
        } else {
            if (!CollectionUtils.isEmpty(similarUserList)) {
                return "昵称有重复，请录入一个更有识别度的昵称~";
            }
            User addUser = new User()
                    .setNickName(nickName)
                    .setWxAccount(wxAccount);
            int i = userMapper.addUser(addUser);
        }
        return "OK";
    }

    @Transactional
    public String settleToday(String settleStr, String settleDay) {
        StringBuilder stringBuilder = new StringBuilder();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date settleDate = Optional.ofNullable(settleDay).map(source -> {
            try {
                return simpleDateFormat.parse(source);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return DateUtil.getYesterday();
        })
                .orElse(DateUtil.getYesterday());
        settleDay = DateUtil.format(settleDate, "yyyy-MM-dd");

        List<User> userList = userMapper.getUserList();
        userList = userList.stream().filter(u -> u.getStatus() == 0).collect(Collectors.toList());

        List<Long> absentList = Lists.newArrayList();
        List<Long> commitList = Lists.newArrayList();

        stringBuilder.append(settleDay).append("计入请假：");
        for (User user : userList) {
            if (settleStr.contains(user.getNickName())) {
                commitList.add(user.getId());
            } else {
                absentList.add(user.getId());
                stringBuilder.append("@").append(user.getNickName()).append(" ");
            }
        }
        stringBuilder.append("\r\n");

        List<Long> absentTwiceList = Lists.newArrayList();
        Date beginOfWeek = DateUtil.getBeginOfWeek(settleDate);
        List<AbsentSettle> absentSettleList = absentSettleMapper.getByTime(beginOfWeek, settleDate);
        for (AbsentSettle formerAbsentSettle : absentSettleList) {
            String absentStr = formerAbsentSettle.getAbsentStr();
            List<Long> userIdList = JSONArray.parseArray(absentStr, Long.class);
            if (CollectionUtils.isEmpty(userIdList)) {
                continue;
            }
            for (Long todayAbsentUserId : absentList) {
                if (userIdList.contains(todayAbsentUserId)) {
                    absentTwiceList.add(todayAbsentUserId);
                }
            }
        }

        //把今天的存起来
        AbsentSettle absentSettle = new AbsentSettle()
                .setAbsentStr(JSON.toJSONString(absentList))
                .setSettleDay(settleDate);
        absentSettleMapper.insertAbsentSettle(absentSettle);


        if (!CollectionUtils.isEmpty(absentTwiceList)) {
            List<User> absentTwiceUserList = userMapper.selectByUserIdList(absentTwiceList);
            Map<Boolean, List<User>> listMap = absentTwiceUserList.stream().collect(Collectors.groupingBy(u -> u.getHolidayNum() > 0, Collectors.toList()));

            List<User> reduceHolidayUsers = listMap.get(true);
            List<User> goodByeUsers = listMap.get(false);

            if (!CollectionUtils.isEmpty(reduceHolidayUsers)) {
                reduceHolidayUsers.forEach(u -> u.setHolidayNum(-1));
                for (User reduceHolidayUser : reduceHolidayUsers) {
                    userMapper.updateHolidayNum(reduceHolidayUser);
                }
            }

            if (!CollectionUtils.isEmpty(goodByeUsers)) {
                List<String> collect = goodByeUsers.stream().map(User::getNickName).collect(Collectors.toList());

                stringBuilder.append("本周请假超过2次，江湖再见：");
                collect.forEach(c -> stringBuilder.append("@").append(c).append(" "));
            }
        }

        if (!CollectionUtils.isEmpty(commitList)) {
            List<CommitLog> commitLogList = commitList.stream().map(userId -> {
                CommitLog commitLog = new CommitLog();
                commitLog.setUserId(userId);
                commitLog.setSettleDay(settleDate);
                return commitLog;
            }).collect(Collectors.toList());
            commitLogMapper.insertBatch(commitLogList);
        }

        return stringBuilder.toString();
    }

    public String checkHoliday(String wxAccount) {
        User user = userMapper.selectByWxAccount(wxAccount);
        if (user == null) {
            return "该用户不存在";
        }
        return "您的假期余额为:" + user.getHolidayNum();
    }
}
