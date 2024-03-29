package com.ian.util.threadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class Test2Callable implements Callable<List<String>> {
//    private IReportBackgroundService iReportBackgroundService = ApplicationContextProvider.getBean(ReportBackgroundService.class);
    private String reportYear;
    private String reportNo;

    public Test2Callable(String reportYear, String reportNo) {
        this.reportYear = reportYear;
        this.reportNo = reportNo;
    }

    @Override
    public List<String> call() throws Exception {
        List<String> testList = new ArrayList<>(8);
        testList.add(reportYear + "_for_test_2");
        testList.add(reportNo + "_for_test_2");
//        System.out.println(testList);
        return testList;
    }
}
