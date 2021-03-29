package com.tyr.xdeveloper.service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tyr.xdeveloper.common.model.User;
import com.tyr.xdeveloper.mapper.UserMapper;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

@Service
public class ExcelService {

    @Resource
    private UserMapper userMapper;

    public void reward() {
        try {

            List<String> nameList = excel();

            List<User> userList = userMapper.selectByNickNameList(nameList);
            List<User> weightedUserList = getWeightList(userList);

            HashSet<String> rewardNamesList = Sets.newHashSet();

            do {
                long mills = System.currentTimeMillis();
                Random random = new Random(mills);
                int rewardIndex = random.nextInt(weightedUserList.size() - 1);
                User user = weightedUserList.get(rewardIndex);
                rewardNamesList.add(user.getNickName());

            } while (rewardNamesList.size() < 3);

            System.out.println("恭喜以下姐妹中奖：" + JSON.toJSONString(rewardNamesList));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<User> getWeightList(List<User> userList) {
        List<User> weightedUserList = Lists.newArrayList();
        weightedUserList.addAll(userList);
        for (User user : userList) {
            Integer holidayNum = user.getHolidayNum();
            for (int i = 0; i < holidayNum; i++) {
                weightedUserList.add(user);
            }
        }
        return weightedUserList;
    }

    public List<String> excel() throws IOException {
        String filePath = "/Users/guili/Documents/it/namelist.xlsx";

        XSSFWorkbook wookbook = new XSSFWorkbook(new FileInputStream(filePath));
        XSSFSheet sheet = wookbook.getSheet("Sheet1");

        //获取到Excel文件中的所有行数
        int rows = sheet.getPhysicalNumberOfRows();

        //遍历行
        List<String> list = Lists.newArrayList();

        for (int i = 1; i < rows; i++) {
            // 读取左上端单元格
            XSSFRow row = sheet.getRow(i);
            if (row != null) {
                //获取打卡昵称
                XSSFCell nameCell = row.getCell(0);
                String name = getValue(nameCell);
                list.add(name);
            }
        }

        System.out.println("list = " + JSON.toJSONString(list));
        return list;
    }

    private String getValue(XSSFCell xSSFCell) {
        if (null == xSSFCell) {
            return "";
        }
        if (xSSFCell.getCellType() == xSSFCell.CELL_TYPE_BOOLEAN) {
            // 返回布尔类型的值
            return String.valueOf(xSSFCell.getBooleanCellValue());
        } else if (xSSFCell.getCellType() == xSSFCell.CELL_TYPE_NUMERIC) {
            // 返回数值类型的值
            return String.valueOf(xSSFCell.getNumericCellValue());
        } else {
            // 返回字符串类型的值
            return String.valueOf(xSSFCell.getStringCellValue());
        }
    }

}