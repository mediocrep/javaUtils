package com.utils;

import com.utils.entity.Report;
import com.utils.threadTask.Test1Callable;
import com.utils.threadTask.Test2Callable;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

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

}
