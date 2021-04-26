package cn.pioneeruniverse.dev.entity;

public class FtpS3Vo {

	/**
	 * S3 Key
	 **/
	private String keyName; //S3 Key

	/**
	 * 文件名
	 **/
	private String fileName; //文件名

	/**
	 * ftp文件路径
	 **/
	private String ftpPath; //ftp文件路径

	/**
	 * 文件类型：1configuration，2document，3sql，4package
	 **/
	private Integer fileType;// 文件类型：1configuration，2document，3sql，4package

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFtpPath() {
		return ftpPath;
	}

	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}

	public Integer getFileType() {
		return fileType;
	}

	public void setFileType(Integer fileType) {
		this.fileType = fileType;
	}

}
