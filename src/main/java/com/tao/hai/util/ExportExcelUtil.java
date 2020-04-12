package com.tao.hai.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tao.hai.enums.ExcelTypeEnum;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author sunits
 * @date 20180912
 * @description 通用POI Excel导出工具类
 */
public class ExportExcelUtil {

    public static String NO_DEFINE = "no_define";//未定义的字段
    public static String DEFAULT_DATE_PATTERN = "yyyy年MM月dd日";//默认日期格式
    public static int DEFAULT_COLOUMN_WIDTH = 17;

    /**
     * 导出方法
     *
     * @param title     标题
     * @param headMap   导出表头
     * @param dataList  数据对象集合
     * @param excelType xls格式 xlxs格式
     * @param response  servlet响应对象
     */
    public static void exportMain(String title, Map<String, String> headMap, List<Object> dataList, String excelType, HttpServletResponse response) {
        try {
            OutputStream outputStream = response.getOutputStream();// 取得输出流
            response.reset();// 清空输出流
            response.setHeader("Content-disposition", "attachment; filename="
                    + ExportExcelUtil.GBToISO(title + DateUtils.getDate())
                    + ".xlsx");// 设定输出文件头
            response.setContentType("application/msexcel");// 定义输出类型
            /**根据格式导出*/
            if (ExcelTypeEnum.XLS.equals(excelType)) {
                ExportExcelUtil.exportExcel(title, headMap, listToJSONArray(dataList), null, 0, outputStream);
            } else {
                ExportExcelUtil.exportExcelX(title, headMap, listToJSONArray(dataList), null, 0, outputStream);
            }
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 转换为JSONArray集合
     */
    private static JSONArray listToJSONArray(List<Object> list) {
        JSONArray jSONArray = new JSONArray();
        /**转换为JSONArray*/
        for (Object t : list) {
            jSONArray.add(t);
        }
        return jSONArray;
    }

    /**
     * 导出Excel 97(.xls)格式 ，少量数据
     *
     * @param title       标题行
     * @param headMap     属性-列名
     * @param jsonArray   数据集
     * @param datePattern 日期格式，null则用默认日期格式
     * @param colWidth    列宽 默认 至少17个字节
     * @param out         输出流
     */
    public static void exportExcel(String title, Map<String, String> headMap, JSONArray jsonArray, String datePattern, int colWidth, OutputStream out) {
        if (datePattern == null) datePattern = DEFAULT_DATE_PATTERN;
        // 声明一个工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        workbook.createInformationProperties();
        workbook.getDocumentSummaryInformation().setCompany("*****公司");
        SummaryInformation si = workbook.getSummaryInformation();
        si.setAuthor("sunits");  //填加xls文件作者信息
        si.setApplicationName("导出程序"); //填加xls文件创建程序信息
        si.setLastAuthor("最后保存者信息"); //填加xls文件最后保存者信息
        si.setComments("sunits is a programmer!"); //填加xls文件作者信息
        si.setTitle("POI导出Excel"); //填加xls文件标题信息
        si.setSubject("POI导出Excel");//填加文件主题信息
        si.setCreateDateTime(new Date());
        //表头样式
        HSSFCellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        HSSFFont titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 20);
        titleFont.setBold(true);
        titleStyle.setFont(titleFont);
        // 列头样式
        HSSFCellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillPattern(FillPatternType.NO_FILL);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        HSSFFont headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        // 单元格样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillPattern(FillPatternType.NO_FILL);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        HSSFFont cellFont = workbook.createFont();
        cellFont.setBold(false);
        cellStyle.setFont(cellFont);
        // 生成一个(带标题)表格
        HSSFSheet sheet = workbook.createSheet();
        // 声明一个画图的顶级管理器
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
        // 定义注释的大小和位置,详见文档
        HSSFComment comment = patriarch.createComment(new HSSFClientAnchor(0,
                0, 0, 0, (short) 4, 2, (short) 6, 5));
        // 设置注释内容
        comment.setString(new HSSFRichTextString("可以在POI中添加注释！"));
        // 设置注释作者，当鼠标移动到单元格上是可以在状态栏中看到该内容.
        comment.setAuthor("sunits");
        //设置列宽
        int minBytes = colWidth < DEFAULT_COLOUMN_WIDTH ? DEFAULT_COLOUMN_WIDTH : colWidth;//至少字节数
        int[] arrColWidth = new int[headMap.size()];
        // 产生表格标题行,以及设置列宽
        String[] properties = new String[headMap.size()];
        String[] headers = new String[headMap.size()];
        int ii = 0;
        for (Iterator<String> iter = headMap.keySet().iterator(); iter
                .hasNext(); ) {
            String fieldName = iter.next();

            properties[ii] = fieldName;
            headers[ii] = fieldName;

            int bytes = fieldName.getBytes().length;
            arrColWidth[ii] = bytes < minBytes ? minBytes : bytes;
            sheet.setColumnWidth(ii, arrColWidth[ii] * 256);
            ii++;
        }
        // 遍历集合数据，产生数据行
        int rowIndex = 0;
        for (Object obj : jsonArray) {
            if (rowIndex == 65535 || rowIndex == 0) {
                if (rowIndex != 0) sheet = workbook.createSheet();//如果数据超过了，则在第二页显示

                HSSFRow titleRow = sheet.createRow(0);//表头 rowIndex=0
                titleRow.createCell(0).setCellValue(title);
                titleRow.getCell(0).setCellStyle(titleStyle);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headMap.size() - 1));

                HSSFRow headerRow = sheet.createRow(1); //列头 rowIndex =1
                for (int i = 0; i < headers.length; i++) {
                    headerRow.createCell(i).setCellValue(headers[i]);
                    headerRow.getCell(i).setCellStyle(headerStyle);

                }
                rowIndex = 2;//数据内容从 rowIndex=2开始
            }
            JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
            HSSFRow dataRow = sheet.createRow(rowIndex);
            for (int i = 0; i < properties.length; i++) {
                HSSFCell newCell = dataRow.createCell(i);
                addCell(jo, properties, i, datePattern, newCell, cellStyle);
            }
            rowIndex++;
        }
        // 自动调整宽度
//        for (int i = 0; i < headers.length; i++) {
//            sheet.autoSizeColumn(i);
//        }
        try {
            workbook.write(out);
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导出Excel 2007 OOXML (.xlsx)格式
     *
     * @param title       标题行
     * @param headMap     属性-列头
     * @param jsonArray   数据集
     * @param datePattern 日期格式，传null值则默认 年月日
     * @param colWidth    列宽 默认 至少17个字节
     * @param out         输出流
     */
    public static void exportExcelX(String title, Map<String, String> headMap, JSONArray jsonArray, String datePattern, int colWidth, OutputStream out) {
        if (datePattern == null) datePattern = DEFAULT_DATE_PATTERN;
        // 声明一个工作薄
        SXSSFWorkbook workbook = new SXSSFWorkbook(1000);//缓存
        workbook.setCompressTempFiles(true);
        //表头样式
        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setAlignment(HorizontalAlignment.CENTER);
        Font titleFont = workbook.createFont();
        titleFont.setFontHeightInPoints((short) 20);
        titleFont.setBold(true);
        titleStyle.setFont(titleFont);
        // 列头样式
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillPattern(FillPatternType.NO_FILL);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        // 单元格样式
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFillPattern(FillPatternType.NO_FILL);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        Font cellFont = workbook.createFont();
        cellFont.setBold(false);
        cellStyle.setFont(cellFont);
        // 生成一个(带标题)表格
        SXSSFSheet sheet = workbook.createSheet();
        //设置列宽
        int minBytes = colWidth < DEFAULT_COLOUMN_WIDTH ? DEFAULT_COLOUMN_WIDTH : colWidth;//至少字节数
        int[] arrColWidth = new int[headMap.size()];
        // 产生表格标题行,以及设置列宽
        String[] properties = new String[headMap.size()];
        String[] headers = new String[headMap.size()];
        int ii = 0;
        for (Iterator<String> iter = headMap.keySet().iterator(); iter
                .hasNext(); ) {
            String fieldName = iter.next();

            properties[ii] = fieldName;
            headers[ii] = headMap.get(fieldName);

            int bytes = fieldName.getBytes().length;
            arrColWidth[ii] = bytes < minBytes ? minBytes : bytes;
            sheet.setColumnWidth(ii, arrColWidth[ii] * 256);
            ii++;
        }
        // 遍历集合数据，产生数据行
        int rowIndex = 0;
        for (Object obj : jsonArray) {
            if (rowIndex == 65535 || rowIndex == 0) {
                if (rowIndex != 0) sheet = workbook.createSheet();//如果数据超过了，则在第二页显示

                SXSSFRow titleRow = sheet.createRow(0);//表头 rowIndex=0
                titleRow.createCell(0).setCellValue(title);
                titleRow.getCell(0).setCellStyle(titleStyle);
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, headMap.size() - 1));

                SXSSFRow headerRow = sheet.createRow(1); //列头 rowIndex =1
                for (int i = 0; i < headers.length; i++) {
                    headerRow.createCell(i).setCellValue(headers[i]);
                    headerRow.getCell(i).setCellStyle(headerStyle);

                }
                rowIndex = 2;//数据内容从 rowIndex=2开始
            }
            JSONObject jo = (JSONObject) JSONObject.toJSON(obj);
            SXSSFRow dataRow = sheet.createRow(rowIndex);
            for (int i = 0; i < properties.length; i++) {
                SXSSFCell newCell = dataRow.createCell(i);
                addCell(jo, properties, i, datePattern, newCell, cellStyle);
            }
            rowIndex++;
        }
        // 自动调整宽度
//        for (int i = 0; i < headers.length; i++) {
//            sheet.autoSizeColumn(i);
//        }
        try {
            workbook.write(out);
            workbook.close();
            workbook.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Web 导出excel
    public static void downloadExcelFile(String title, Map<String, String> headMap, JSONArray ja, HttpServletResponse response) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            exportExcelX(title, headMap, ja, null, 0, os);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();

            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((title + ".xlsx").getBytes(), StandardCharsets.ISO_8859_1));
            response.setContentLength(content.length);
            ServletOutputStream outputStream = response.getOutputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedOutputStream bos = new BufferedOutputStream(outputStream);
            byte[] buff = new byte[8192];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);

            }
            bis.close();
            bos.close();
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 加入单元格
     */
    public static void addCell(JSONObject jo, String[] properties, int i, String datePattern, Cell newCell, CellStyle cellStyle) {
        Object o = jo.get(properties[i]);
        String cellValue = "";
        if (o == null) {
            cellValue = "";
            newCell.setCellValue(cellValue);
        } else if (o instanceof Date) {
            cellValue = new SimpleDateFormat(datePattern).format(o);
            newCell.setCellValue(cellValue);
        } else if (o instanceof Float || o instanceof Double) {
            BigDecimal cellNumberValue = new BigDecimal(o.toString()).setScale(2, BigDecimal.ROUND_HALF_UP);
            newCell.setCellValue(cellNumberValue.doubleValue());
        } else if (o instanceof Long || o instanceof Integer) {
            BigDecimal cellNumberValue = new BigDecimal(o.toString()).setScale(0, BigDecimal.ROUND_HALF_UP);
            newCell.setCellValue(cellNumberValue.doubleValue());
        } else if (o instanceof Boolean) {
            boolean booleanValue = ((Boolean) o).booleanValue();
            newCell.setCellValue(booleanValue);
        } else {
            cellValue = o.toString();
            newCell.setCellValue(cellValue);
        }
        newCell.setCellStyle(cellStyle);
    }

    /***加入excel文件到压缩包zip
     * @param excelFile 文件
     * @param zipOutputStream zip文件
     * */
    public static void addFileToZip(File excelFile, ZipOutputStream zipOutputStream) throws IOException {
        int BUFFER = 2048;// 预设缓冲区成员（位元阵列）为2048元组
        byte[] data = new byte[BUFFER];// 获得一个字节数组
        // 下面是压缩文件到zip文件中
        FileInputStream fi = new FileInputStream(excelFile);// 通过路径数组来创建文件写入流
        BufferedInputStream origin = new BufferedInputStream(fi, BUFFER);// 实例化一个有缓冲区大小的输入流
        ZipEntry entry = new ZipEntry(excelFile.getName());// 通过指定的名称创建ZIP文件
        //entry.setUnixMode(644);// 解决linux乱码
        zipOutputStream.putNextEntry(entry);// 输出ZIP文件
        int count;// 设置变量
        // 将输入流中最多BUFFER个数据字节读入字节数组。以整数形式返回实际读取的字节数，若到达末尾没有数据了，返回-1
        while ((count = origin.read(data, 0, BUFFER)) != -1) {
            zipOutputStream.write(data, 0, count);// 输出ZIP数据
        }
        origin.close();// 关闭输入流
        excelFile.delete();
    }

    public static void main(String[] args) throws IOException {
        int count = 100000;
        JSONArray ja = new JSONArray();
        for (int i = 0; i < 100000; i++) {
//            Student s = new Student();
//            s.setName("POI"+i);
//            s.setAge(i);
//            s.setBirthday(new Date());
//            s.setHeight(i);
//            s.setWeight(i);
//            s.setSex(i/2==0?false:true);
//            ja.add(s);
        }
        Map<String, String> headMap = new LinkedHashMap<String, String>();
        headMap.put("name", "姓名");
        headMap.put("age", "年龄");
        headMap.put("birthday", "生日");
        headMap.put("height", "身高");
        headMap.put("weight", "体重");
        headMap.put("sex", "性别");

        String title = "测试";
        /*
        OutputStream outXls = new FileOutputStream("E://a.xls");
        System.out.println("正在导出xls....");
        Date d = new Date();
        ExcelUtil.exportExcel(title,headMap,ja,null,outXls);
        System.out.println("共"+count+"条数据,执行"+(new Date().getTime()-d.getTime())+"ms");
        outXls.close();*/
        //
        OutputStream outXlsx = new FileOutputStream("E://b.xlsx");
        System.out.println("正在导出xlsx....");
        Date d2 = new Date();
        ExportExcelUtil.exportExcelX(title, headMap, ja, null, 0, outXlsx);
        System.out.println("共" + count + "条数据,执行" + (new Date().getTime() - d2.getTime()) + "ms");
        outXlsx.close();

    }

    /**
     * 转换中文输入
     */
    public static String GBToISO(String gb) {
        try {
            return gb == null ? gb : new String(gb.getBytes("GB2312"),
                    StandardCharsets.ISO_8859_1);
        } catch (Exception e) {
            return gb;
        }
    }
}
