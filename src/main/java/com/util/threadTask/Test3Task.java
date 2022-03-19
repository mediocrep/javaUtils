package com.util.threadTask;

import java.util.ArrayList;
import java.util.List;

public class Test3Task {

    private static int counter = 2020;
    private Integer reportYear;
    private String reportNo;

    public Integer getReportYear() {
        return reportYear;
    }

    public void setReportYear(Integer reportYear) {
        this.reportYear = reportYear;
    }

    public String getReportNo() {
        return reportNo;
    }

    public void setReportNo(String reportNo) {
        this.reportNo = reportNo;
    }

    public Test3Task(Integer reportYear, String reportNo) {
        this.reportYear = reportYear;
        this.reportNo = reportNo;
    }

    public static Test3Task getTest3Task(Test3Task test3Task) {
        List<String> testList = new ArrayList<>();
        testList.add(test3Task.reportYear + "_for_test_3");
        testList.add(test3Task.reportNo + "_for_test_3");
        System.out.println(testList);
        return new Test3Task(test3Task.reportYear + 1, test3Task.reportNo + " C ");
    }
}
