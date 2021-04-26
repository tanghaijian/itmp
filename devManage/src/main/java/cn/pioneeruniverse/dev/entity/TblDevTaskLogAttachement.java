package cn.pioneeruniverse.dev.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *
 * @ClassName:TblDevTaskLogAttachement
 * @Description:工作任务日志附件类
 * @author author
 * @date 2020年8月16日
 *
 */
@TableName("tbl_dev_task_log_attachement")
public class TblDevTaskLogAttachement extends BaseEntity{
	private Long devTaskLogId;//工作任务日志ID
	private String fileNameNew;//新名称
	private String fileNameOld;//旧名称
	private String fileType;//文件类型
	private String filePath;//文件路径
	private String fileS3Bucket;
	private String fileS3Key;
	
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
	public Long getDevTaskLogId() {
		return devTaskLogId;
	}
	public void setDevTaskLogId(Long devTaskLogId) {
		this.devTaskLogId = devTaskLogId;
	}
	

}
