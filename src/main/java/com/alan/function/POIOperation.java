package com.alan.function;

import org.apache.poi.xssf.usermodel.*;

import java.io.*;

/**
 * Created by mengqinghao on 2018/3/23.
 * POI的相关操作
 */
public class POIOperation {

    public static void main(String[] args) {
        String path = "/Users/finup/Documents/商城类目映射-标注版.xlsx";
        try {
            readExcel(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readExcel(String path) throws IOException {
        File file = new File(path);
        InputStream is = new FileInputStream(file);// 获取文件输入流
        XSSFWorkbook workbook2007 = new XSSFWorkbook(is);// 创建Excel2007文件对象
        XSSFSheet sheet = workbook2007.getSheetAt(0);// 取出第一个工作表，索引是0
        int number = sheet.getLastRowNum();
        System.out.println(number);
        for(int i=2; i < sheet.getLastRowNum()+10; i++){
            XSSFRow row = sheet.getRow(i);
            if(row == null)
                break;
            XSSFCell oldCell =  row.getCell(2);
            XSSFCell newCell =  row.getCell(6);
            //XSSFCell isShow =  row.getCell(7);
            //XSSFColor oldColor = oldCell.getCellStyle().getFillForegroundColorColor();
            XSSFColor newColor = newCell.getCellStyle().getFillForegroundColorColor();
            //XSSFColor showColor = isShow.getCellStyle().getFillForegroundColorColor();
            if(newColor != null){
                if("FF00B0F0".equals(newColor.getARGBHex())){
                    String oldVal = oldCell.toString();
                    String id = oldVal.substring(0,oldVal.indexOf("-"));
                    System.out.println("update category set `name`='"+newCell+"' where id = "+id+";");
                    /*System.out.println("insert into category_rel_third(category_id,third_category_id,channel,enabled,del,create_time) values " +
                            "("+id+","+id+",1,0,0,'2018-03-23');");*/
                }
            }
            /*if(showColor != null && "FFFFC000".equals(showColor.getARGBHex()))
                System.out.println(oldCell+"<------->"+newCell+"<==>黄色"+showColor.getARGBHex()+"<-->"+isShow);*/
        }
    }

}
