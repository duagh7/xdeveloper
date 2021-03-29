package com.tyr.xdeveloper.mapper;

import com.tyr.xdeveloper.common.model.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper {

    User selectByWxAccount(String wxAccount);

    List<User> selectByNickName(String nickName);

    User getUserById(Long id);

    List<User> getUserList();

    int updateNickNameById(User user);

    int addUser(User user);

    List<User> selectByUserIdList(@Param("userIdList") List<Long> absentTwiceList);

    int updateHolidayNum(User holidayUser);

    List<User> selectByNickNameList(@Param("nameList") List<String> nameList);
}
