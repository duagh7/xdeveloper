package com.tyr.designpattern;

import com.tyr.xdeveloper.XdeveloperStarter;
import com.tyr.xdeveloper.schedule.WeekSettleSchedule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = XdeveloperStarter.class)
public class TestJob {

    @Autowired
    private WeekSettleSchedule weekSettleSchedule;

    @Test
    public void test1() {
        weekSettleSchedule.settle();
    }

}
