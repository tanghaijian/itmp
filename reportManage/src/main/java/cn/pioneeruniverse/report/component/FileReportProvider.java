package cn.pioneeruniverse.report.component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bstek.ureport.exception.ReportException;
import com.bstek.ureport.provider.report.ReportFile;
import com.bstek.ureport.provider.report.ReportProvider;

import cn.pioneeruniverse.report.service.IReportService;

/**
 * 
* @ClassName: FileReportProvider
* @Description: ureport2文件存储支持类，
*               ureport2将报表的基本信息保存在xml中 
*               此类是为了将ureport2设置的文件保存到s3中
*               该类继承ureport2默认的provider,具体原理，请参w3cschool中的ureport2部分
* @author author 
* @date 2020年7月30日 下午5:10:19
*
 */
@Component
public class FileReportProvider implements ReportProvider{
    private String prefix="s3:";//文件前缀，自定义
    private boolean disabled;//是否可用
    
    @Autowired
    private IReportService iReportService;
    
    /**
     * 
    * @Title: loadReport
    * @Description: 报表制作页面从s3中加载文件到ureport2界面
    * @author author
    * @param 文件名
    * @return
    * @throws
     */
    @Override
    public InputStream loadReport(String file) {
        if(file.startsWith(prefix)){
            file=file.substring(prefix.length(),file.length());
        }
        InputStream stream = null;
        
        try {
        	//读取文件流
        	stream = iReportService.loadReport(file);
        }catch(Exception e) {
        	throw new ReportException(e);
        }
        return stream;
    }
    
    /**
     * 
    * @Title: deleteReport
    * @Description: 报表制作页面删除文件
    * @author author
    * @param file 文件名
    * @throws
     */
    @Override
    public void deleteReport(String file) {
        if(file.startsWith(prefix)){
            file=file.substring(prefix.length(),file.length());
        }
        try {
        	//删除文件
        	iReportService.deleteReport(file);
        }catch(Exception e) {
        	throw new ReportException(e);
        }
        
    }
    
    /**
     * 
    * @Title: getReportFiles
    * @Description: 报表制作页面获取文件列表
    * @author author
    * @return
    * @throws
     */
    @Override
    public List<ReportFile> getReportFiles() {
    	List<ReportFile> list=new ArrayList<ReportFile>();
        try {
        	//从数据库中获取所有的文件列表
        	list = iReportService.getReportFiles();
        }catch(Exception e) {
        	throw new ReportException(e);
        }
        return list;
    }
    /**
     * 
    * @Title: saveReport
    * @Description: 保存页面配置好的报表，并最终保存进s3
    * @author author
    * @param file
    * @param content
    * @throws
     */
    @Override
    public void saveReport(String file,String content) {
        if(file.startsWith(prefix)){
            file=file.substring(prefix.length(),file.length());
        }
        try {
        	iReportService.saveReport(file, content);
        }catch(Exception e) {
        	throw new ReportException(e);
        }
         
    }
    
    /**
     * 
    * @Title: getName
    * @Description: 展示在报表制作器页面中的文件系统名称
    * @author author
    * @return
    * @throws
     */
    @Override
    public String getName() {
        return "S3服务器文件系统";
    }
     
    @Override
    public boolean disabled() {
        return disabled;
    }
     
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }
    @Override
    public String getPrefix() {
        return prefix;
    }

}
