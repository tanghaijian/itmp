package cn.pioneeruniverse.report.service;

import java.io.InputStream;
import java.util.List;

import com.bstek.ureport.provider.report.ReportFile;

public interface IReportService {
   /**
    * @author author
    * @Description  通过文件名从数据库中获取文件信息
    * @Date 2020/9/9
    * @param filename
    * @return java.io.InputStream
    **/
   public  InputStream  loadReport(String filename) throws Exception;
   /**
    * @author author
    * @Description 通过文件名删除数据库中的信息，并根据存储的S3key删除S3文件
    * @Date 2020/9/9
    * @param filename
    * @return void
    **/
   public void deleteReport(String filename) throws Exception;
   /**
    * @author author
    * @Description 获取保存的所有的文件内容，并且以最后更新时间进行排序
    * @Date 2020/9/9
    * @param
    * @return java.util.List<com.bstek.ureport.provider.report.ReportFile>
    **/
   public List<ReportFile> getReportFiles() throws Exception;
   /**
    * @author author
    * @Description 保存
    * @Date 2020/9/9
    * @param file
    * @param content
    * @return void
    **/
   public void saveReport(String file,String content) throws Exception;
}
