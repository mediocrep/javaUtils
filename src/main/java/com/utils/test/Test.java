package com.utils.test;

//import java.util.Base64;
import org.apache.commons.codec.binary.Base64;

public class Test {

    public static void main(String[] args) throws Exception {
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

        byte[] bytes = new byte[]{43,27,41,54};

        for (byte b:bytes){
            System.out.println(b);
        }

        System.out.println(new String(bytes,"UTF-8"));


    }
}
