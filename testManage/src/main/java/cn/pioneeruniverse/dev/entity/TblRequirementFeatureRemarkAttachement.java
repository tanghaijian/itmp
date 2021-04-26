package cn.pioneeruniverse.dev.entity;

import java.util.Date;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;

@TableName("tbl_requirement_feature_remark_attachement")
public class TblRequirementFeatureRemarkAttachement extends BaseEntity {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Long requirementFeatureRemarkId;

    private String fileNameNew;

    private String fileNameOld;

    private String fileType;

    private String filePath;

    private String fileS3Bucket;

    private String fileS3Key;


    public Long getRequirementFeatureRemarkId() {
        return requirementFeatureRemarkId;
    }

    public void setRequirementFeatureRemarkId(Long requirementFeatureRemarkId) {
        this.requirementFeatureRemarkId = requirementFeatureRemarkId;
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