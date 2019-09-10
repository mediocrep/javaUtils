package com.utils;

import com.utils.constants.Constants;
import org.testng.annotations.Test;

import java.util.Random;
import java.util.UUID;

/**
 * String工具类
 */
public class StringUtils {
    /**
     * 根据传递过来的参数capacity，获取指定长度的随机字符串（方法1）
     * @param capacity
     * @return String
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
     * 根据传递过来的参数length，获取指定长度的随机字符串（方法2）
     * @param length
     * @return String
     */
    public static String getRandomStr2(int length){
        //1.  定义一个字符串（A-Z，a-z，0-9）即62个数字字母；
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789*!@#$%^&*()_+<>?";
        //2.  由Random生成随机数
        Random random=new Random();
        StringBuffer sb=new StringBuffer();
        //3.  长度为几就循环几次
        for(int i=0; i<length; ++i){
            //从62个的数字或字母中选择
            int number=random.nextInt(62);
            //将产生的数字通过length次承载到sb中
            sb.append(str.charAt(number));
        }
        //将承载的字符转换成字符串
        return sb.toString();
    }

    /**
     * 生成32位随机字符串，小写字母a - f，和数字组成（方法3）
     * @return String
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "");
    }

    /**
     * 获取指定长度的随机字符串（方法4）：
     * commons-lang.jar包下有一个RandomStringUtils类，
     *  (1)其中有一个randomAlphanumeric(int length)函数，可以随机生成一个长度为length的字符串
     *      RandomStringUtils.randomAlphanumeric(32)
     *  (2)RandomStringUtils.random(32,true,false) 获取包含字母、不含数字的32位随机字符串
     */


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
