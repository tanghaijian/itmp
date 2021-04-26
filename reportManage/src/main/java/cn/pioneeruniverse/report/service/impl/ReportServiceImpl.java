package cn.pioneeruniverse.report.service.impl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bstek.ureport.provider.report.ReportFile;

import cn.pioneeruniverse.common.utils.S3Util;
import cn.pioneeruniverse.report.dao.mybatis.report.UreportMapper;
import cn.pioneeruniverse.report.service.IReportService;
import cn.pioneeruniverse.report.vo.TblReportInfo;

/**
 * 
* @ClassName: ReportServiceImpl
* @Description: 从数据库中读取存储的xml文件信息
* @author author
* @date 2020年7月30日 下午5:17:26
*
 */
@Service
public class ReportServiceImpl implements  IReportService{

	@Autowired
	private UreportMapper ureportMapper;
	
	//自定义报表xml文件存储在s3中的桶名
	@Value("${s3.reportBucket}")
	private String bucketName;
	@Autowired
	private S3Util s3Util;
	private static final String KEY_PREFIX = "report_key_";
	
	
	@Transactional
	@Override
	/**
	* @author author
	* @Description  通过文件名从数据库中获取文件信息
	* @Date 2020/9/9
	* @param filename
	* @return java.io.InputStream
	**/
	public InputStream loadReport(String filename) throws Exception {

        ByteArrayInputStream stream = null;
        //根据文件名获取s3 key
        TblReportInfo info = ureportMapper.selectFileByName(filename);
        if(info != null) {
        	String key = info.getFileS3Key();
        	//获取S3中的文件内容，并且以流的形式输出
        	String content = s3Util.getStringByS3(bucketName,key);
        	if(StringUtils.isNotBlank(content))
        		stream = new ByteArrayInputStream(content.getBytes());
        }
        return stream;
	}


	/**
	* @author author
	* @Description 通过文件名删除数据库中的信息，并根据存储的S3key删除S3文件
	* @Date 2020/9/9
	* @param filename
	* @return void
	**/
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void deleteReport(String filename) throws Exception {
		TblReportInfo info = ureportMapper.selectFileByName(filename);
		if(info != null) {
			String key = info.getFileS3Key();
			ureportMapper.delFileByName(filename);
			//删除S3
			s3Util.deleteObject(bucketName, key);
		}
		
		
		
	}

	/**
	* @author author
	* @Description 获取保存的所有的文件内容，并且以最后更新时间进行排序
	* @Date 2020/9/9
	* @param
	* @return java.util.List<com.bstek.ureport.provider.report.ReportFile>
	**/
	@Transactional
	@Override
	public List<ReportFile> getReportFiles() throws Exception {
		List<TblReportInfo> list = ureportMapper.selectFileList();
		List<ReportFile> fs = new ArrayList<ReportFile>();
		if(CollectionUtils.isNotEmpty(list)) {
			for(TblReportInfo info : list) {
				fs.add(new ReportFile(info.getFilenameOld(),info.getLastUpdateDate()));
			}
		}
		return fs;
	}

	/**
	* @author author
	* @Description 保存
	* @Date 2020/9/9
	* @param file
	* @param content
	* @return void
	**/
	@Transactional(rollbackFor=Exception.class)
	@Override
	public void saveReport(String file, String content) throws Exception {
		
		 //先删除同名的再保存
		  this.deleteReport(file);
         // 保存字符串到S3
		   String key = KEY_PREFIX+UUID.randomUUID().toString().replaceAll("-","");
           s3Util.putObjectLogs(bucketName,key,content);
         // 保存Key和filename之间的关联关系
           TblReportInfo bean = new TblReportInfo();
           Timestamp  time = new Timestamp(new Date().getTime());
           bean.setCreateDate(time);
           bean.setLastUpdateDate(time);
           bean.setFilenameOld(file);
           bean.setReportName(file);
           bean.setFileS3Bucket(bucketName);
           bean.setFileS3Key(key);
           ureportMapper.saveFile(bean);
         
	}


}
