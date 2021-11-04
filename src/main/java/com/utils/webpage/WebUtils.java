package com.utils.webpage;

import com.utils.TimeUtils;
import com.utils.constants.Constants;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebUtils {

    public static void batchGetPic(String urlFile, final String localPath) throws IOException {

        Map<String, Integer> counter = new HashMap<>();
        counter.put("success", 0);
        counter.put("fail", 0);
        counter.put("exist", 0);
        // 读取 file 文件中的urls
        Files.lines(Paths.get(urlFile)).forEach(line -> {
            if (StringUtils.isNotBlank(line) && !line.startsWith("#")) {
                if (line.endsWith(".html")) {
                    // eg. "https://m.raoniao.com/rentiyishu/32269.html"
                    String localPath1 = localPath + "/" + TimeUtils.getTimeStr("YYYYMMdd");
                    File dir = new File(localPath1);
                    if (!dir.isDirectory()) {
                        dir.mkdirs();
                    }
                    batchGetPicByHtml(line, localPath1, counter);
                } else if (line.endsWith("/")) {  // 该类型的每一个网页中，都包含了多个第一种类型的url
                    // eg. "https://m.raoniao.com/tag/younisi/"
                    // 把所有第一种的url（即endsWith(".html")）都找出来，然后按照处理第一种的url的方式进行处理
                    String localPath2 = localPath + line.substring(line.substring(0, line.length() - 1).lastIndexOf("/"));
                    File dir = new File(localPath2);
                    if (!dir.isDirectory()) {
                        dir.mkdirs();
                    }
                    Document doc = null;
                    try {
                        /*
                        html中需要的元素的结构如下：
                        <div class="w98">
                         <ul id="ToWeb" class="listUll2" style="position:relative;">
                          <li class="mr5">
                           <div class="libox">
                            <a href="https://m.raoniao.com/rentiyishu/32765.html"><img src="https://pic.99ym.cn/d/qqre/202101/6spw5ba2x3dy.jpg" lazysrc="https://pic.99ym.cn/d/qqre/202101/6spw5ba2x3dy.jpg"><p>巨乳美女尤妮丝吊床上各种诱惑姿势天真人体艺术图片</p></a>
                           </div></li>
                          <li>
                           <div class="libox">
                            <a href="https://m.raoniao.com/neiyimeinv/32637.html"><img src="https://pic.99ym.cn/d/qqre/202101/16hv32qie5lf.jpg" lazysrc="https://pic.99ym.cn/d/qqre/202101/16hv32qie5lf.jpg"><p>巨乳细腰控美女尤妮丝猫儿情趣女仆制服水蛇腰吸精男人命</p></a>
                           </div></li>
                         */
                        doc = Jsoup.parse(new URL(line), 20000);
                        // System.out.println(doc);
                        doc.select("ul#ToWeb").first().select("a").stream().forEach(element -> batchGetPicByHtml(element.attr("href"), localPath2, counter));
                    } catch (IOException e) {
                        System.out.format("解析url (%s) 失败%n", line);
                        e.printStackTrace();
                    }
                }
            }
        });

        System.out.format("程序抓取图片总数：%d，其中成功数：%d（抓取成功的图片中，本身存在于本地不用抓取的图片数为：%d），失败数：%d%n", counter.get("success") + counter.get("fail"),
                counter.get("success"), counter.get("exist"), counter.get("fail"));
    }

    private static void batchGetPicByHtml(String line, final String localPath, Map<String, Integer> counter) {
//        String line = "https://m.raoniao.com/rentiyishu/32269.html";
        String prefix = line.substring(0, line.indexOf("/", 10));
        String imgUrl = "";  // 图片url
        String tmpUrl = "";  // 临时存放当前网页的url
        String nextUrl = "";  // 下一个网页的url

        boolean flag = false;  // 标志，false 图片抓取失败，true 图片抓取成功
        // 计数器，读取网页失败则递增1，连续失败才会递增，如果第一次读取网页失败，第2次读取成功，则cnt归零
        // 计数器，记录抓取图片总数 total，抓取图片失败的次数 fail，连续失败计数器 continuingFail（抓取图片失败后，重新抓取该图片，直到连续失败3次，才放弃尝试，抓取下一张）
        int success = 0, fail = 0, exist = 0, continuingFail = 0;
        // 记录抓取成功的图片url
//        List<String> successfulPics = new ArrayList<>();
        // 记录抓取失败的图片url
//        List<String> failedPics = new ArrayList<>();
        //输入到程序的数据流
        InputStream inputStream = null;
        InputStream inputStream2 = null;
        //输出到本地的数据流
        FileOutputStream fileOutputStream = null;
        OutputStream outputStream = null;

        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(line), 20000);
        } catch (IOException e) {
            System.out.format("解析url (%s) 失败%n", line);
            e.printStackTrace();
        }

        // 循环抓取每一页的图片
        // line.equals(nextUrl) 表示 不停地处理下一页，最终处理完了所有页，最终回到了第一页；fail < 6 表示 下载失败的页面中的图片不超过5个
        while (!line.equals(nextUrl) && fail < 6) {
            tmpUrl = nextUrl;
            if (null != doc) {
                Elements children = doc.select("div.ArticleBox").first().children();
                for (Element element : children) {  //
//                    Elements children2 = element.children();
                    imgUrl = "";
                    if (!element.children().isEmpty()) {
//                    System.out.println("children is not null.");
                        Element img = element.select("img").first();
                        if (null != img) { // 该if语句解决 element.select("img").first() 为 null 从而导致 java.lang.NullPointerException 的 bug
                            imgUrl = img.attr("src");
                            while (!flag && continuingFail < 4) {
                                try {
                                /*
                                    html中需要的元素的 3 种结构如下：
                                    结构1：
                                    <div class="tip">
                                     Tips：点击图片进入下一页&amp;长按图片存图
                                    </div>
                                    <script type="text/javascript">nei_top()</script>
                                    <div class="ArticleBox">
                                     <a class="" href="/neiyimeinv/29490_2.html"> </a>
                                     <p><a class="" href="/neiyimeinv/29490_2.html"><img alt="图片第1张" src="https://img.99ym.cn/d/fiel/20200516/sbpitp0ycch.jpg"></a> </p>
                                    </div>

                                    结构2：
                                    <div class="ArticleBox">
                                        <a class="" href="/rentiyishu/32269_11.html"> <img alt="图片(图10)" src="https://pic.99ym.cn/d/qqre/202101/x5ezjjyxnq3.jpg"></a>
                                    </div>

                                    <div class="ArticleBox">

                                    结构3：
                                    <div class="ArticleBox">
                                     <a class="" href="/rentiyishu/28025_5.html"> <img alt="尤蜜荟性感女神尤妮丝霸屏白嫩双峰巨乳美臀风情万种第4张" src="https://img.99ym.cn/d/fiel/20190526/ifvs25puu3v.jpg"> </a>
                                     <p><a class="" href="/rentiyishu/28025_5.html"></a> </p>
                                    </div>
                                */
                                    // 断点续传功能：先判断本地有无该图片，如果有，则不抓取，并让相应的计数器递增1
                                    File imgFile = new File(localPath + imgUrl.substring(imgUrl.lastIndexOf("/")));
                                    if (null != imgFile && imgFile.exists() && imgFile.isFile()) {
                                        exist++;
                                        System.out.println("图片已存在于本地！图片URL：" + imgUrl);
                                    } else {
                                        Connection.Response response = Jsoup.connect(imgUrl).method(Connection.Method.GET).ignoreContentType(true).timeout(20000).execute();
                                        inputStream2 = response.url().openStream();
                                        inputStream = new BufferedInputStream(inputStream2);
                                        fileOutputStream = new FileOutputStream(imgFile);
                                        outputStream = new BufferedOutputStream(fileOutputStream);
                                        fetchToLocal(inputStream, outputStream);
                                    }
                                    flag = true;
                                    nextUrl = prefix + element.select("a").first().attr("href");
                                    break;
                                } catch (IOException e) {
                                    continuingFail++;
                                    System.out.format("本次抓取图片 (%s) 连续失败 %d 次，将立即重试（最多重试3次）...%n", imgUrl, continuingFail);
                                    e.printStackTrace();
                                } finally {
                                    closeStream(outputStream, fileOutputStream, inputStream, inputStream2);
                                }
                            }
                        }
                    }
                }
            }
            // 不止这里需要设置nextUrl，其他情况也可能需要，所以在下面已经有这一步了，所以这里可以省略
//            else {  // 如果解析url失败，则无法根据当前url获取下一页的url，即nextUrl，此时需要尝试根据上一页url和下一页url之间的关系，使用拼接的方式来生成nextUrl
//                if ("".equals(nextUrl)) {
//                    // 根据line拼接： eg. https://m.raoniao.com/rentiyishu/32364.html
//                    nextUrl = line.substring(0, line.lastIndexOf(".")) + "_2.html";
//                } else {
//                    // 根据nextUrl拼接： eg. https://m.raoniao.com/rentiyishu/32364_2.html
//                    int underlineIndex = nextUrl.lastIndexOf("_");
//                    int page = Integer.parseInt(nextUrl.substring(underlineIndex + 1, nextUrl.lastIndexOf("."))) + 1; // 当前页的下一页
//                    nextUrl = nextUrl.substring(0, underlineIndex + 1) + page + ".html";
//                }
//            }

            if (flag) {
                // successfulPics.add(imgUrl);
                System.out.println("下载图片成功！图片URL：" + imgUrl);
                success++;
            } else {
                System.out.println("下载图片失败！图片URL：" + imgUrl);
                // failedPics.add(imgUrl);
                fail++;
                if (tmpUrl.equals(nextUrl)) {  // nextUrl没有变化，说明设置为下一页的url，需要设置一下
                    if ("".equals(nextUrl)) {
                        // 根据line拼接： eg. https://m.raoniao.com/rentiyishu/32364.html
                        nextUrl = line.substring(0, line.lastIndexOf(".")) + "_2.html";
                    } else {
                        // 根据nextUrl拼接： eg. https://m.raoniao.com/rentiyishu/32364_2.html
                        int underlineIndex = nextUrl.lastIndexOf("_");
                        int page = Integer.parseInt(nextUrl.substring(underlineIndex + 1, nextUrl.lastIndexOf("."))) + 1; // 当前页的下一页
                        nextUrl = nextUrl.substring(0, underlineIndex + 1) + page + ".html";
                    }
                }
            }

            flag = false;  // 重置flag为false
            continuingFail = 0;  // 不管抓取图片成功还是失败，都重置 计数器 continuingFail 为 0

            try {
                doc = Jsoup.parse(new URL(nextUrl), 20000);
//                System.out.println(doc);
            } catch (IOException e) {
                System.out.format("解析url (%s) 失败%n", nextUrl);
                e.printStackTrace();
            }
        }
        counter.put("success", counter.get("success") + success);
        counter.put("fail", counter.get("fail") + fail);
        counter.put("exist", counter.get("exist") + exist);
        System.out.format("URL: %s，本轮抓取图片总数：%d，其中成功数：%d（抓取成功的图片中，本身存在于本地不用抓取的图片数为：%d），失败数：%d%n", line, success + fail, success, exist, fail);
        if (fail > 5) {
            System.out.println("另外，本轮抓取图片失败数 > 5，主动终止本轮抓取！");
        }
    }

    public static void fetchToLocal(InputStream inputStream, OutputStream outputStream) throws IOException {
        //一次最多读取1k
        byte[] buffer = new byte[1024];
        //实际读取的长度
        int readLenghth;

        //文件逐步写入本地
        while ((readLenghth = inputStream.read(buffer,0,1024)) != -1) {  // 先读出来，保存在buffer数组中
//            System.out.println(readLenghth);
            outputStream.write(buffer,0,readLenghth);//再从buffer中取出来保存到本地
        }
    }

    private static void batchGetFailedPics(List<String> failedPicUrls) {
        //输入到程序的数据流
        InputStream inputStream = null;
        InputStream inputStream2 = null;
        //输出到本地的数据流
        FileOutputStream fileOutputStream = null;
        OutputStream outputStream = null;
        for (String imgUrl: failedPicUrls) {
            try {
                Connection.Response response = Jsoup.connect(imgUrl).method(Connection.Method.GET).ignoreContentType(true).timeout(20000).execute();
                inputStream2 = response.url().openStream();
                inputStream = new BufferedInputStream(inputStream2);
                fileOutputStream = new FileOutputStream(Constants.LOCAL_PIC_PATH + imgUrl.substring(imgUrl.lastIndexOf("/"), imgUrl.length()));
                outputStream = new BufferedOutputStream(fileOutputStream);
                fetchToLocal(inputStream, outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeStream(outputStream, fileOutputStream, inputStream, inputStream2);
            }
        }
    }

    private static void closeStream(OutputStream outputStream, FileOutputStream fileOutputStream, InputStream inputStream, InputStream inputStream2) {
        if (null != outputStream) {
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (null != inputStream) {
            try {
                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (null != inputStream2) {
            try {
                inputStream2.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (null != fileOutputStream) {
            try {
                fileOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        /*
        batchGetPic("data/urls.txt");  // 抓取图片总数：295，其中成功数：290，失败数：5
        batchGetPic("data/urls2.txt", Constants.LOCAL_PIC_PATH2);  // 抓取图片总数：327，其中成功数：327，失败数：0  程序总耗时：421669 毫秒！
        List<String> failedPicUrls = new ArrayList<>();
        failedPicUrls.add("https://img.99ym.cn/d/file/202009/kon4ooqajgi.jpg");
        failedPicUrls.add("https://pic.99ym.cn/d/qqre/20200427/nz55wxjbqs5.jpg");
        failedPicUrls.add("https://pic.99ym.cn/d/qqre/20200427/0ni0hon2rvi.jpg");
        failedPicUrls.add("https://pic.99ym.cn/d/qqre/20200427/jp2awerd4rn.jpg");
        failedPicUrls.add("https://pic.99ym.cn/d/qqre/20200427/jfb4d53p4rs.jpg");
        batchGetFailedPics(failedPicUrls);
        */

        batchGetPic("data/urls4.txt", Constants.LOCAL_PIC_PATH);  // 抓取图片总数：327，其中成功数：327，失败数：0  程序总耗时：421669 毫秒！

        long end = System.currentTimeMillis();
        System.out.println("程序总耗时：" + (end - start) + " 毫秒！");
    }
}
