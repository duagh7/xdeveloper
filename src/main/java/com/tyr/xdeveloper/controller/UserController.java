package com.tyr.xdeveloper.controller;

import com.tyr.xdeveloper.service.UserService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @GetMapping("/collectName")
    public String collectName(String wxAccount, String nickName) {
        if (StringUtils.isEmpty(wxAccount) || StringUtils.isEmpty(nickName)) {
            return "参数不能为空";
        }
        if (nickName.getBytes(StandardCharsets.UTF_8).length < 4){
            return "昵称需要大于2个汉字或4个英文字符";
        }
        return userService.collectName(wxAccount, nickName);
    }

    @GetMapping("/settleToday")
    public String settleToday(String settleStr) {
        return userService.settleToday(settleStr, null);
    }

    @GetMapping("/settleDay")
    public String settleToday(String settleStr, String settleDay) {
        return userService.settleToday(settleStr, settleDay);
    }

    @GetMapping("/checkHoliday")
    public String checkHoliday(String wxAccount){
        if (StringUtils.isEmpty(wxAccount)){
            return "请填写微信号";
        }
        return userService.checkHoliday(wxAccount);
    }


}
