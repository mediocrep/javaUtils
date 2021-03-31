package com.utils.entity;


public class Report {
    private String no;
    private String reportType;
    private String reportTypeContent;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getReportTypeContent() {
        return reportTypeContent;
    }

    public void setReportTypeContent(String reportTypeContent) {
        this.reportTypeContent = reportTypeContent;
    }

    public Report(String no, String reportType, String reportTypeContent) {
        this.no = no;
        this.reportType = reportType;
        this.reportTypeContent = reportTypeContent;
    }
}
