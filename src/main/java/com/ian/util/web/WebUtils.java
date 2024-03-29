package com.ian.util.web;

import com.ian.util.TimeUtils;
import com.ian.util.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class WebUtils {

    public static void batchGetPic(String urlFile, final String localPath) throws IOException {

        Map<String, Integer> counter = new HashMap<>();
        counter.put("success", 0);
        counter.put("fail", 0);
        counter.put("exist", 0);
        // 读取 file 文件中的urls
        Files.lines(Paths.get(urlFile)).forEach(line -> {
            if (StringUtils.isNotBlank(line) && !line.startsWith("#")) {
                if (line.endsWith(".html") && !line.contains("tag")) {
                    // eg. "https://m.raoniao.com/rentiyishu/32269.html"
                    String localPath1 = localPath + "/" + TimeUtils.getTimeStr("YYYYMMdd");
                    File dir = new File(localPath1);
                    if (!dir.isDirectory()) {
                        dir.mkdirs();
                    }
                    batchGetPicByHtml(line, localPath1, counter);
                } else if (line.contains("tag")) {  // 该类型的每一个网页中，都包含了多个第一种类型的url
                    // eg. "https://m.raoniao.com/tag/yiyang/" ， https://m.raoniao.com/tag/yiyang/index_2.html
                    // 在该url中找出所有第一种的url（即line.endsWith(".html") && !line.contains("tag")），然后按照处理第一种的url的方式进行处理
                    String localPath2 = localPath + line.substring(line.indexOf("tag") + 3, line.lastIndexOf("/"));
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
                            <a href="https://m.raoniao.com/rentiyishu/32765.html"><img src="https://pic.99ym.cn/d/qqre/202101/6spw5ba2x3dy.jpg" lazysrc="https://pic.99ym.cn/d/qqre/202101/6spw5ba2x3dy.jpg"><p>史蒂文无</p></a>
                           </div></li>
                          <li>
                           <div class="libox">
                            <a href="https://m.raoniao.com/neiyimeinv/32637.html"><img src="https://pic.99ym.cn/d/qqre/202101/16hv32qie5lf.jpg" lazysrc="https://pic.99ym.cn/d/qqre/202101/16hv32qie5lf.jpg"><p>是的发送到</p></a>
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

        System.out.format("%n程序抓取图片总数：%d，其中成功数：%d（抓取成功的图片中，本身存在于本地不用抓取的图片数为：%d），失败数：%d%n", counter.get("success") + counter.get("fail"),
                counter.get("success"), counter.get("exist"), counter.get("fail"));
    }

    private static void batchGetPicByHtml(String line, final String localPath, Map<String, Integer> counter) {
//        String line = "https://m.raoniao.com/rentiyishu/32269.html";
        String prefix = line.substring(0, line.indexOf("/", 10));  // prefix eg. "https://m.raoniao.com"
        String prefix2 = line.substring(0, line.indexOf("/", 26));  // 有时获取的a标签中的href为这种格式“27252_3.html”，此时需要用prefix2，否则拼接的nextUrl就是错的
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
            log.error(e.toString());
        }

        // 循环抓取每一页的图片
        // line.equals(nextUrl) 表示 不停地处理下一页，最终处理完了所有页，最终回到了第一页；fail < 6 表示 下载失败的页面中的图片不超过5个
        while (!line.equals(nextUrl) && fail <= Constants.RETRY_SINGLE_PIC_SET_MAX) {
            imgUrl = "";
            tmpUrl = nextUrl;
            if (null != doc) {
                Elements children = doc.select("div.ArticleBox").first().children();
                for (Element element : children) {  //
//                    Elements children2 = element.children();
                    if (!element.children().isEmpty()) {
//                    System.out.println("children is not null.");
                        Element img = element.select("img").first();
                        if (null != img) { // 该if语句解决 element.select("img").first() 为 null 从而导致 java.lang.NullPointerException 的 bug
                            imgUrl = img.attr("src");
                            while (!flag && continuingFail <= Constants.RETRY_SINGLE_PIC_MAX) {
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
                                     <a class="" href="/rentiyishu/28025_5.html"> <img alt="卫栖梧二去玩儿第4张" src="https://img.99ym.cn/d/fiel/20190526/ifvs25puu3v.jpg"> </a>
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

                                    // 拼接nextUrl
                                    String href = element.select("a").first().attr("href");
                                    if (StringUtils.isNotBlank(href)) {  // href eg. /rentiyishu/27571.html
                                        if (href.length() > 15) {
                                            nextUrl = prefix + href;
                                        } else if (href.contains("/")) {  // href eg. /27252_3.html
                                            nextUrl = prefix2 + href;
                                        } else {  // href eg. 27252_3.html
                                            nextUrl = prefix2 + "/" + href;
                                        }
                                    }
                                    break;
                                } catch (IOException e) {
                                    continuingFail++;
                                    if (continuingFail <= Constants.RETRY_SINGLE_PIC_MAX) {
                                        System.out.format("本次抓取图片 (%s) 连续失败 %d 次，第 %d 次重试（最多重试 %d 次）...%n", imgUrl, continuingFail, continuingFail, Constants.RETRY_SINGLE_PIC_MAX);
                                    } else {
                                        System.out.format("本次抓取图片 (%s) 连续失败 %d 次，终止重试！%n", imgUrl, continuingFail);
                                    }
                                    e.printStackTrace();
                                } finally {
                                    closeStream(outputStream, fileOutputStream, inputStream, inputStream2);
                                }
                            }
                        }
                    }

                    if (flag) {
                        break;
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

            doc = null;  // 重置doc 为 null，解决“由于nextUrl网址错误，从而导致下面的Jsoup.parse()解析失败，从而导致doc还是上一次解析的内容，从而导致下一次循环赋值给nextUrl的还是之前的错误的网址，从而死循环”的bug
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
        if (fail > Constants.RETRY_SINGLE_PIC_SET_MAX) {
            System.out.format("另外，本轮抓取图片失败数 > %d 次，主动终止本轮抓取！", Constants.RETRY_SINGLE_PIC_SET_MAX);
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


    /**
     * 获取文字 from dynamic-datasource文档
     * @param urlFile
     * @param localPath
     * @throws IOException
     */
    public static void batchGetWord(String urlFile, final String localPath) throws IOException {

        /*Map<String, Integer> counter = new HashMap<>();
        counter.put("success", 0);
        counter.put("fail", 0);
        counter.put("exist", 0);*/
        // 读取 file 文件中的urls
        Files.lines(Paths.get(urlFile)).filter(line -> StringUtils.isNotBlank(line) && !line.startsWith("#")).forEach(line -> {
            Document doc = null;
            try {
                    /*
                    html中需要的元素的结构如下：
                     */
                String[] split = line.split(":::");
                doc = Jsoup.parse(new URL(split[0]), 20000);
                System.out.println(doc);
                String content = doc.select("div.content").first().html();
//                System.out.println(content);

//                doc.select("ul#ToWeb").first().select("a").stream().forEach(element -> batchGetPicByHtml(element.attr("href"), localPath2, counter));

                Path path = Paths.get(Constants.LOCAL_DYNAMIC_DATASOURCE_PATH);
                Files.write(path, content.getBytes(StandardCharsets.UTF_8));

            } catch (IOException e) {
                System.out.format("解析url (%s) 失败%n", line);
                e.printStackTrace();
            }
        });

        /*System.out.format("%n程序抓取图片总数：%d，其中成功数：%d（抓取成功的图片中，本身存在于本地不用抓取的图片数为：%d），失败数：%d%n", counter.get("success") + counter.get("fail"),
                counter.get("success"), counter.get("exist"), counter.get("fail"));*/
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
}
