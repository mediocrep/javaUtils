package com.utils.test;

import com.utils.StringUtils;
import org.apache.commons.lang.RandomStringUtils;

public class Test {

    public static void main(String[] args) {
        String randomStr = StringUtils.getRandomStr(32);
        System.out.println(randomStr);

        System.out.println(StringUtils.isEmpty(null)); //true
        System.out.println(StringUtils.isEmpty("")); //true
        System.out.println(StringUtils.isEmpty(" ad ")); //false
        System.out.println(StringUtils.isEmpty("  ")); //true

        System.out.println(StringUtils.getUUID());

        System.out.println(RandomStringUtils.randomAlphanumeric(10));
        System.out.println(RandomStringUtils.random(10,true,false));
        System.out.println(RandomStringUtils.random(10,false,true));

    }
}
