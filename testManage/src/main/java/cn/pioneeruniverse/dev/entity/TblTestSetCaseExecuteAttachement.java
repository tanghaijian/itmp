package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *@author liushan
 *@Description 测试案例执行附件实体类
 *@Date 2020/8/11
 *@return
 **/
public class TblTestSetCaseExecuteAttachement extends BaseEntity{
	//测试集案例执行表主键
	private Long testSetCaseExecuteId;
	//新文件名称
	private String fileNameNew;
	//原文件名称
	private String fileNameOld;
	//文件类型
	private String fileType;
	//存放路径
	private String filePath;
	//S3对象存储的BUCKET
	private String fileS3Bucket;
	//S3对象存储文件KEY
	private String fileS3Key;
	
	
	public Long getTestSetCaseExecuteId() {
		return testSetCaseExecuteId;
	}
	public void setTestSetCaseExecuteId(Long testSetCaseExecuteId) {
		this.testSetCaseExecuteId = testSetCaseExecuteId;
	}
	public String getFileNameNew() {
		return fileNameNew;
	}
	public void setFileNameNew(String fileNameNew) {
		this.fileNameNew = fileNameNew;
	}
	public String getFileNameOld() {
		return fileNameOld;
	}
	public void setFileNameOld(String fileNameOld) {
		this.fileNameOld = fileNameOld;
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
	
}
