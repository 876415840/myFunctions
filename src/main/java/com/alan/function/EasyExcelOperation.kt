package com.alan.function

import com.alibaba.excel.ExcelWriter
import com.alibaba.excel.metadata.BaseRowModel
import com.alibaba.excel.metadata.Sheet
import com.alibaba.excel.support.ExcelTypeEnum
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletResponse
import java.io.IOException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date

/**
 * @Description easyexcel操作
 * @Author mengqinghao
 * @Date 2018/11/6 下午3:48
 * @Version 1.0
 */
object EasyExcelOperation {

    private val LOGGER = LoggerFactory.getLogger(EasyExcelOperation::class.java)

    /**
     * @Description: 导出
     * @author mengqinghao
     * @param: [list, response, clazz]
     * @date 2018/11/6 下午3:56
     * @return: void
     */
    fun export(list: List<BaseRowModel>, response: HttpServletResponse, clazz: Class<out BaseRowModel>) {


        var out: ServletOutputStream? = null

        try {

            out = response.outputStream

        } catch (e: IOException) {

            e.printStackTrace()

        }

        val writer = ExcelWriter(out, ExcelTypeEnum.XLSX, true)

        try {


            val fileName = String(SimpleDateFormat("yyyy-MM-dd").format(Date()).toByteArray(), "UTF-8")

            val sheet2 = Sheet(2, 3, clazz, "sheet", null)

            writer.write(list, sheet2)

            //response.setContentType("multipart/form-data");

            response.characterEncoding = "utf-8"

            response.contentType = "application/vnd.ms-excel"

            response.setHeader("content-Disposition", "attachment;filename=" + URLEncoder.encode("$fileName.xlsx", "utf-8"))

            //response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");

            out!!.flush()

            LOGGER.info("[easy excel export] success")

        } catch (e: Exception) {

            e.printStackTrace()

            LOGGER.info("[easy excel export] failed")

        } finally {

            writer.finish()

            try {

                out!!.close()

            } catch (e: IOException) {

                e.printStackTrace()

            }

        }

    }
}
