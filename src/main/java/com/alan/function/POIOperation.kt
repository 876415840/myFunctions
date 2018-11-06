package com.alan.function

import org.apache.poi.xssf.usermodel.*

import java.io.*

/**
 * Created by mengqinghao on 2018/3/23.
 * POI的相关操作
 */
object POIOperation {

    @JvmStatic
    fun main(args: Array<String>) {
        val path = "/Users/finup/Documents/商城类目映射-标注版.xlsx"
        try {
            readExcel(path)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    @Throws(IOException::class)
    fun readExcel(path: String) {
        val file = File(path)
        val `is` = FileInputStream(file)// 获取文件输入流
        val workbook2007 = XSSFWorkbook(`is`)// 创建Excel2007文件对象
        val sheet = workbook2007.getSheetAt(0)// 取出第一个工作表，索引是0
        val number = sheet.lastRowNum
        println(number)
        for (i in 2 until sheet.lastRowNum + 10) {
            val row = sheet.getRow(i) ?: break
            val oldCell = row.getCell(2)
            val newCell = row.getCell(6)
            //XSSFCell isShow =  row.getCell(7);
            //XSSFColor oldColor = oldCell.getCellStyle().getFillForegroundColorColor();
            val newColor = newCell.cellStyle.fillForegroundColorColor
            //XSSFColor showColor = isShow.getCellStyle().getFillForegroundColorColor();
            if (newColor != null) {
                if ("FF00B0F0" == newColor.argbHex) {
                    val oldVal = oldCell.toString()
                    val id = oldVal.substring(0, oldVal.indexOf("-"))
                    println("update category set `name`='$newCell' where id = $id;")
                    /*System.out.println("insert into category_rel_third(category_id,third_category_id,channel,enabled,del,create_time) values " +
                            "("+id+","+id+",1,0,0,'2018-03-23');");*/
                }
            }
            /*if(showColor != null && "FFFFC000".equals(showColor.getARGBHex()))
                System.out.println(oldCell+"<------->"+newCell+"<==>黄色"+showColor.getARGBHex()+"<-->"+isShow);*/
        }
    }

}
