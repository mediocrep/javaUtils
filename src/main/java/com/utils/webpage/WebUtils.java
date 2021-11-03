package com.utils.webpage;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class WebUtils {

    public static void batchGetPic(String file) throws IOException {

        // 读取 file 文件中的urls
        Files.lines(Paths.get("data/urls.txt")).forEach(line -> {
            if (StringUtils.isNotBlank(line) && !line.startsWith("#")) {
//                System.out.println(line);
                if (line.endsWith("html")) {

//                    while () {
//                        Document doc = Jsoup.parse(new URL("https://m.raoniao.com/neiyimeinv/29490.html"), 20000);
//                    }
                }
            }
        });


        // String html = doc.toString();
    }

    public static void download(String file) {

    }

    public static void main(String[] args) {
        // "data/urls.txt"
        /*
            html结构如下：
            <div class="tip">
             Tips：点击图片进入下一页&amp;长按图片存图
            </div>
            <script type="text/javascript">nei_top()</script>
            <div class="ArticleBox">
             <a class="" href="/neiyimeinv/29490_2.html"> </a>
             <p><a class="" href="/neiyimeinv/29490_2.html"><img alt="图片第1张" src="https://img.99ym.cn/d/fiel/20200516/sbpitp0ycch.jpg"></a> </p>
            </div>
        */
        String line = "https://m.raoniao.com/rentiyishu/32269.html";
        String prefix = line.substring(0, line.indexOf("/", 10));
        String nextUrl = "";
        boolean flag = false;  // 标志，false 图片抓取失败，true 图片抓取成功
        // 计数器，读取网页失败则递增1，连续失败才会递增，如果第一次读取网页失败，第2次读取成功，则cnt归零
        // 计数器，记录抓取图片总数 total，抓取图片失败的次数 fail
        int success = 0, fail = 0;
        // 记录抓取成功的图片url
//        List<String> successfulPics = new ArrayList<>();
        // 记录抓取失败的图片url
        List<String> failedPics = new ArrayList<>();

        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(line), 20000);
        } catch (IOException e) {
            System.out.format("解析URL (%s) 失败%n", line);
            e.printStackTrace();
        }

        while (!line.equals(nextUrl) && fail < 6) {
            if (null != doc) {
                Elements children = doc.select("div.ArticleBox").first().children();
                for (Element element : children) {
                    Elements children2 = element.children();
                    if (!children2.isEmpty()) {
//                    System.out.println("children is not null.");
                        // Exception in thread "main" java.lang.IndexOutOfBoundsException: Index: 0, Size: 0
                        // 这里报错，还要根据返回的html对象格式做一些判断
                        String imgUrl = children2.first().child(0).attr("src");
                        nextUrl = prefix + children2.first().attr("href");
                        try {
                            download(imgUrl);
                        } catch (Exception e) {
                            System.out.println("下载图片失败！图片URL：" + imgUrl);
                            failedPics.add(imgUrl);
                        }
//                        successfulPics.add(imgUrl);
                        System.out.println("下载图片成功！图片URL：" + imgUrl);
                        success++;
                        flag = true;
                        break;
                    }
                }
//            String imageUrl = doc.select("div.ArticleBox").first().child(1).child(0).child(0).attr("src");
//            System.out.println(imageUrl);
            }

            if (!flag) {
                fail++;
            }

            flag = false;  // 重置flag为false

            try {
                doc = Jsoup.parse(new URL(nextUrl), 20000);
//                System.out.println(doc);
            } catch (IOException e) {
                System.out.format("解析URL (%s) 失败%n", nextUrl);
                e.printStackTrace();
            }
        }

        System.out.format("抓取图片总数：%d，其中成功数：%d，失败数：%d", success + fail, success, fail);
    }
}
