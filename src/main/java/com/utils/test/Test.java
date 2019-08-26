package com.utils.test;

import com.utils.StringUtils;

public class Test {

    public static void main(String[] args) {
        String randomStr = StringUtils.getRandomStr(32);
        System.out.println(randomStr);

        System.out.println(StringUtils.isEmpty(null)); //true
        System.out.println(StringUtils.isEmpty("")); //true
        System.out.println(StringUtils.isEmpty(" ad ")); //false
        System.out.println(StringUtils.isEmpty("  ")); //true



    }
}
