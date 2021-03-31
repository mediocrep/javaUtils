package com.utils.test;

//import java.util.Base64;

import com.utils.PoiUtils;
import org.testng.annotations.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
}
