package cn.pioneeruniverse.dev.entity;

import java.sql.Timestamp;
import cn.pioneeruniverse.common.entity.BaseEntity;
import cn.pioneeruniverse.common.utils.CommonUtil;
import com.baomidou.mybatisplus.annotations.TableName;

import javax.servlet.http.HttpServletRequest;

@TableName("tbl_dev_task_attachement")
public class TblDevTaskAttachement extends BaseEntity{

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long DevTaskId; //工作任务ID

    private String fileNameNew; //重命名后的文件名

    private String fileNameOld; //重命名前的文件名

    private String fileType; //文件类型

    private String filePath; //存储路径（以S3存储后，该字段未用）

    private String fileS3Bucket; //S3桶名

    private String fileS3Key; //S3KEY

    public TblDevTaskAttachement(){}

    public TblDevTaskAttachement( HttpServletRequest request){
        Long userId = CommonUtil.getCurrentUserId(request);
        this.setStatus(1);
        this.setCreateBy(userId);
        this.setCreateDate(new Timestamp(System.currentTimeMillis()));
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

    public String getFileNameNew() {
        return fileNameNew;
    }

    public void setFileNameNew(String fileNameNew) {
        this.fileNameNew = fileNameNew == null ? null : fileNameNew.trim();
    }

    public String getFileNameOld() {
        return fileNameOld;
    }

    public void setFileNameOld(String fileNameOld) {
        this.fileNameOld = fileNameOld == null ? null : fileNameOld.trim();
    }

    public String getFileType() {
        return fileType;
    }

  

	public Long getDevTaskId() {
		return DevTaskId;
	}

	public void setDevTaskId(Long devTaskId) {
		DevTaskId = devTaskId;
	}

	public void setFileType(String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }
}