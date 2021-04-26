package cn.pioneeruniverse.dev.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;
@TableName("tbl_dev_task_remark_attachement")
public class TblDevTaskRemarkAttachement extends BaseEntity{

	/**
	 * 工作任务备注ID
	 **/
	private Long  devTaskRemarkId;//工作任务备注ID

	/**
	 * 重命名后的文件
	 **/
	private String fileNameNew; //重命名后的文件

	/**
	 * 重命名前的文件
	 **/
	private String fileNameOld;//重命名前的文件

	/**
	 * 文件类型
	 **/
	private String fileType;//文件类型

	/**
	 * 文件路径（S3存储后用不着）
	 **/
	private String filePath;//文件路径（S3存储后用不着）

	/**
	 * S3桶
	 **/
	private String fileS3Bucket;//S3桶

	/**
	 * S3 key
	 **/
	private String fileS3Key;//S3 key
	
	public String getFileNameNew() {
		return fileNameNew;
	}
	public void setFileNameNew(String fileNameNew) {
		this.fileNameNew = fileNameNew;
	}
	public String getFileNameOld() {
		return fileNameOld;
	}
	public Long getDevTaskRemarkId() {
		return devTaskRemarkId;
	}
	public void setDevTaskRemarkId(Long devTaskRemarkId) {
		this.devTaskRemarkId = devTaskRemarkId;
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
