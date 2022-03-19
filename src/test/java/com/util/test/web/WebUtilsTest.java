package com.util.test.web;

import com.util.constants.Constants;
import com.util.web.WebUtils;
import org.testng.annotations.Test;

import java.io.IOException;

public class WebUtilsTest {

    @Test
    public void batchGetPicTest() throws IOException {
        long start = System.currentTimeMillis();
        /*
        batchGetPic("data/urls.txt");  // 抓取图片总数：295，其中成功数：290，失败数：5
        batchGetPic("data/urls2.txt", Constants.LOCAL_PIC_PATH2);  // 抓取图片总数：327，其中成功数：327，失败数：0  程序总耗时：421669 毫秒！
        batchGetPic("data/urls4.txt", Constants.LOCAL_PIC_PATH);  // 抓取图片总数：327，其中成功数：327，失败数：0  程序总耗时：421669 毫秒！
        List<String> failedPicUrls = new ArrayList<>();
        failedPicUrls.add("https://img.99ym.cn/d/file/202009/kon4ooqajgi.jpg");
        failedPicUrls.add("https://pic.99ym.cn/d/qqre/20200427/nz55wxjbqs5.jpg");
        failedPicUrls.add("https://pic.99ym.cn/d/qqre/20200427/0ni0hon2rvi.jpg");
        failedPicUrls.add("https://pic.99ym.cn/d/qqre/20200427/jp2awerd4rn.jpg");
        failedPicUrls.add("https://pic.99ym.cn/d/qqre/20200427/jfb4d53p4rs.jpg");
        batchGetFailedPics(failedPicUrls);
        */
        // 程序抓取图片总数：639，其中成功数：633（抓取成功的图片中，本身存在于本地不用抓取的图片数为：0），失败数：6   程序总耗时：558393 毫秒！
        WebUtils.batchGetPic("data/urls5.txt", Constants.LOCAL_PIC_PATH);
        long end = System.currentTimeMillis();
        System.out.println("程序总耗时：" + (end - start) + " 毫秒！");
    }

    @Test
    public void batchGetWordTest() throws IOException {
        WebUtils.batchGetWord("data/dynamic_datasource_urls.txt", Constants.LOCAL_DYNAMIC_DATASOURCE_PATH);
    }
}
