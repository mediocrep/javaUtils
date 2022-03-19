package com.util;

import com.util.threadTask.Test1Callable;
import com.util.threadTask.Test2Callable;
import com.util.threadTask.Test3Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadUtils {

    private static Logger logger = LoggerFactory.getLogger(ThreadUtils.class);

    public static List<List<String>> getDataList() {
        int threadPoolCount = 3;
        String reportYear = "2020";
        String reportNo = "RP02-200100000002";
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(threadPoolCount);
        List<List<String>> dataList = new ArrayList<>();
        List<Future> futureList = new ArrayList<>();
        Future future;

        // thread1
        Test1Callable test1Callable = new Test1Callable(reportYear, reportNo);
        future = executor.submit(test1Callable);
        futureList.add(future);
        // thread2
        Test2Callable test2Callable = new Test2Callable(reportYear, reportNo);
        future = executor.submit(test2Callable);
        futureList.add(future);

        try {
            for (Future futureItem : futureList) {
                List<String> testList = (List<String>) futureItem.get();
                dataList.add(testList);
            }
        } catch (ExecutionException | InterruptedException e) {
            logger.error("", e);
        }
        return dataList;
    }

    public static void getDataList2(Test3Task test3Task) {
        MyTimer timer = new MyTimer();
        CompletableFuture<Test3Task> completableFuture = CompletableFuture.completedFuture(test3Task)
                .thenApplyAsync(Test3Task::getTest3Task)
                .thenApplyAsync(Test3Task::getTest3Task)
                .thenApplyAsync(Test3Task::getTest3Task);
        System.out.println(timer.duration());
        System.out.println(completableFuture.join());  // 返回 completableFuture 对象内部的 Task3Task 对象
        System.out.println(timer.duration());
    }

    public static List<List<String>> getStringList(Test3Task test3Task5, Test3Task test3Task6, Test3Task test3Task7) {

        List<List<String>> testListList = new ArrayList<>();
        List<String> testList = new ArrayList<>();
        List<String> testList2 = new ArrayList<>();
        List<String> testList3 = new ArrayList<>();
        final CompletableFuture<Test3Task> completableFuture1 = CompletableFuture.completedFuture(test3Task5).thenApplyAsync(Test3Task::getTest3Task);
        final CompletableFuture<Test3Task> completableFuture2 = CompletableFuture.completedFuture(test3Task6).thenApplyAsync(Test3Task::getTest3Task);
        final CompletableFuture<Test3Task> completableFuture3 = CompletableFuture.completedFuture(test3Task7).thenApplyAsync(Test3Task::getTest3Task);

        final Test3Task test3Task1 = completableFuture1.join();
        testList.add(test3Task1.getReportYear() + "_for_test_1");
        testList.add(test3Task1.getReportNo() + "_for_test_1");
        testListList.add(testList);

        final Test3Task test3Task2 = completableFuture2.join();
        testList2.add(test3Task2.getReportYear() + "_for_test_2");
        testList2.add(test3Task2.getReportNo() + "_for_test_2");
        testListList.add(testList2);

        final Test3Task test3Task3 = completableFuture3.join();
        testList3.add(test3Task3.getReportYear() + "_for_test_3");
        testList3.add(test3Task3.getReportNo() + "_for_test_3");
        testListList.add(testList3);

        return testListList;
    }

}
