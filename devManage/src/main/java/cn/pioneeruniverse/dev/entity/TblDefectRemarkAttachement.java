package cn.pioneeruniverse.dev.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 *@author liushan
 *@Description 缺陷备注附件实体类
 *@Date 2020/8/6
 *@return
 **/
@TableName("tbl_defect_remark_attachement")
public class TblDefectRemarkAttachement extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long defectRemarkId;

    private String fileNameNew;// 附件新的名称

    private String fileNameOld;// 附件旧的名称

    private String fileType;// 附件类型

    private String filePath;// 路径

    private String fileS3Bucket; // S3桶名

    private String fileS3Key;// s3key值

    public Long getDefectRemarkId() {
        return defectRemarkId;
    }

    public void setDefectRemarkId(Long defectRemarkId) {
        this.defectRemarkId = defectRemarkId;
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