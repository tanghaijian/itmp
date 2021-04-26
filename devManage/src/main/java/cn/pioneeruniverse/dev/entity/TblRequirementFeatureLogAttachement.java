package cn.pioneeruniverse.dev.entity;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.pioneeruniverse.common.entity.BaseEntity;

/**
 * 
* @ClassName: TblRequirementFeatureLogAttachement
* @Description: 开发任务日志附件
* @author author
* @date 2020年8月8日 上午10:11:22
*
 */
@TableName("tbl_requirement_feature_log_attachement")
public class TblRequirementFeatureLogAttachement extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long requirementFeatureLogId; //开发任务日志ID

    private String fileNameNew; //重命名后的附件

    private String fileNameOld; //重命名前的附件

    private String fileType;//文件类型

    private String filePath; //文件路径（未用）

    private String fileS3Bucket; //S3桶

    private String fileS3Key;//S3的KEY值


    public Long getRequirementFeatureLogId() {
        return requirementFeatureLogId;
    }

    public void setRequirementFeatureLogId(Long requirementFeatureLogId) {
        this.requirementFeatureLogId = requirementFeatureLogId;
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

    public void setFileType(String fileType) {
        this.fileType = fileType == null ? null : fileType.trim();
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath == null ? null : filePath.trim();
    }

    public String getFileS3Bucket() {
        return fileS3Bucket;
    }

    public void setFileS3Bucket(String fileS3Bucket) {
        this.fileS3Bucket = fileS3Bucket == null ? null : fileS3Bucket.trim();
    }

    public String getFileS3Key() {
        return fileS3Key;
    }

    public void setFileS3Key(String fileS3Key) {
        this.fileS3Key = fileS3Key == null ? null : fileS3Key.trim();
    }

}