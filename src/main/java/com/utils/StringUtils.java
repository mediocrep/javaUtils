package com.utils;

import com.utils.constants.Constants;

import java.util.Random;

/**
 * String工具类
 */
public class StringUtils {
    /**
     * 根据传递过来的参数capacity，获取capacity长度的随机字符串
     * @param capacity
     * @return
     */
    public static String getRandomStr(int capacity){
        Random random = new Random();
        int len = Constants.RANDOM_STR_ARR.length;
        StringBuilder sb = new StringBuilder(capacity);
        for (int i = 0; i < capacity; i++){
            String ch = Constants.RANDOM_STR_ARR[random.nextInt(len)];
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * 判断字符串是否为空：null或""
     * @param str
     * @return
     */
    public static boolean isEmpty(String str){
        if(str == null)
            return true;
        else
            return "".equals(str.trim());
    }

    /**
     * 判断字符串是否非空：非null并且非""
     * @param str
     * @return
     */
    public static boolean isNotEmpty(String str){
        return !isEmpty(str);
    }




}
