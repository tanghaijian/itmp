package cn.pioneeruniverse.project.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblRequirementAttachement
* @Description: 需求附件bean
* @author author
* @date 2020年8月31日 上午11:07:36
*
 */
@TableName("tbl_requirement_attachement")
public class TblRequirementAttachement extends BaseEntity {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long requirementId; //需求ID
	private String fileNameNew; //重命名后的文件
	private String fileNameOld;//重命名前的文件
	private String fileType;//文件类型，txt等
	private String filePath;//文件存储路径：（未用）
	private String fileS3Bucket;//s3桶
	private String fileS3Key; //s3 key
	public Long getRequirementId() {
		return requirementId;
	}
	public void setRequirementId(Long requirementId) {
		this.requirementId = requirementId;
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
