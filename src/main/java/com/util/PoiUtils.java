package com.util;

import com.util.constants.Constants;
import com.util.entity.Report;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PoiUtils {

    /**
     * methods of creating workbook:
     * 1. HSSFWorkbook excelFile = new HSSFWorkbook();  // generate a xls file
     * 2. XSSFWorkbook excelFile = new XSSFWorkbook();  // generate a xlsx file
     * 3. HSSFWorkbook excelFile = new HSSFWorkbook(new ByteArrayInputStream(new ByteArrayOutputStream().toByteArray()))
     */
    public void exportToExcel() {
        OutputStream outputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream("D:\\develop\\exportToXls.xls");
            outputStream = new BufferedOutputStream(fileOutputStream);
            byte[] bytes = exportToXls();
            outputStream.write(bytes);
            outputStream.flush();
            exportToXlsx();
            // method 3 of creating workbook
            createWorkbookAndSheets();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != fileOutputStream)
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            if (null != outputStream)
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    public void exportToXlsx(){
        //字体
        String font_Name = "微软雅黑";
        int rowIndex = 0;
        // create a blank XSSFWorkbook
        XSSFWorkbook excelFile = new XSSFWorkbook();
        //Create a blank XSSFSheet
        Sheet sheet = null;
        // XSSFWorkbook不能像HSSFWorkbook那样获取自定义调色盘
//        setColor(HSSFColor.BLUE_GREY.index, (byte) 221, (byte) 235, (byte) 247, excelFile);
//        setColor(HSSFColor.BLUE.index, (byte) 68, (byte) 114, (byte) 196, excelFile);
//        setColor(HSSFColor.TURQUOISE.index, (byte) 99, (byte) 199, (byte) 235, excelFile);
        try (FileOutputStream fos = new FileOutputStream(new File("D:\\develop\\exportToXlsx.xlsx"))) {
            sheet = excelFile.createSheet("年鉴报告");
            rowIndex++;
            sheet.createRow(rowIndex);

            //添加大标题
            addBigExcelTitle(excelFile, sheet, font_Name, rowIndex, Constants.REPORT_NAME);

            //表格标题头部
            String[] titles = {"序号", "报告信息", "本年度"};

            CellStyle headerStyle = createHeaderStyle(excelFile, font_Name);
            CellStyle dataCellStyle = createDataCellStyle(excelFile, font_Name);
            CellStyle titleStyle = createTitleStyle(excelFile, font_Name);

            //参加调研的样本数量
            List<Report> list = new ArrayList<>();
            Report r1 = new Report("001","报告名称", "2020 Industry Report");
            Report r2 = new Report("002", "样本数量（单位：户）", "161");
            Report r3 = new Report("003", "职工人数（单位：人）", "35,610");
            Report r4 = new Report("004", "领导人数（单位：人）", "358");

            list.add(r1);
            list.add(r2);
            list.add(r3);
            list.add(r4);

            if (!CollectionUtils.isEmpty(list))
                rowIndex = initExcel(rowIndex, list, sheet, headerStyle, dataCellStyle,
                        titleStyle, Constants.REPORT_TITLE, titles, false);

            excelFile.write(fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] exportToXls(){
        //字体
        String font_Name = "微软雅黑";
        int rowIndex = 0;
        HSSFWorkbook excelFile = new HSSFWorkbook();
        //设置目录sheet
        Sheet sheet;
        String sheetName = "年鉴报告";
        byte[] bytes = null;
        setColor(HSSFColor.BLUE_GREY.index, (byte) 221, (byte) 235, (byte) 247, excelFile);
        setColor(HSSFColor.BLUE.index, (byte) 68, (byte) 114, (byte) 196, excelFile);
        setColor(HSSFColor.TURQUOISE.index, (byte) 99, (byte) 199, (byte) 235, excelFile);
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            sheet = excelFile.createSheet(sheetName);
            rowIndex++;
            sheet.createRow(rowIndex);

            //添加大标题
            addBigExcelTitle(excelFile, sheet, font_Name, rowIndex, Constants.REPORT_NAME);

            //表格标题头部
            String[] titles = {"序号", "报告信息", "本年度"};

            CellStyle headerStyle = createHeaderStyle(excelFile, font_Name);
            CellStyle dataCellStyle = createDataCellStyle(excelFile, font_Name);
            CellStyle titleStyle = createTitleStyle(excelFile, font_Name);

            //参加调研的样本数量
            List<Report> list = new ArrayList<>();
            Report r1 = new Report("1","报告名称", "2020 Industry Report");
            Report r2 = new Report("2", "样本数量（单位：户）", "161");
            Report r3 = new Report("3", "职工人数（单位：人）", "35,610");
            Report r4 = new Report("4", "领导人数（单位：人）", "358");

            list.add(r1);
            list.add(r2);
            list.add(r3);
            list.add(r4);

            if (!CollectionUtils.isEmpty(list))
                rowIndex = initExcel(rowIndex, list, sheet, headerStyle, dataCellStyle,
                        titleStyle, Constants.REPORT_TITLE, titles, false);

            excelFile.write(os);
            bytes = os.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    /**
     * 1. method 3 of creating workbook
     * 2. workbook add sheets
     */
    public void createWorkbookAndSheets(){

        HSSFWorkbook excelFile;
        HSSFWorkbook workbook;
        ByteArrayOutputStream baos;

        try {
            baos = new ByteArrayOutputStream();
            workbook = new HSSFWorkbook();
            workbook.createSheet("sheet1");
            workbook.createSheet("sheet2");
            workbook.write(baos);
            byte[] ba = baos.toByteArray();

            ByteArrayInputStream bais = new ByteArrayInputStream(ba);
            excelFile = new HSSFWorkbook(bais);

            OutputStream outputStream = new FileOutputStream(new File("D:\\study\\java\\poi\\createWorkbookAndSheets.xls"));
            excelFile.write(outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setColor(short index, byte r, byte g, byte b, HSSFWorkbook excelFile) {
        HSSFPalette palette = excelFile.getCustomPalette();
        HSSFColor hssfColor;
        try {
            hssfColor = palette.findColor(r, g, b);
            if (hssfColor == null) {
                palette.setColorAtIndex(index, r, g, b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addBigExcelTitle(Workbook excelFile, Sheet sheet, String font_name, int rowIndex, String reportName) {
        //大标题
        Font font_320 = excelFile.createFont();
        font_320.setFontHeightInPoints((short) 16);
        font_320.setFontName(font_name);
        font_320.setColor(HSSFColor.BLACK.index);
        font_320.setBold(true);

        CellStyle cellStyle_BigTitle = excelFile.createCellStyle();
        cellStyle_BigTitle.setFont(font_320);
        cellStyle_BigTitle.setAlignment(HorizontalAlignment.CENTER);

        //添加大标题
        Cell cell = sheet.getRow(rowIndex).createCell(0);
        cell.setCellStyle(cellStyle_BigTitle);
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, 2));
        cell.setCellStyle(cellStyle_BigTitle);
        cell.setCellValue(Constants.REPORT_NAME);

        sheet.setColumnWidth(0, 7000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 7000);
    }

    private CellStyle createHeaderStyle(Workbook excelFile, String font_name) {
        Font font_w = excelFile.createFont();
        font_w.setFontName(font_name);
        font_w.setColor(HSSFColor.WHITE.index);
        font_w.setFontHeightInPoints((short) 10);
        font_w.setBold(true);

        /*if (excelFile instanceof HSSFWorkbook) {
            HSSFWorkbook workbook = (HSSFWorkbook) excelFile;
            HSSFCellStyle cellStyle_head = workbook.createCellStyle();
        }else{
            XSSFWorkbook workbook = (XSSFWorkbook) excelFile;
            workbook.createCellStyle();
        }*/

        CellStyle cellStyle_head = excelFile.createCellStyle();
        //设置字体
        cellStyle_head.setFont(font_w);
        initHeaderStyle(cellStyle_head);
        return cellStyle_head;
    }

    private void initHeaderStyle(CellStyle cellStyle_1) {
        //设置表头单元格字体颜色
        //设置是否换行
        cellStyle_1.setWrapText(true);
        //设置表头单元格水平对齐
        cellStyle_1.setAlignment(HorizontalAlignment.CENTER);
        //设置表头单元格垂直对齐
        cellStyle_1.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置表头单元格边框样式
        cellStyle_1.setBorderTop(BorderStyle.THIN);
        cellStyle_1.setTopBorderColor(HSSFColor.TURQUOISE.index);
        //设置表头单元格背景色
        cellStyle_1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle_1.setFillForegroundColor(HSSFColor.BLUE.index);
    }

    private CellStyle createDataCellStyle(Workbook excelFile, String font_name) {
        Font font_c = excelFile.createFont();
        font_c.setFontName(font_name);
        font_c.setFontHeightInPoints((short) 10);
        CellStyle cellStyle_1 = excelFile.createCellStyle();
        //设置字体
        cellStyle_1.setFont(font_c);
        //设置单元格水平对齐
        cellStyle_1.setAlignment(HorizontalAlignment.CENTER);
        //设置单元格垂直对齐
        cellStyle_1.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置单元格边框样式
        //设置表头单元格背景色
        cellStyle_1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle_1.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
        return cellStyle_1;
    }

    private CellStyle createTitleStyle(Workbook excelFile, String font_name) {
        Font font = excelFile.createFont();
        font.setFontName(font_name);
        font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 10);
        font.setBold(true);
        CellStyle cellStyle = excelFile.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(HorizontalAlignment.LEFT);
        return cellStyle;
    }

    private void createExcelTitle(Integer colSize, String title, Sheet sheet, int rowIndex, CellStyle cellStyle) {
        sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, colSize - 1));
        Cell cell1 = sheet.getRow(rowIndex).createCell(0);
        cell1.setCellValue(title);
        cell1.setCellStyle(cellStyle);
    }

    private void createHead(Integer colSize, String[] titles, Sheet sheet, int rowIndex, CellStyle headerStyle) {
        for (int i = 0; i < titles.length; i++) {
            createExcelHead(i, colSize - 1, titles[i], sheet, rowIndex, headerStyle);
        }
    }

    private void createExcelHead(int headerCellIndex, Integer dataSize, String headTitle, Sheet sheet, int rowIndex, CellStyle cellStyle_1) {
        if (headerCellIndex == 0) {
            cellStyle_1.setBorderLeft(BorderStyle.THIN);
            cellStyle_1.setLeftBorderColor(HSSFColor.TURQUOISE.index);
        }
        if (headerCellIndex == dataSize) {
            cellStyle_1.setBorderRight(BorderStyle.THIN);
            cellStyle_1.setRightBorderColor(HSSFColor.TURQUOISE.index);
        }
        //设置表头单元格内容
        Cell cell2 = sheet.getRow(rowIndex).createCell(headerCellIndex);
        cell2.setCellValue(headTitle);
        sheet.getRow(rowIndex).setHeight((short) 600);
        cell2.setCellStyle(cellStyle_1);
    }

    private int addDataExcel(List<Report> list, Integer colSize, int dataSize, int rowIndex, CellStyle dataCellStyle, Sheet sheet) {
        //设置数据行
        // colSize => titles.length; dataSize => list.size()
        for (int idx = 0; idx < dataSize; idx++) {
            rowIndex++;
            sheet.createRow(rowIndex);
            for (int colIdx = 0; colIdx < colSize; colIdx++) {
                if (colIdx == 0) {
                    dataCellStyle.setBorderLeft(BorderStyle.THIN);
                    dataCellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
                }
                if (colIdx == colSize - 1) {
                    dataCellStyle.setBorderRight(BorderStyle.THIN);
                    dataCellStyle.setRightBorderColor(HSSFColor.BLACK.index);
                }
//                if (idx == colSize - 1) {
//                    dataCellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
//                }
                dataCellStyle.setBorderTop(BorderStyle.THIN);
                dataCellStyle.setTopBorderColor(HSSFColor.BLACK.index);
                dataCellStyle.setBorderBottom(BorderStyle.THIN);
                dataCellStyle.setBottomBorderColor(HSSFColor.BLACK.index);

                dataCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                dataCellStyle.setFillForegroundColor(HSSFColor.BLUE_GREY.index);
                //设置数据行单元格内容
                Cell cell = sheet.getRow(rowIndex).createCell(colIdx);
                cell.setCellStyle(dataCellStyle);
//                String value = getCellValue(colSize, list.get(rowIndex), cellIndex);
                String value = getCellValue(list.get(idx), colIdx);
                cell.setCellValue(value);
            }
        }

        return rowIndex;
    }

    private String getCellValue(Report report, int cellIndex) {
        String value = "";
        switch (cellIndex) {
            case 0:
                value = report.getNo();
                break;
            case 1:
                value = report.getReportType();
                break;
            case 2:
                value = report.getReportTypeContent();
                break;
            default:
                break;
        }
        return value;
    }

    private int initExcel(int rowIndex, List<Report> list, Sheet sheet, CellStyle headerStyle, CellStyle dataCellStyle,
                          CellStyle titleStyle, String title, String[] titles, boolean firstCellMerge) {
        int dataSize = list.size();
        Integer colSize = titles.length;
        rowIndex += 2;
        sheet.createRow(rowIndex);
        //设置标题
        createExcelTitle(colSize, title, sheet, rowIndex, titleStyle);
        //换行
        rowIndex++;
        sheet.createRow(rowIndex);

        //添加数据
        createHead(colSize, titles, sheet, rowIndex, headerStyle);
        rowIndex = addDataExcel(list, colSize, dataSize, rowIndex, dataCellStyle, sheet);
        //换行
        rowIndex += 2;
        //再次添加同样的数据
        rowIndex = addDataExcel(list, colSize, dataSize, rowIndex, dataCellStyle, sheet);
//        rowIndex += dataSize;
        /*//第一列进行合并单元格
        if (firstCellMerge) {
            firstExcelCellMerge(rowIndex, sheet, list.size(), list, dataCellStyle);
        }*/
        return rowIndex;
    }

    // TODO: merger some cells of a column
    /*private void firstExcelCellMerge(int rowIndex, Sheet sheet, int dataSize, List<?> list, CellStyle dataCellStyle) {
        int ksRows = 0;
        int jsRows = 0;
        ksRows = rowIndex - (dataSize - 1);
        jsRows = ksRows + 1;
        int dgLenght = dataSize / 2;
        String[] cellList = new String[dataSize / 2];
        for (int i = 0; i < dgLenght; i++) {
            ReportMultiRangeDTO data = (ReportMultiRangeDTO) list.get(i * 2);
            cellList[i] = data.getReportTypeName();
            sheet.addMergedRegion(new CellRangeAddress(ksRows, jsRows, 0, 0));
            Cell cell = sheet.getRow(ksRows).createCell(0);
            cell.setCellValue(cellList[i]);
            cell.setCellStyle(dataCellStyle);
            ksRows += 2;
            jsRows = ksRows + 1;
        }
    }*/
}
