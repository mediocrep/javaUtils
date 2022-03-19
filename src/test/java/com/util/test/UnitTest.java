package com.util.test;

//import java.util.Base64;

import com.util.PoiUtils;
import com.util.ThreadUtils;
import com.util.threadTask.Test3Task;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UnitTest {

    @Test
    void test1() {
//        System.out.println(StringUtils.getRandomStr(32));
//        System.out.println(StringUtils.getRandomStr2(32));
//
//        System.out.println(StringUtils.isEmpty(null)); //true
//        System.out.println(StringUtils.isEmpty("")); //true
//        System.out.println(StringUtils.isEmpty(" ad ")); //false
//        System.out.println(StringUtils.isEmpty("  ")); //true

//        System.out.println(StringUtils.getUUID());

//        System.out.println(RandomStringUtils.randomAlphanumeric(10));
//        System.out.println(RandomStringUtils.random(10,true,false));
//        System.out.println(RandomStringUtils.random(10,false,true));

        /*byte[] bytes = new byte[]{43,27,41,54};
        for (byte b:bytes){
            System.out.println(b);
        }
        System.out.println(new String(bytes,"UTF-8"));*/
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:h2:E:\\Develop\\H2\\db\\test6", "sa", "");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
    }

    @Test
    public void test2(){
        PoiUtils poiUtils = new PoiUtils();
        poiUtils.exportToExcel();
    }

    @Test
    public void test3(){
        List<List<String>> list = new ArrayList<>();

        List<String> l1 = new ArrayList<>();
        l1.add("a1");
        l1.add("a2");
        list.add(l1);
        l1 = new ArrayList<>();
        l1.add("b1");
        l1.add("b2");
        list.add(l1);

        for (List<String> l : list){
            // list.forEach(data -> System.out.println(data));
            System.out.println(l.hashCode());
            System.out.println(l);
            System.out.println("-----------------------");
        }

        System.out.println(list);
    }

    @Test
    public void test4() {
        String reportNo = "RP02-200100000003";
        final Test3Task test3Task = new Test3Task(2020, "RP02-200100000003");
        final Test3Task test3Task2 = new Test3Task(3020, "SP02-200100000003");
        final Test3Task test3Task3 = new Test3Task(4020, "TP02-200100000003");
        ThreadUtils.getDataList2(test3Task);
        System.out.println("--------------------------------------------------");
        System.out.println(ThreadUtils.getStringList(test3Task, test3Task2, test3Task3));
    }


}
