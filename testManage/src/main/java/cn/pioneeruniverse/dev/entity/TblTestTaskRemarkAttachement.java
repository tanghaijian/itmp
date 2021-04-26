package cn.pioneeruniverse.dev.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;
@TableName("tbl_test_task_remark_attachement")
public class TblTestTaskRemarkAttachement extends BaseEntity{
	private Long  testTaskRemarkId;

	/**
	 * 新名称
	 **/
	private String fileNameNew;// 新名称

	/**
	 * 旧名称
	 **/
	private String fileNameOld;//旧名称

	/**
	 * 文件类型
	 **/
	private String fileType;// 文件类型

	/**
	 * 路径
	 **/
	private String filePath;// 路径

	/**
	 * s3桶名
	 **/
	private String fileS3Bucket;// s3桶名

	/**
	 * s3key值
	 **/
	private String fileS3Key;// s3key值
	public Long getTestTaskRemarkId() {
		return testTaskRemarkId;
	}
	public void setTestTaskRemarkId(Long testTaskRemarkId) {
		this.testTaskRemarkId = testTaskRemarkId;
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
