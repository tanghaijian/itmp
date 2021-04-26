package cn.pioneeruniverse.dev.entity;

import java.util.Date;

import cn.pioneeruniverse.common.entity.BaseEntity;
/**
 *@author liushan
 *@Description 测试案例步骤执行附件实体类
 *@Date 2020/8/11
 *@return
 **/
public class TblTestSetCaseStepExecuteAttachement extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 89433790705187290L;

	private Long testSetCaseStepExecuteId;// 测试集案例步骤执行id

	private String fileNameNew;// 新名称

	private String fileNameOld;//旧名称

	private String fileType;// 文件类型

	private String filePath;// 路径

	private String fileS3Bucket;// s3桶名

	private String fileS3Key;// s3key值

	public Long getTestSetCaseStepExecuteId() {
		return testSetCaseStepExecuteId;
	}

	public void setTestSetCaseStepExecuteId(Long testSetCaseStepExecuteId) {
		this.testSetCaseStepExecuteId = testSetCaseStepExecuteId;
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