package cn.pioneeruniverse.project.entity;

import cn.pioneeruniverse.common.entity.BaseEntity;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

import java.util.Date;

public class TblSystemDirectoryDocumentAttachment extends BaseEntity {

    @JsonSerialize(using = ToStringSerializer.class)
    private Long systemDirectoryDocumentId;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long systemDirectoryDocumentChaptersId;

    private String attachmentNameNew;

    private String attachmentNameOld;

    private Integer attachmentType;

    private String attachmentS3Bucket;

    private String attachmentS3Key;

    private String attachmentUrl;

    public Long getSystemDirectoryDocumentId() {
        return systemDirectoryDocumentId;
    }

    public void setSystemDirectoryDocumentId(Long systemDirectoryDocumentId) {
        this.systemDirectoryDocumentId = systemDirectoryDocumentId;
    }

    public Long getSystemDirectoryDocumentChaptersId() {
        return systemDirectoryDocumentChaptersId;
    }

    public void setSystemDirectoryDocumentChaptersId(Long systemDirectoryDocumentChaptersId) {
        this.systemDirectoryDocumentChaptersId = systemDirectoryDocumentChaptersId;
    }

    public String getAttachmentNameNew() {
        return attachmentNameNew;
    }

    public void setAttachmentNameNew(String attachmentNameNew) {
        this.attachmentNameNew = attachmentNameNew == null ? null : attachmentNameNew.trim();
    }

    public String getAttachmentNameOld() {
        return attachmentNameOld;
    }

    public void setAttachmentNameOld(String attachmentNameOld) {
        this.attachmentNameOld = attachmentNameOld == null ? null : attachmentNameOld.trim();
    }

    public String getAttachmentS3Bucket() {
        return attachmentS3Bucket;
    }

    public void setAttachmentS3Bucket(String attachmentS3Bucket) {
        this.attachmentS3Bucket = attachmentS3Bucket == null ? null : attachmentS3Bucket.trim();
    }

    public String getAttachmentS3Key() {
        return attachmentS3Key;
    }

    public void setAttachmentS3Key(String attachmentS3Key) {
        this.attachmentS3Key = attachmentS3Key == null ? null : attachmentS3Key.trim();
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public Integer getAttachmentType() {
        return attachmentType;
    }

    public void setAttachmentType(Integer attachmentType) {
        this.attachmentType = attachmentType;
    }

    @Override
    public String toString() {
        return "TblSystemDirectoryDocumentAttachment{" +
                "systemDirectoryDocumentId=" + systemDirectoryDocumentId +
                ", systemDirectoryDocumentChaptersId=" + systemDirectoryDocumentChaptersId +
                ", attachmentNameNew='" + attachmentNameNew + '\'' +
                ", attachmentNameOld='" + attachmentNameOld + '\'' +
                ", attachmentType=" + attachmentType +
                ", attachmentS3Bucket='" + attachmentS3Bucket + '\'' +
                ", attachmentS3Key='" + attachmentS3Key + '\'' +
                ", attachmentUrl='" + attachmentUrl + '\'' +
                '}';
    }
}