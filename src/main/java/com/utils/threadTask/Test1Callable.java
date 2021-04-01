package com.utils.threadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Test1Callable implements Callable<List<String>> {
//    private IReportBackgroundService iReportBackgroundService = ApplicationContextProvider.getBean(ReportBackgroundService.class);
    private String reportYear;
    private String reportNo;

    public Test1Callable(String reportYear, String reportNo) {
        this.reportYear = reportYear;
        this.reportNo = reportNo;
    }

    @Override
    public List<String> call() throws Exception {
        List<String> testList = new ArrayList<>(8);
        testList.add(reportYear + "_for_test_1");
        testList.add(reportNo + "_for_test_1");
//        System.out.println(testList);
        return testList;
    }
}
