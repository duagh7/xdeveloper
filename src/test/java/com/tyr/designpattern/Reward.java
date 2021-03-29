package com.tyr.designpattern;

import com.tyr.xdeveloper.XdeveloperStarter;
import com.tyr.xdeveloper.service.ExcelService;
import com.tyr.xdeveloper.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = XdeveloperStarter.class)
public class Reward {

    @Autowired
    private ExcelService excelService;

    @Autowired
    private UserService userService;

    @Test
    public void test1() {
        excelService.reward();

        assert true;
    }

    @Test
    public void test2(){
//        String s = userService.settleToday(settleStr, "2021-03-23");
//        System.out.println(s);

    }

}
