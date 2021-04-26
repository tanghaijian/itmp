package cn.pioneeruniverse.report.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class TblReportInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -882774576983544024L;
	
	private Long id;
	
	private String reportName; //制作的报表名称
	
	private String reportCode;//报表编码
	
	private String filenameNew; //重命名后的文件
	
	private String filenameOld;//原文件名
	
	private String fileType;//文件类型
	
	private String filePath;//存放路径
	
	private String fileS3Bucket;//S3对象存储的Bucket
	
	private String fileS3Key;//S3对象存储文件key
	
	private Integer status;//状态 1=正常；2=删除
	
	private Long createBy;//创建者
	
	private Timestamp createDate;//创建时间
	
	private Long lastUpdateBy;//更新者
	
	private Timestamp lastUpdateDate;//更新时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public String getFilenameNew() {
		return filenameNew;
	}

	public void setFilenameNew(String filenameNew) {
		this.filenameNew = filenameNew;
	}

	public String getFilenameOld() {
		return filenameOld;
	}

	public void setFilenameOld(String filenameOld) {
		this.filenameOld = filenameOld;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileS3Bucket() {
		return fileS3Bucket;
	}

	public void setFileS3Bucket(String fileS3Bucket) {
		this.fileS3Bucket = fileS3Bucket;
	}

	public String getFileS3Key() {
		return fileS3Key;
	}

	public void setFileS3Key(String fileS3Key) {
		this.fileS3Key = fileS3Key;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Timestamp getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Timestamp createDate) {
		this.createDate = createDate;
	}

	public Long getLastUpdateBy() {
		return lastUpdateBy;
	}

	public void setLastUpdateBy(Long lastUpdateBy) {
		this.lastUpdateBy = lastUpdateBy;
	}

	public Timestamp getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Timestamp lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

}
