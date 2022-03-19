package com.util.constants;

/**
 * 接口：用于存放各种常量
 */
public interface Constants {

    public static final String[] RANDOM_STR_ARR = {
        "0","1","2","3","4","5","6","7","8","9","A","B","C","D","E",
        "F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T",
        "U","V","W","X","Y","Z","a","b","c","d","e","f","g","h","i",
        "j","k","l","m","n","o","p","q","r","s","t","u","v","w","x",
        "y","z","_","-"
    };

    public static final String REPORT_YEAR = "2020";
    public static final String REPORT_NO = "RP01-2001000000001";
    public static final String REPORT_NAME = "2020 Report (just for test)";
    public static final String REPORT_TITLE = "参加调研的样本数量";

    public static final String LOCAL_PIC_PATH = "/home/witt/ettmt/pic/beau";
    public static final String LOCAL_DYNAMIC_DATASOURCE_PATH = "/home/witt/1project/microservices/doc/dynamic_datasource/dynamic_datasource_doc.md";
    public static final int RETRY_SINGLE_PIC_MAX = 3;  // 单张图片
    public static final int RETRY_SINGLE_PIC_SET_MAX = 5;  // 一套图片

    // time type
    public final static String TIME_SECOND = "SECOND";
    public final static String TIME_MILLISECOND = "MILLISECOND";
    public final static String TIME_YMD = "YYYY-MM-dd";
    public final static String TIME_HMS = "HH:mm:ss";
    public final static String TIME_YMDHMS = "YYYY-MM-dd HH:mm:ss";



}
