package com.util.m3u8.main;

import com.fasterxml.jackson.databind.JsonNode;
import com.util.JsonUtils;
import com.util.m3u8.download.M3u8DownloadFactory;
import com.util.m3u8.listener.DownloadListener;
import com.util.m3u8.utils.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Witt
 * @email zhangwei3583@126.com
 * @date 2022/03/20 18:00
 */

@Slf4j
public class M3u8Main {
    private static final Formatter FORMATTER = new Formatter(new StringBuilder());

    /*
    把要下载的m3u8链接放在这里。
    但是现在 M3U8URL 变量不用了，之前是手动从网页中查找m3u8的URL，然后赋值给变量 M3U8URL，这种方法一次只能下载一个视频。
    我修改了程序，直接解析网页的URL，做2件事：1 获取m3u8的URL，存入集合；2 获取下一集电视剧的网页URL。这样依次遍历到最后一集的网页URL。
    然后就遍历m3u8的URL集合，下载所有的视频。这样就实现了一次自动下载整个电视连续剧的功能。
     */
    // private static final String M3U8URL = "https://youku.cdn-56.com/20180109/2SwCGxb4/index.m3u8";
    // private static final String M3U8URL = "https://laoya2.77lehuo.com/20211019/Ws8jKkDh5/index.m3u8";
    // private static final String M3U8URL = "https://laoya2.77lehuo.com/20210926/ZgZ6sDHk2/index.m3u8";
    // private static final String M3U8URL = "https://laoya2.77lehuo.com/20210419/f3042IZH8/index.m3u8";
    // private static final String M3U8URL = "https://vod.hjbfq.com/20210421/dZ7D7upj6/index.m3u8";
    // private static final String M3U8URL = "https://laoya2.77lehuo.com/20210720/ZeFYr03491/index.m3u8";
    // private static final String M3U8URL = "https://laoya.77lehuo.com/20210417/W7dmFwfo3/index.m3u8";
    // private static final String M3U8URL = "https://laoya2.77lehuo.com/20211018/D9Tp5MXs2/index.m3u8";
    private static final String M3U8URL = "https://laoya2.77lehuo.com/20210928/3D8NvO0C6/index.m3u8";

    private static final String HOST_RENSHIJIAN = "https://sx0372.com";
    private static final String NAME_RENSHIJIAN = "RENSHIJIAN";
    private static final String SUFFIX_RENSHIJIAN = ".mp4"; // 工具类的下载方法中自动补上了后缀名 “.mp4”
    private static final String URL_1_RENSHIJIAN = "https://sx0372.com/vodplay/104803-2-%s.html";

    public static void main(String[] args) {
        List<Map<String, String>> m3u8UrlList = new ArrayList<>();
        int counter = 53;
        String link_next = FORMATTER.format(URL_1_RENSHIJIAN, counter).toString();
        String m3u8Url = "";
        Document doc = null;
        while (!HOST_RENSHIJIAN.equals(link_next) && !(HOST_RENSHIJIAN + "null").equals(link_next)
                && counter <= 53) {
            log.info("link_{}: {}", counter, link_next);
            /*
            原始url示例：https://sx0372.com/vodplay/104803-2-1.html
            要从url中提取的内容示例如下：
            <script type="text/javascript">var player_aaaa={"flag":"play","encrypt":0,"trysee":0,"points":0,"link":"\/vodplay\/11112-1-1.html","link_next":"","link_pre":"\/vodplay\/104803-2-57.html","url":"https:\/\/ukzy.ukubf3.com\/20220301\/8Oo9lD60\/index.m3u8","url_next":"","from":"ukm3u8","server":"no","note":"","id":"104803","sid":2,"nid":58}</script><script type="text/javascript" src="/static/js/playerconfig.js?t=20220319"></script><script type="text/javascript" src="/static/js/player.js?t=a20220319"></script>
            <script type="text/javascript">var player_aaaa={"flag":"play","encrypt":0,"trysee":0,"points":0,"link":"\/vodplay\/11112-1-1.html","link_next":"\/vodplay\/104803-2-58.html","link_pre":"\/vodplay\/104803-2-56.html","url":"https:\/\/ukzy.ukubf3.com\/20220301\/4p9nu77D\/index.m3u8","url_next":"https:\/\/ukzy.ukubf3.com\/20220301\/8Oo9lD60\/index.m3u8","from":"ukm3u8","server":"no","note":"","id":"104803","sid":2,"nid":57}</script><script type="text/javascript" src="/static/js/playerconfig.js?t=20220319"></script><script type="text/javascript" src="/static/js/player.js?t=a20220319"></script>

            真正要提取的内容是： https:\/\/ukzy.ukubf3.com\/20220301\/8Oo9lD60\/index.m3u8
            然后去掉里面的反斜杠，得到最终的m3u8Url: https://ukzy.ukubf3.com/20220301/8Oo9lD60/index.m3u8
            最后，把最终的m3u8Url存入m3u8UrlList
             */
            try {
                doc = Jsoup.parse(new URL(link_next), 20000);

                // 这一段代码是解析网页，获取m3u8Url 和 下一个视频的网址。
                //   根据网页的不同，可能会有差异，到时候可能需要根据实际情况进行调整
                Optional<String> optional = doc.select("script").stream()
                        .filter(ele -> ele.toString().contains("player_aaaa"))
                        .map(ele -> ele.html().split("=")[1])
                        // .forEach(System.out::println);
                        .findFirst();

                if (optional.isPresent()) {
                    JsonNode jsonNode = JsonUtils.jsonStrToJsonNode(optional.get());
                    Map<String, String> map = new HashMap<>();
                    map.put("fileName", NAME_RENSHIJIAN + counter);
                    map.put("m3u8Url", StringUtils.trim(jsonNode.get("url").asText())); //.replace("https", "http"));
                    m3u8UrlList.add(map);
                    // String url = FORMATTER.format(URL_1_RENSHIJIAN, i).toString();
                    link_next = HOST_RENSHIJIAN + StringUtils.trim(jsonNode.get("link_next").asText());
                }

                counter++;
            } catch (IOException e) {
                System.out.format("解析url (%s) 失败%n", link_next);
                log.error(e.toString());
                return;
            }
        }

        /*
        这里保留了下载一个 m3u8 视频的功能，如果要用这个功能，需要放开这段注释，然后把上面的生成m3u8 URL 集合的代码注释掉
        Map<String, String> map = new HashMap<>();
        map.put("fileName", NAME_RENSHIJIAN + 13);
        map.put("m3u8Url", "http://vod1.bdzybf1.com/20220223/5tfiM73A/index.m3u8");
        m3u8UrlList.add(map);
        */
        m3u8UrlList.forEach(System.out::println);

        m3u8UrlList.forEach(m3u8 -> {
            //log.info("handle m3u8 url: {}", "http://vod1.bdzybf1.com/20220223/3wxVeNDG/index.m3u8");
            log.info("handle m3u8 url: {}", m3u8.get("m3u8Url"));
            //M3u8DownloadFactory.M3u8Download m3u8Download = M3u8DownloadFactory.getInstance("http://vod1.bdzybf1.com/20220223/3wxVeNDG/index.m3u8");
            M3u8DownloadFactory.M3u8Download m3u8Download = M3u8DownloadFactory.getInstance(m3u8.get("m3u8Url"));
            //设置生成目录
            String downloadDir = "/home/witt/download/m3u8/" + m3u8.get("fileName");
            //m3u8Download.setDir("/home/witt/download/m3u8");
            m3u8Download.setDir(downloadDir);
            //设置视频名称
            //m3u8Download.setFileName("人世间1");
            m3u8Download.setFileName(m3u8.get("fileName"));
            //设置线程数
            m3u8Download.setThreadCount(100);
            //设置重试次数
            m3u8Download.setRetryCount(80);
            //设置连接超时时间（单位：毫秒）
            m3u8Download.setTimeoutMillisecond(10000L);
            /*
            设置日志级别
            可选值：NONE INFO DEBUG ERROR
            */
            m3u8Download.setLogLevel(Constant.INFO);
            //设置监听器间隔（单位：毫秒）
            m3u8Download.setInterval(500L);
            //添加额外请求头
            /*  Map<String, Object> headersMap = new HashMap<>();
            headersMap.put("Content-Type", "text/html;charset=utf-8");
            m3u8Download.addRequestHeaderMap(headersMap);*/
            //如果需要的话设置http代理
            //m3u8Download.setProxy("172.50.60.3",8090);
            //添加监听器
            m3u8Download.addListener(new DownloadListener() {
                @Override
                public void start() {
                    System.out.println("开始下载！");
                }

                @Override
                public void process(String downloadUrl, int finished, int sum, float percent) {
                    System.out.println("下载网址：" + downloadUrl + "\t已下载" + finished + "个\t一共" + sum + "个\t已完成" + percent + "%");
                }

                @Override
                public void speed(String speedPerSecond) {
                    System.out.println("下载速度：" + speedPerSecond);
                }

                @Override
                public void end() {
                    System.out.println("下载完毕");
                }
            });
            //开始下载
            m3u8Download.start();
            // 必须等到第一个文件下载完毕，再下载下一个文件，否则多个文件的多个线程之间会相互影响，报错
            //      while条件：目标视频文件未生成，或者 ts文件分片未删除
            while (!Files.exists(Paths.get(downloadDir + File.separator + m3u8.get("fileName") + SUFFIX_RENSHIJIAN))
                    || Files.exists(Paths.get(downloadDir + File.separator + "1.xyz"))) {
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    log.error(e.toString());
                }
            }
        });

        // 将所有在子目录中的视频文件移动到上一层目录，然后删除子目录
        try {
            Files.walk(Paths.get("/home/witt/download/m3u8/"))
                    .filter(path -> Files.isRegularFile(path) && path.toString().endsWith(".mp4"))
                    .forEach(path -> {
                        try {
                            System.out.println("moving file '" + path + "' to its parent folder...");
                            Path targetPath = path.getParent().getParent().resolve(path.getFileName());
                            // 将子目录中的视频文件移动到上一层目录
                            Files.move(path, targetPath);
                            // 删除子目录
                            Files.delete(path.getParent());
                            log.info("将所有在子目录中的视频文件移动到上一层目录，然后删除子目录，成功！");
                        } catch (IOException e) {
                            log.error("将所有在子目录中的视频文件移动到上一层目录，然后删除子目录，失败！\n失败原因：{}", e.toString());
                        }
                    });
        } catch (IOException e) {
            log.error(e.toString());
        }
    }
}
